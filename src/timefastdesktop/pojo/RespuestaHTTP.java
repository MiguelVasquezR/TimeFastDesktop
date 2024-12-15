package timefastdesktop.pojo;

public class RespuestaHTTP {

    private Integer codigoRespuesta;
    private String contenido;

    public RespuestaHTTP() {
    }

    public RespuestaHTTP(int codigoRespuesta, String contenido) {
        this.codigoRespuesta = codigoRespuesta;
        this.contenido = contenido;
    }

    public int getCodigoRespuesta() {
        return codigoRespuesta;
    }

    public void setCodigoRespuesta(int codigoRespuesta) {
        this.codigoRespuesta = codigoRespuesta;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    @Override
    public String toString() {
        return "RespuestaHTTP{" + "codigoRespuesta=" + codigoRespuesta + ", contenido=" + contenido + '}';
    }
    
    

}
