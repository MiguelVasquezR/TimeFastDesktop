/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timefastdesktop.panes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import timefastdesktop.modelo.dao.ClienteDAO;
import timefastdesktop.pojo.Cliente;
import timefastdesktop.pojo.Direccion;
import timefastdesktop.pojo.Mensaje;
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
    @FXML
    private TextField tfCliente;
    @FXML
    private Label lbErrorCliente;
    @FXML
    private Label lbDatosDireccionCliente;

    private Cliente datosCliente;
    @FXML
    private ComboBox<?> cbEnvios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilidades.estilizarBarraScroll(scroll);
        vaciarLabels();
    }
    
    public void vaciarLabels() {
    lbUsuario.setText("");
    lbErrorAlto.setText("");
    lbErrorAncho.setText("");
    lbErrorEnvio.setText("");
    lbErrorProfundidad.setText("");
    lbErrorPeso.setText("");
    lbErrorDescripcion.setText("");
    lbErrorCliente.setText("");
    lbDatosDireccionCliente.setText("");
}


    //Búsqueda de cliente
    private Cliente buscarClienteNombre(String nombre) {
        Mensaje msj = ClienteDAO.buscarClienteNombre(nombre);
        Integer idCliente = null;
        if (!msj.getError()) {
            Gson gson = new Gson();
            try {
                Object objeto = msj.getObjeto();
                JsonObject jsonObject = gson.fromJson(objeto.toString(), JsonObject.class);
                JsonObject valueJsonObject = jsonObject.getAsJsonObject("value");
                return gson.fromJson(valueJsonObject, Cliente.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                Utilidades.mostrarAlertaSimple("Error de Formato", "El formato de los datos es incorrecto.", Alert.AlertType.ERROR);
            } catch (ClassCastException | NullPointerException e) {
                Utilidades.mostrarAlertaSimple("Error Interno", "El objeto devuelto no tiene el formato esperado.", Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error Obtener Cliente", msj.getMensaje(), Alert.AlertType.ERROR);
            vaciarLabels();
        }
        return null;
    }

    private void llenarCampoDatosCliente() {
        String direccion = String.format("%s #%s Col. %s, C.P. %s, %s, %s",
                this.datosCliente.getDireccion().getCalle() != null ? this.datosCliente.getDireccion().getCalle() : "N/A",
                this.datosCliente.getDireccion().getNumero() != null ? this.datosCliente.getDireccion().getNumero() : "N/A",
                this.datosCliente.getDireccion().getColonia() != null ? this.datosCliente.getDireccion().getColonia() : "N/A",
                this.datosCliente.getDireccion().getCodigoPostal() != null ? this.datosCliente.getDireccion().getCodigoPostal() : "N/A",
                this.datosCliente.getDireccion().getCiudad() != null ? this.datosCliente.getDireccion().getCiudad() : "N/A",
                this.datosCliente.getDireccion().getEstado() != null ? this.datosCliente.getDireccion().getEstado() : "N/A");
        lbDatosDireccionCliente.setText(direccion);
    }
    
    private void llenarComboBox(){
        
    }

    @FXML
    private void buscarCliente(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (!tfCliente.getText().isEmpty() || !tfCliente.getText().equals("")) {
                this.datosCliente = buscarClienteNombre(tfCliente.getText());
                if (this.datosCliente != null) {
                    llenarCampoDatosCliente();
                }
            }
        }
    }

}
