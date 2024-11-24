package timefastdesktop.panes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private ComboBox<?> cbOrigen;
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
    }

   

}
