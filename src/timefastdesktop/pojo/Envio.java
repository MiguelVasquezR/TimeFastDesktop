package timefastdesktop.pojo;

public class Envio {

    private String idCliente;
    private Direccion origen;
    private Direccion destino;
    private String numGuia;
    private String costo;

    public Envio() {
    }

    public Envio(String idCliente, Direccion origen, Direccion destino, String numGuia, String costo) {
        this.idCliente = idCliente;
        this.origen = origen;
        this.destino = destino;
        this.numGuia = numGuia;
        this.costo = costo;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public Direccion getOrigen() {
        return origen;
    }

    public void setOrigen(Direccion origen) {
        this.origen = origen;
    }

    public Direccion getDestino() {
        return destino;
    }

    public void setDestino(Direccion destino) {
        this.destino = destino;
    }

    public String getNumGuia() {
        return numGuia;
    }

    public void setNumGuia(String numGuia) {
        this.numGuia = numGuia;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

}
