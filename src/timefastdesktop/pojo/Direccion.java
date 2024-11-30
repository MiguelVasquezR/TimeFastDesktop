package timefastdesktop.pojo;

public class Direccion {

    private String calle;
    private String colonia;
    private String numero;
    private String codigoPostal;
    private String ciudad;
    private String estado;

    public Direccion() {
    }

    public Direccion(String calle, String colonia, String numero, String codigoPostal, String ciudad, String estado) {
        this.calle = calle;
        this.colonia = colonia;
        this.numero = numero;
        this.codigoPostal = codigoPostal;
        this.ciudad = ciudad;
        this.estado = estado;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
