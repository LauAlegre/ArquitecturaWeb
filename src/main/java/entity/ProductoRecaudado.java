package entity;

public class ProductoRecaudado {
    private final int idProducto;
    private final String nombre;
    private final Float recaudacion;

    public ProductoRecaudado(int idProducto, String nombre, Float recaudacion) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.recaudacion = recaudacion;
    }

    public int getIdProducto() { return idProducto; }
    public String getNombre() { return nombre; }
    public Float getRecaudacion() { return recaudacion; }
}