package org.Utils;

import org.dao.*;
import org.entity.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class CsvLoader {

    private final EstudianteDAO estudianteDAO;
    private final CarreraDAO carreraDAO;
    private final InscripcionDAO inscripcionDAO;

    public CsvLoader(EstudianteDAO estudianteDAO, CarreraDAO carreraDAO, InscripcionDAO inscripcionDAO) {
        this.estudianteDAO = estudianteDAO;
        this.carreraDAO = carreraDAO;
        this.inscripcionDAO = inscripcionDAO;
    }

    public void importAll(String estudiantesPath, String carrerasPath, String inscripcionesPath) throws Exception {
        importEstudiantes(estudiantesPath);
        importCarreras(carrerasPath);
        importInscripciones(inscripcionesPath);
    }

    private void importEstudiantes(String path) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            br.readLine(); // Saltar cabecera
            while ((line = br.readLine()) != null) {
                String[] campos = line.split(",");
                Estudiante e = new Estudiante();
                e.setNombre(campos[1]);
                e.setFechaNacimiento(sdf.parse(campos[2]));
                e.setGenero(campos[3]);
                e.setNroDocumento(Integer.parseInt(campos[4]));
                e.setCiudadResidencia(campos[5]);
                // Asume que el primer campo es el id
                // Si tienes setNumeroDeLibreta, agrégalo aquí
                estudianteDAO.alta(e);
            }
        }
    }

    private void importCarreras(String path) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine(); // Saltar cabecera
            while ((line = br.readLine()) != null) {
                String[] campos = line.split(",");
                Carrera c = new Carrera();
                // Asume que el primer campo es el id
                // Si tienes setIdCarrera, agrégalo aquí
                // c.setIdCarrera(Integer.parseInt(campos[0]));
                // c.setNombre(campos[1]);
                // c.setDuracionAnios(Integer.parseInt(campos[2]));
                carreraDAO.alta(c);
            }
        }
    }

    private void importInscripciones(String path) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            br.readLine(); // Saltar cabecera
            while ((line = br.readLine()) != null) {
                String[] campos = line.split(",");
                Inscripcion insc = new Inscripcion();
                InscripcionId id = new InscripcionId();
                id.setIdEstudiante(Integer.parseInt(campos[0]));
                id.setIdCarrera(Integer.parseInt(campos[1]));
                insc.setId(id);
                insc.setFechaInscripcion(sdf.parse(campos[2]));
                if (!campos[3].isEmpty()) {
                    insc.setFechaGraduado(sdf.parse(campos[3]));
                }
                // Si necesitas asociar Estudiante y Carrera, puedes obtenerlos por id usando los DAOs
                inscripcionDAO.alta(insc);
            }
        }
    }
}

