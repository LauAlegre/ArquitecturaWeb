import dao.SchemaGenerator.SchemaGenerator;

public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando aplicación...");

        // Tarea 1: Crear el esquema de la base de datos.
        // Llamamos al método estático de nuestra clase de utilidad.
        SchemaGenerator.createSchema();

        // Aquí irían las llamadas a las siguientes tareas, como cargar los datos
        // y generar los reportes.

        System.out.println("Proceso finalizado.");
    }
}

