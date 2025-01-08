package timefastdesktop.panes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URL;
import java.util.Map;
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
import javafx.scene.control.TextArea;
import timefastdesktop.modelo.dao.EnviosDAO;
import timefastdesktop.observador.NotificadorOperacion;
import timefastdesktop.pojo.Direccion;
import timefastdesktop.pojo.Envio;
import timefastdesktop.pojo.EstadoEnvio;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.utilidades.Utilidades;

public class EnvioEstadoController implements Initializable {

    @FXML
    private ComboBox<String> cbEstado;

    private Envio envio;
    ObservableList<String> estados;
    @FXML
    private TextArea taDescripcion;
    
    private NotificadorOperacion observador;


    public void setObservador(NotificadorOperacion observador) {
        this.observador = observador;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.estados = FXCollections.observableArrayList(
                "Pendiente",
                "En transito",
                "Detenido",
                "Entregado",
                "Cancelado"
        );
        cbEstado.setItems(estados);

    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
        if (envio != null && envio.getDestino() != null) {
            cbEstado.setValue(envio.getDestino().getEstado());
            Mensaje msj = EnviosDAO.obtenerEstadosEnvios(envio.getIdEnvio());
            Gson gson = new Gson();
            if (msj.getError() == false) {
                try {
                    JsonObject json = gson.fromJson(msj.getObjeto().toString(), JsonObject.class);
                    EstadoEnvio ee = gson.fromJson(json.get("value"), EstadoEnvio.class);
                    int i = 0;
                    for (String estado : estados) {

                        if (estado.equals(ee.getEstado())) {
                            cbEstado.getSelectionModel().select(i);
                            return;
                        }
                        i++;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
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
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo actualizar el estado del envío: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

        @FXML
       private void btnAceptar(ActionEvent event) {
           if (taDescripcion.getText().isEmpty() || taDescripcion == null) {
               Utilidades.mostrarAlertaSimple("Completa los campos", "Se necesita una descripción para cambiar el estado", Alert.AlertType.WARNING);
               return;
           }

           String nuevoEstado = cbEstado.getSelectionModel().getSelectedItem();
           EstadoEnvio ee = new EstadoEnvio();
           ee.setEstado(nuevoEstado);
           ee.setEnvio(this.envio);
           ee.setIdEnvio(this.envio.getIdEnvio());
           ee.setDescripcion(taDescripcion.getText());
           Mensaje mensaje = EnviosDAO.actualizarEstado(ee);
           if (mensaje.getError() == false) {
               // Notificar al observador
               if (observador != null) {
                   observador.notificacionOperacion("Estado actualizado", "El estado del envío ha cambiado.");
               }
               cerrarVentana();
           } else {
               Utilidades.mostrarAlertaSimple("Error al actualizar", "Por el momento no es posible actualizar el estado, intentelo más tarde.", Alert.AlertType.WARNING);
           }
       }


    @FXML
    private void btnCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) cbEstado.getScene().getWindow();
        stage.close();
    }

}
