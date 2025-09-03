package entity;

public class ClienteFacturacion {
    private final int idCliente;
    private final String nombre;
    private final String email;
    private final Float total;

    public ClienteFacturacion(int idCliente, String nombre, String email, Float total) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.total = total;
    }

    public int getIdCliente() { return idCliente; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Float getTotal() { return total; }
}