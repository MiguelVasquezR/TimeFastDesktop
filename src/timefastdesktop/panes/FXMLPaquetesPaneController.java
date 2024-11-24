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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import timefastdesktop.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author vasquez
 */
public class FXMLPaquetesPaneController implements Initializable {

    @FXML
    private Label lbUsuario;
    @FXML
    private TextField tfBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private ScrollPane scroll;
    @FXML
    private TextField tfAlto;
    @FXML
    private Label lbErrorAlto;
    @FXML
    private TextField tfAncho;
    @FXML
    private Label lbErrorAncho;
    @FXML
    private TextField tfEnvio;
    @FXML
    private Label lbErrorEnvio;
    @FXML
    private TextField tfProfundidad;
    @FXML
    private Label lbErrorProfundidad;
    @FXML
    private TextField tfPeso;
    @FXML
    private Label lbErrorPeso;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private Label lbErrorDescripcion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilidades.estilizarBarraScroll(scroll);
    }    
    
}
