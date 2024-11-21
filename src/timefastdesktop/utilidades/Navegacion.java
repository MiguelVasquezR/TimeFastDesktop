package timefastdesktop.utilidades;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Navegacion {

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
