/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timefastdesktop.panes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilidades.estilizarBarraScroll(scroll);
    }    
    
}
