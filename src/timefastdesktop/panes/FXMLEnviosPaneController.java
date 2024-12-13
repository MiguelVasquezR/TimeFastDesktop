package timefastdesktop.panes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import timefastdesktop.pojo.Direccion;
import timefastdesktop.pojo.Envio;
import timefastdesktop.utilidades.Utilidades;

public class FXMLEnviosPaneController implements Initializable {

    @FXML
    private ScrollPane scroll;
    @FXML
    private Label lbUsuario;
    @FXML
    private Button btnBuscar;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TextField tfCalle;
    @FXML
    private Label lbErrorCalle;
    @FXML
    private TextField tfColonia;
    @FXML
    private Label lbErrorColonia;
    @FXML
    private TextField tfNumero;
    @FXML
    private Label lbErrorNumero;
    @FXML
    private TextField tfCP;
    @FXML
    private Label lbErrorCP;
    @FXML
    private TextField tfCiudad;
    @FXML
    private Label lbErrorCiudad;
    @FXML
    private TextField tfEstado;
    @FXML
    private Label lbErrorEstado;
    @FXML
    private TextField tfCliente;
    @FXML
    private Label lbErrorCliente;
    @FXML
    private ComboBox<Direccion> cbOrigen;
    @FXML
    private Label lbErrorOrigen;
    @FXML
    private TextField tfNumGuia;
    @FXML
    private Label lbErrorNumGuia;
    @FXML
    private TextField tfPrecio;
    @FXML
    private Label lbErrorPrecio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilidades.estilizarBarraScroll(scroll);
        resetearLabelError();
    }

    private void resetearLabelError() {
        lbErrorCalle.setText("");
        lbErrorColonia.setText("");
        lbErrorNumero.setText("");
        lbErrorCP.setText("");
        lbErrorCiudad.setText("");
        lbErrorEstado.setText("");
        lbErrorCliente.setText("");
        lbErrorOrigen.setText("");
        lbErrorNumGuia.setText("");
        lbErrorPrecio.setText("");
    }

    private Boolean validarDatos() {
        boolean esValido = true;

        resetearLabelError();

        if (tfCalle.getText() == null || tfCalle.getText().trim().isEmpty()) {
            lbErrorCalle.setText("La calle no puede estar vacía.");
            esValido = false;
        }

        if (tfColonia.getText() == null || tfColonia.getText().trim().isEmpty()) {
            lbErrorColonia.setText("La colonia no puede estar vacía.");
            esValido = false;
        }

        if (tfNumero.getText() == null || tfNumero.getText().trim().isEmpty()) {
            lbErrorNumero.setText("El número no puede estar vacío.");
            esValido = false;
        } else {
            try {
                Integer.parseInt(tfNumero.getText());
            } catch (NumberFormatException e) {
                lbErrorNumero.setText("El número debe ser un valor numérico.");
                esValido = false;
            }
        }

        if (tfCP.getText() == null || tfCP.getText().trim().isEmpty()) {
            lbErrorCP.setText("El código postal no puede estar vacío.");
            esValido = false;
        } else if (tfCP.getText().length() != 5) {
            lbErrorCP.setText("El código postal debe tener 5 dígitos.");
            esValido = false;
        } else {
            try {
                Integer.parseInt(tfCP.getText());
            } catch (NumberFormatException e) {
                lbErrorCP.setText("El código postal debe ser un valor numérico.");
                esValido = false;
            }
        }

        if (tfCiudad.getText() == null || tfCiudad.getText().trim().isEmpty()) {
            lbErrorCiudad.setText("La ciudad no puede estar vacía.");
            esValido = false;
        }

        if (tfEstado.getText() == null || tfEstado.getText().trim().isEmpty()) {
            lbErrorEstado.setText("El estado no puede estar vacío.");
            esValido = false;
        }

        if (tfCliente.getText() == null || tfCliente.getText().trim().isEmpty()) {
            lbErrorCliente.setText("El cliente no puede estar vacío.");
            esValido = false;
        }

        if (cbOrigen.getValue() == null) {
            lbErrorOrigen.setText("Debe seleccionar un origen.");
            esValido = false;
        }

        if (tfNumGuia.getText() == null || tfNumGuia.getText().trim().isEmpty()) {
            lbErrorNumGuia.setText("El número de guía no puede estar vacío.");
            esValido = false;
        }

        if (tfPrecio.getText() == null || tfPrecio.getText().trim().isEmpty()) {
            lbErrorPrecio.setText("El precio no puede estar vacío.");
            esValido = false;
        } else {
            try {
                Double.parseDouble(tfPrecio.getText());
            } catch (NumberFormatException e) {
                lbErrorPrecio.setText("El precio debe ser un valor numérico.");
                esValido = false;
            }
        }

        return esValido;

    }

    private void llenarPojos() {
        /*
        Direccion origen = new Direccion(
                tfCalle.getText(),
                tfColonia.getText(),
                tfNumero.getText(),
                tfCP.getText(),
                tfCiudad.getText(),
                tfEstado.getText()
        );

        Direccion destino = new Direccion(
                "Calle de ejemplo",
                "Colonia de ejemplo",
                "123",
                "45678",
                "Ciudad de ejemplo",
                "Estado de ejemplo"
        );

        Envio envio = new Envio(
                tfCliente.getText(),
                origen,
                destino,
                tfNumGuia.getText(),
                tfPrecio.getText()
        );
         */

    }

    @FXML
    private void botonPresionado(ActionEvent event) {
        if (validarDatos()) {
            llenarPojos();
        }
    }

}
