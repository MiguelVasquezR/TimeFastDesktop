package timefastdesktop;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import timefastdesktop.utilidades.Navegacion;

public class FXMLOlvideContrasenaController implements Initializable {

    @FXML
    private Label lbTexto;
    @FXML
    private TextField txCorreo;
    @FXML
    private Label lbCorreoError;
    @FXML
    private Button btnRecuperarC;
    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void btnRecuperar(ActionEvent event) {
        String correo = txCorreo.getText();
        lbCorreoError.setText("");
        if (!esCorreo(correo)) {
            lbCorreoError.setText("Debes ingresar un correo valido");
            return;
        }

        procesarRecuperacion();

    }

    private boolean esCorreo(String correo) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);
        return matcher.matches();
    }

    private void procesarRecuperacion() {
        txCorreo.setVisible(false);
        lbCorreoError.setVisible(false);
        btnRecuperarC.setVisible(false);
        btnCancelar.setText("Regresar");
        lbTexto.setText("Revise su correo electrónico, se le ha enviado un link para cambiar su contraseña");
        lbTexto.setFont(new Font(20));
        lbTexto.setWrapText(true);
        lbTexto.setPrefHeight(100);
    }

    @FXML
    private void btnRegresar(ActionEvent event) {
        Navegacion.cambiarPantalla(lbTexto, "TimeFast | Pantalla Principal", "FXMLLogin.fxml", this.getClass());
    }

}
