package timefastdesktop.utilidades;

public class SesionUsuario {
    private static SesionUsuario instancia;
    private String noPersonal;

    private SesionUsuario() {
        // Constructor privado para implementar Singleton
    }

    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    public String getNoPersonal() {
        return noPersonal;
    }

    public void setNoPersonal(String noPersonal) {
        this.noPersonal = noPersonal;
    }
}
