package timefastdesktop;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import timefastdesktop.utilidades.Navegacion;

public class FXMLLoginController implements Initializable {

    private Label label;
    @FXML
    private TextField txEmail;
    @FXML
    private PasswordField psPassword;
    @FXML
    private Label lbErrorEmail;
    @FXML
    private Label lbErrorPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    @FXML
    private void btnIngresar(ActionEvent event) {
        Navegacion.cambiarPantalla(lbErrorEmail, "TimeFast | Pantalla Principal", "FXMLPrincipal.fxml", this.getClass());
    }

    private boolean camposVacios() {
        boolean band = false;
        lbErrorPassword.setText("");
        lbErrorEmail.setText("");
        if (psPassword.getText().isEmpty() || psPassword.getText().equals("")) {
            lbErrorPassword.setText("La contraseña es necesaria para ingresar al sistema");
            band = true;
        }
        if (txEmail.getText().isEmpty() || txEmail.getText().equals("")) {
            lbErrorEmail.setText("El correo es necesario para ingresar al sistema");
            band = true;
        }
        return band;
    }

    @FXML
    private void btnOlvide(MouseEvent event) {
        Navegacion.cambiarPantalla(lbErrorEmail, "TimeFast | Olvide Contraseña", "FXMLOlvideContrasena.fxml", this.getClass());
    }

}
