package org.utils;

import org.dao.*;
import javax.persistence.EntityManager;

/**
 * CSVImporter
 * Clase utilitaria para importar todos los CSV de datos iniciales.
 * Ahora recibe los DAO por parámetro, sin crearlos internamente.
 */
public class CSVImporter {

    private CSVImporter() {
        // 🔒 Evita instanciación
    }

    /**
     * Ejecuta la importación de todos los CSV usando los DAO recibidos.
     */
    public static void importarCSV(
            EntityManager conn,
            EstudianteDAO estudianteDAO,
            CarreraDAO carreraDAO,
            InscripcionDAO inscripcionDAO
    ) {
        try {
            // Crear el loader con los DAO recibidos
            CsvLoader loader = new CsvLoader(estudianteDAO, carreraDAO, inscripcionDAO, conn);

            // Ejecutar importación de los 3 CSV
            loader.importAll(
                    "src/main/resources/estudiantes.csv",
                    "src/main/resources/carreras.csv",
                    "src/main/resources/estudianteCarrera.csv"
            );

            System.out.println("✔ Importación CSV completada con éxito.");

        } catch (Exception e) {
            System.err.println("❌ Error al importar CSV:");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
