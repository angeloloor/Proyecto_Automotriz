import java.util.Date;
/**
 *Esta es la clase Transacción
 * @author Angelo Loor
 * @version 2023 B
 */
public class Transaccion {
    private int id;
    private Date fecha;
    private int clienteId;
    private double total;

    public Transaccion(int id, Date fecha, int clienteId, double total) {
        this.id = id;
        this.fecha = fecha;
        this.clienteId = clienteId;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}

