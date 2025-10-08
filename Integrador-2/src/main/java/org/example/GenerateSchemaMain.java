package org.example;

import org.utils.CSVImporter;
import org.dao.CarreraDAO;
import org.dao.EstudianteDAO;
import org.dao.InscripcionDAO;
import org.factory.DAOFactory;
import org.dto.EstudianteDTO;
import org.dto.CarreraDTO; // <-- agregado

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class GenerateSchemaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my_persistence_unit");
        EntityManager em = emf.createEntityManager();

        try {
            // 🔹 Crear la factory y los DAO
            DAOFactory factory = new DAOFactory(em);
            EstudianteDAO estudianteDAO = factory.getEstudianteDAO();
            CarreraDAO carreraDAO = factory.getCarreraDAO();
            InscripcionDAO inscripcionDAO = factory.getInscripcionDAO();

            // 🔹 Importar los CSV
            CSVImporter.importarCSV(em, estudianteDAO, carreraDAO, inscripcionDAO);
            System.out.println("\n✅ Esquema generado y datos cargados desde CSV.");

            System.out.println("\n--- 🎓 Estudiantes ordenados por nombre ASC ---");
            List<EstudianteDTO> estudiantesOrdenados = estudianteDAO.findAllOrderByNombreAsc();
            estudiantesOrdenados.forEach(System.out::println);

            System.out.println("\n--- 🔍 Buscar estudiante por Nro de Libreta (ej: 1) ---");
            EstudianteDTO porLibreta = estudianteDAO.findByNroLibreta(56695);
            System.out.println(porLibreta != null ? porLibreta : "⚠ No se encontró estudiante con LU=1");

            System.out.println("\n--- 🚻 Estudiantes por género (ej: 'F') ---");
            List<EstudianteDTO> porGenero = estudianteDAO.findByGenero("Male");
            porGenero.forEach(System.out::println);

            System.out.println("\n--- 🏫 Estudiantes de carrera=1 en ciudad='Paquera' ---");
            List<EstudianteDTO> porCarreraCiudad = estudianteDAO.findByCarreraAndCiudad(1, "Paquera");
            porCarreraCiudad.forEach(System.out::println);

            // ==== Carreras con inscriptos (orden desc por cantidad) ====
            System.out.println("\n--- 📊 Carreras con inscriptos (desc por cantidad) ---");
            List<CarreraDTO> carrerasConInscriptos = carreraDAO.findCarrerasConInscriptosOrdenadasPorCantidadDesc();
            carrerasConInscriptos.forEach(System.out::println);

            // 🔹 Obtener el reporte de carreras con inscriptos y egresados por año
            List<Object[]> reporte = inscripcionDAO.getReporteCarrerasPorAnio();

            // 🔹 Mostrar el reporte formateado
            String carreraActual = "";
            System.out.println("\n================= REPORTE DE CARRERAS =================");
            for (Object[] fila : reporte) {
                String carrera = (String) fila[0];
                int anio = (int) fila[1];
                long inscriptos = (long) fila[2];
                long egresados = (long) fila[3];

                // Si cambia la carrera, imprimimos un encabezado nuevo
                if (!carrera.equals(carreraActual)) {
                    System.out.println("\nCarrera: " + carrera);
                    System.out.println("Año\tInscriptos\tEgresados");
                    carreraActual = carrera;
                }

                System.out.println(anio + "\t" + inscriptos + "\t\t" + egresados);
            }

            System.out.println("\n========================================================");

        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            ex.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
