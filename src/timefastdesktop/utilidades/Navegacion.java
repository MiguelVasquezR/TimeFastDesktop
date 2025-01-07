package timefastdesktop.utilidades;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import timefastdesktop.FXMLPrincipalController;
import timefastdesktop.pojo.Colaborador;

public class Navegacion {

    public static void cambiarPantallaInicioSesion(Label elementoVista, String tituloVista, String nombreArchivo, Class<?> clase,
            Colaborador colaboradorJSON) {
        try {
            Stage escenarioBase = (Stage) elementoVista.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(clase.getResource(nombreArchivo));
            Parent principal = loader.load();
            FXMLPrincipalController controlador = loader.getController();
            controlador.setColaborador(colaboradorJSON);
            Scene escenaPrincipal = new Scene(principal);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle(tituloVista);
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    public static void cambiarPantalla(Label elementoVista, String tituloVista, String nombreArchivo, Class<?> clase) {
        try {
            Stage escenarioBase = (Stage) elementoVista.getScene().getWindow();
            Parent principal = FXMLLoader.load(clase.getResource(nombreArchivo));
            Scene escenaPrincipal = new Scene(principal);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle(tituloVista);
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
