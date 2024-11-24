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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import timefastdesktop.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author vasquez
 */
public class FXMLUnidadesPaneController implements Initializable {

    @FXML
    private Label lbUsuario;
    @FXML
    private TextField tfBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private ImageView viUnidad;
    @FXML
    private ComboBox<?> cbTipoUnidad;
    @FXML
    private TextField tfMarca;
    @FXML
    private TextField tfModelo;
    @FXML
    private TextField tfAno;
    @FXML
    private TextField tfVin;
    @FXML
    private TextField tfNII;
    @FXML
    private Button btnGuardar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
