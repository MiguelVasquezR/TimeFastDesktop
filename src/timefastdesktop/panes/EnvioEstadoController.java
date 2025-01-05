
package timefastdesktop.panes;

import com.google.gson.Gson;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import timefastdesktop.modelo.dao.EnviosDAO;
import timefastdesktop.pojo.Direccion;
import timefastdesktop.pojo.Envio;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.utilidades.Utilidades;

public class EnvioEstadoController implements Initializable {

    @FXML
    private ComboBox<String> cbEstado;

    private Envio envio; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ObservableList<String> estados = FXCollections.observableArrayList(
            "Pendiente",
            "En transito",
            "Detenido",
            "Entregado",
            "Cancelado"
        );

        cbEstado.setItems(estados);


        cbEstado.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && envio != null) {
                actualizarEstadoEnvio(newValue);
            }
        });
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
        if (envio != null && envio.getDestino() != null) {
            cbEstado.setValue(envio.getDestino().getEstado()); 
        }
    }

private void actualizarEstadoEnvio(String nuevoEstado) {
    try {

        Direccion direccionClonada = new Direccion(envio.getDestino());
        direccionClonada.setEstado(nuevoEstado);

        envio.setDestino(direccionClonada);

        Gson gson = new Gson();
        String jsonEstado = gson.toJson(envio);

        Mensaje respuesta = EnviosDAO.actualizarEstadoEnvio(jsonEstado);

        if (respuesta != null && !respuesta.getError()) {
            Utilidades.mostrarAlertaSimple("Actualización exitosa",
                    "El estado del envío se actualizó a: " + nuevoEstado, Alert.AlertType.INFORMATION);
        } else {
            Utilidades.mostrarAlertaSimple("Error al actualizar",
                    respuesta != null ? respuesta.getMensaje() : "Error desconocido.", Alert.AlertType.ERROR);
        }
    } catch (Exception e) {
        Utilidades.mostrarAlertaSimple("Error", "No se pudo actualizar el estado del envío: " + e.getMessage(), Alert.AlertType.ERROR);
    }
}

    @FXML
    private void btnAceptar(ActionEvent event) {
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
    }

}
