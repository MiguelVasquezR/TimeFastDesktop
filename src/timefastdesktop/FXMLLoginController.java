package timefastdesktop;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import timefastdesktop.modelo.dao.LoginDAO;
import timefastdesktop.pojo.Colaborador;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.utilidades.Navegacion;
import timefastdesktop.utilidades.Utilidades;

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

        if (!camposVacios()) {
            Mensaje msj = LoginDAO.iniciarSesion(txEmail.getText(), psPassword.getText());
            Gson gson = new Gson();
            if (msj.getError() == false) {
                Utilidades.mostrarAlertaSimple("Sesión Iniciada", msj.getMensaje(), Alert.AlertType.CONFIRMATION);
                JsonObject json = gson.fromJson(msj.getObjeto().toString(), JsonObject.class);
                Colaborador colaboradorJSON = gson.fromJson(json.get("value"), Colaborador.class);
                Navegacion.cambiarPantallaInicioSesion(lbErrorEmail, "TimeFast | Pantalla Principal", "FXMLPrincipal.fxml", this.getClass(), colaboradorJSON);
            } else {
                Utilidades.mostrarAlertaSimple("Error al iniciar sesión", msj.getMensaje(), Alert.AlertType.ERROR);
            }
        }
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
