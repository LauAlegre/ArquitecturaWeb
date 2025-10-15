package org.utils;

import org.repository.CarreraRepository;
import org.repository.EstudianteRepository;
import org.repository.InscripcionRepository;

import javax.persistence.EntityManager;
import java.io.*;

/**
 * CsvLoader
 * Carga estudiantes, carreras e inscripciones (ignorando el primer campo del CSV de inscripciones).
 *
 */
public class CsvLoader {

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final InscripcionRepository inscripcionRepository;
    private final EntityManager em;

    public CsvLoader(EstudianteRepository estudianteRepository, CarreraRepository carreraRepository, InscripcionRepository inscripcionRepository, EntityManager em) {
        this.estudianteRepository = estudianteRepository;
        this.carreraRepository = carreraRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.em = em;
    }

    public void importAll(String estudiantesPath, String carrerasPath, String inscripcionesPath) throws Exception {
        System.out.println("=== INICIO DE IMPORTACIÓN ===");
        importEstudiantes(estudiantesPath);
        importCarreras(carrerasPath);
        importInscripciones(inscripcionesPath);
        System.out.println("=== IMPORTACIÓN COMPLETA ===");
    }

    // ------------------------------------------------------
    // 1️⃣ Importar estudiantes
    // ------------------------------------------------------
    private void importEstudiantes(String path) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // Cabecera: DNI,nombre,apellido,edad,genero,ciudad,LU

            em.getTransaction().begin();
            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] campos = line.split(",");

                if (campos.length < 7) {
                    System.err.println("❌ Línea inválida (estudiantes): " + line);
                    continue;
                }

                try {
                    int dni = Integer.parseInt(campos[0].trim());
                    String nombre = campos[1].trim();
                    String apellido = campos[2].trim();
                    String genero = campos[4].trim();
                    String ciudad = campos[5].trim();
                    int nroLibreta = Integer.parseInt(campos[6].trim());
                    System.out.println("Importando estudiante LU " + nroLibreta + ": " + nombre + " " + apellido);

                    estudianteRepository.cargarEstudiante(nroLibreta, nombre, apellido, genero, ciudad, dni);
                    count++;

                } catch (Exception e) {
                    System.err.println("⚠ Error en estudiante → " + e.getMessage() + " | Línea: " + line);
                }
            }

            em.getTransaction().commit();
            System.out.println("✔ Estudiantes cargados: " + count);
        }
    }

    // ------------------------------------------------------
    // 2️⃣ Importar carreras
    // ------------------------------------------------------
    private void importCarreras(String path) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // Cabecera: id,nombre,duracion

            em.getTransaction().begin();
            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] campos = line.split(",");

                if (campos.length < 3) {
                    System.err.println("❌ Línea inválida (carreras): " + line);
                    continue;
                }

                try {
                    int idCarrera = Integer.parseInt(campos[0].trim());
                    String nombre = campos[1].trim();
                    int duracion = Integer.parseInt(campos[2].trim());

                    carreraRepository.cargarCarrera(idCarrera, nombre, duracion);
                    count++;

                } catch (Exception e) {
                    System.err.println("⚠ Error en carrera → " + e.getMessage() + " | Línea: " + line);
                }
            }

            em.getTransaction().commit();
            System.out.println("✔ Carreras cargadas: " + count);
        }
    }

    // ------------------------------------------------------
    // 3️⃣ Importar inscripciones (ignorando el primer campo)
    // ------------------------------------------------------
    private void importInscripciones(String path) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // Cabecera: id, idEstudiante, idCarrera, fechaInscripcion, fechaGraduado, antiguedad

            em.getTransaction().begin();
            int count = 0;

            while ((line = br.readLine()) != null) {
                String[] campos = line.split(",");

                if (campos.length < 6) {
                    System.err.println("❌ Línea inválida (inscripciones): " + line);
                    continue;
                }

                try {
                    // IGNORAR el primer campo (id autoincremental)
                    int nroDocumento = Integer.parseInt(campos[1].trim()); // segunda columna
                    int idCarrera = Integer.parseInt(campos[2].trim());
                    int fechaInscripcion = parseIntSafe(campos[3]);
                    int fechaGraduacion = parseIntSafe(campos[4]);
                    int antiguedad = parseIntSafe(campos[5]);

                    if (estudianteRepository.findById(nroDocumento) == null) {
                        System.err.println("⚠ Estudiante no encontrado (DOC " + nroDocumento + ")");
                        continue;
                    }
                    if (carreraRepository.getCarreraById(idCarrera) == null) {
                        System.err.println("⚠ Carrera no encontrada (ID " + idCarrera + ")");
                        continue;
                    }

                    inscripcionRepository.matricular(nroDocumento, idCarrera, fechaInscripcion, fechaGraduacion, antiguedad);
                    count++;

                } catch (Exception e) {
                    System.err.println("⚠ Error en inscripción → " + e.getMessage() + " | Línea: " + line);
                }
            }

            em.getTransaction().commit();
            System.out.println("✔ Inscripciones cargadas: " + count);
        }
    }

    // ------------------------------------------------------
    // Utilidad para parsear enteros (maneja vacíos)
    // ------------------------------------------------------
    private int parseIntSafe(String s) {
        try {
            s = s.trim();
            if (s.isEmpty()) return 0;
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }
}
