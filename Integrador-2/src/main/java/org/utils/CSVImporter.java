package org.utils;

import org.repository.CarreraRepository;
import org.repository.EstudianteRepository;
import org.repository.InscripcionRepository;

import javax.persistence.EntityManager;

/**
 * CSVImporter
 * Clase utilitaria para importar todos los CSV de datos iniciales.

 */
public class CSVImporter {

    private CSVImporter() {
        // 🔒 Evita instanciación
    }


    public static void importarCSV(
            EntityManager conn,
            EstudianteRepository estudianteRepository,
            CarreraRepository carreraRepository,
            InscripcionRepository inscripcionRepository
    ) {
        try {

            CsvLoader loader = new CsvLoader(estudianteRepository, carreraRepository, inscripcionRepository, conn);

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
