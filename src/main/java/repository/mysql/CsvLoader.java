package repository.mysql;

import dao.*;
import entity.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;

public class CsvLoader {
    private final ClienteDAO clienteDAO;
    private final ProductoDAO productoDAO;
    private final FacturaDAO facturaDAO;
    private final FacturaProductoDAO facturaProductoDAO;

    public CsvLoader(ClienteDAO clienteDAO,
                     ProductoDAO productoDAO,
                     FacturaDAO facturaDAO,
                     FacturaProductoDAO facturaProductoDAO) {
        this.clienteDAO = clienteDAO;
        this.productoDAO = productoDAO;
        this.facturaDAO = facturaDAO;
        this.facturaProductoDAO = facturaProductoDAO;
    }

    public void importAll(String clientes, String productos, String facturas, String items) throws Exception {
        loadClientes(clientes);
        loadProductos(productos);
        loadFacturas(facturas);
        loadItems(items);
    }

    private CSVFormat buildFormat() {
        return CSVFormat.DEFAULT.builder()
                .setHeader()               // usa la primera fila como header
                .setSkipHeaderRecord(true) // saltea esa fila al iterar
                .build();
    }

    private void loadClientes(String path) throws Exception {
        try (CSVParser p = buildFormat().parse(new FileReader(path))) {
            for (CSVRecord r : p) {
                Cliente c = new Cliente(
                        r.get("nombre"),
                        r.get("email")
                );
                clienteDAO.insert(c);
            }
        }
    }

    private void loadProductos(String path) throws Exception {
        try (CSVParser p = buildFormat().parse(new FileReader(path))) {
            for (CSVRecord r : p) {
                Producto pr = new Producto(
                        Integer.parseInt(r.get("idProducto")),
                        r.get("nombre"),
                        Float.parseFloat(r.get("valor")) // si tu entidad usa BigDecimal: new BigDecimal(r.get("valor"))
                );
                productoDAO.insert(pr);
            }
        }
    }

    private void loadFacturas(String path) throws Exception {
        try (CSVParser p = buildFormat().parse(new FileReader(path))) {
            for (CSVRecord r : p) {
                Factura f = new Factura(
                        Integer.parseInt(r.get("idFactura")),
                        Integer.parseInt(r.get("idCliente"))
                        // si tu entidad Factura tiene fecha, agregala acá también
                );
                facturaDAO.insert(f);
            }
        }
    }

    private void loadItems(String path) throws Exception {
        try (CSVParser p = buildFormat().parse(new FileReader(path))) {
            for (CSVRecord r : p) {
                FacturaProducto it = new FacturaProducto(
                        Integer.parseInt(r.get("idFactura")),
                        Integer.parseInt(r.get("idProducto")),
                        Integer.parseInt(r.get("cantidad"))
                );
                facturaProductoDAO.insert(it);
            }
        }
    }
}
