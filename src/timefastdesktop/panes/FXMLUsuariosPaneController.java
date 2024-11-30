/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timefastdesktop.panes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import timefastdesktop.pojo.Cliente;
import timefastdesktop.pojo.Direccion;
import timefastdesktop.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author vasquez
 */
public class FXMLUsuariosPaneController implements Initializable {

    @FXML
    private Label lbUsuario;
    @FXML
    private TextField tfBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private ScrollPane scroll;
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
    private TextField tfNombre;
    @FXML
    private Label lbErrorNombre;
    @FXML
    private TextField tfPaterno;
    @FXML
    private Label lbErrorPaterno;
    @FXML
    private Label lbErrorMaterno;
    @FXML
    private TextField tfTelefono;
    @FXML
    private Label lbErrorTelefono;
    @FXML
    private TextField tfCorreo;
    @FXML
    private Label lbErrorCorreo;
    @FXML
    private TextField tfMaterno;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilidades.estilizarBarraScroll(scroll);
        resetearLabelErrors();
    }

    public void llenarPojo() {
        Direccion direccion = new Direccion(
                tfCalle.getText(),
                tfColonia.getText(),
                tfNumero.getText(),
                tfCP.getText(),
                tfCiudad.getText(),
                tfEstado.getText()
        );

       /* Cliente cliente = new Cliente(
                tfNombre.getText(),
                tfPaterno.getText(),
                tfMaterno.getText(),
                direccion,
                tfTelefono.getText(),
                tfCorreo.getText()
        );*/

    
    }

    public boolean validarFormulario() {
        boolean esValido = true;
        resetearLabelErrors();

        if (tfCalle.getText() == null || tfCalle.getText().trim().isEmpty()) {
            lbErrorCalle.setText("La calle es obligatoria.");
            esValido = false;
        }

        if (tfColonia.getText() == null || tfColonia.getText().trim().isEmpty()) {
            lbErrorColonia.setText("La colonia es obligatoria.");
            esValido = false;
        }

        if (tfNumero.getText() == null || tfNumero.getText().trim().isEmpty()) {
            lbErrorNumero.setText("El número es obligatorio.");
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
            lbErrorCP.setText("El código postal es obligatorio.");
            esValido = false;
        } else if (tfCP.getText().length() != 5) {
            lbErrorCP.setText("El código postal debe tener 5 dígitos.");
            esValido = false;
        } else {
            try {
                Integer.parseInt(tfCP.getText());
            } catch (NumberFormatException e) {
                lbErrorCP.setText("El código postal debe ser numérico.");
                esValido = false;
            }
        }

        if (tfCiudad.getText() == null || tfCiudad.getText().trim().isEmpty()) {
            lbErrorCiudad.setText("La ciudad es obligatoria.");
            esValido = false;
        }

        if (tfEstado.getText() == null || tfEstado.getText().trim().isEmpty()) {
            lbErrorEstado.setText("El estado es obligatorio.");
            esValido = false;
        }

        if (tfNombre.getText() == null || tfNombre.getText().trim().isEmpty()) {
            lbErrorNombre.setText("El nombre es obligatorio.");
            esValido = false;
        }

        if (tfPaterno.getText() == null || tfPaterno.getText().trim().isEmpty()) {
            lbErrorPaterno.setText("El apellido paterno es obligatorio.");
            esValido = false;
        }

        if (tfMaterno.getText() == null || tfMaterno.getText().trim().isEmpty()) {
            lbErrorPaterno.setText("El apellido paterno es obligatorio.");
            esValido = false;
        }

        if (tfTelefono.getText() == null || tfTelefono.getText().trim().isEmpty()) {
            lbErrorTelefono.setText("El teléfono es obligatorio.");
            esValido = false;
        } else if (tfTelefono.getText().length() != 10) {
            lbErrorTelefono.setText("El teléfono debe tener 10 dígitos.");
            esValido = false;
        } else {
            try {
                Long.parseLong(tfTelefono.getText());
            } catch (NumberFormatException e) {
                lbErrorTelefono.setText("El teléfono debe ser numérico.");
                esValido = false;
            }
        }

        if (tfCorreo.getText() == null || tfCorreo.getText().trim().isEmpty()) {
            lbErrorCorreo.setText("El correo electrónico es obligatorio.");
            esValido = false;
        } else if (!tfCorreo.getText().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            lbErrorCorreo.setText("El correo electrónico no es válido.");
            esValido = false;
        }

        return esValido;
    }

    private void resetearLabelErrors() {
        lbErrorCalle.setText("");
        lbErrorColonia.setText("");
        lbErrorNumero.setText("");
        lbErrorCP.setText("");
        lbErrorCiudad.setText("");
        lbErrorEstado.setText("");
        lbErrorNombre.setText("");
        lbErrorPaterno.setText("");
        lbErrorMaterno.setText("");
        lbErrorTelefono.setText("");
        lbErrorCorreo.setText("");
    }

    @FXML
    private void botonPresionado(ActionEvent event) {
        if (validarFormulario()) {
            llenarPojo();
        }
    }

}
