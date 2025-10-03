package org.Utils;

import org.dao.*;
import org.factory.*;
import org.repository.MySQLDaoFactory;

import java.sql.Connection;

public class CSVImporter {

    private CSVImporter() {
        // 🔒 Constructor privado para que no se instancie esta clase utilitaria
    }

    public static void importarCSV(Connection conn) {
        try {
            // Obtener DAOs desde la factory
            DAOFactory factory = new MySQLDaoFactory(conn);
            EstudianteDAO estudianteDAO = factory.getEstudianteDAO();
            CarreraDAO carreraDAO = factory.getCarreraDAO();
            InscripcionDAO inscripcionDAO = factory.getInscripcionDAO();

            // Instanciar el CsvLoader
            CsvLoader loader = new CsvLoader(estudianteDAO, carreraDAO, inscripcionDAO);

            // Importar todos los archivos
            loader.importAll(
                    "src/main/java/resources/estudiantes.csv",
                    "src/main/java/resources/carreras.csv",
                    "src/main/java/resources/inscripciones.csv"
            );

            System.out.println("✔ Importación CSV completada con éxito.");

        } catch (Exception e) {
            System.err.println("❌ Error al importar CSV:");
            e.printStackTrace();
        }
    }
}
