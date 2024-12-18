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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import timefastdesktop.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author vasquez
 */
public class FXMLColaboradorPaneController implements Initializable {

    //Dar de alta rama

    @FXML
    private Label lbUsuario;
    @FXML
    private TextField txNombre;
    @FXML
    private TextField txPaterno;
    @FXML
    private TextField txMaterno;
    @FXML
    private TextField txCurp;
    @FXML
    private TextField txNumPersonal;
    @FXML
    private TextField psCorreo;
    @FXML
    private TextField txPassword;
    @FXML
    private TextField psPasswordConfirm;
    @FXML
    private ComboBox<?> cbRol;
    @FXML
    private CheckBox chConductor;
    @FXML
    private TextField txLicencia;
    @FXML
    private TextField tfBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private ScrollPane scroll;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilidades.estilizarBarraScroll(scroll);
    }    


    @FXML
    private void btnGuardarColaborador(ActionEvent event) {
    }
    
}
