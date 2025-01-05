package timefastdesktop.panes.card;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import timefastdesktop.modelo.dao.PaqueteDAO;
import timefastdesktop.observador.NotificadorOperacion;
import timefastdesktop.panes.FXMLPaquetesPaneController;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.Paquete;
import timefastdesktop.utilidades.Alertas;
import timefastdesktop.utilidades.Utilidades;
import timefastdesktop.observador.NotificadorOperacion;

public class FXMLCardPaquetesController implements Initializable {

    @FXML
    private Label lbGuia;
    @FXML
    private Label lbDimensiones;
    @FXML
    private Label lbPeso;
    @FXML
    private Label lbDescripcion;

    private Paquete paquete;
    private NotificadorOperacion observador;
    private FXMLPaquetesPaneController controllerPaquetesPane;

    public void setPadreController(FXMLPaquetesPaneController controllerPaquetesPane) {
        this.controllerPaquetesPane = controllerPaquetesPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setPaqueteData(NotificadorOperacion notificacionOperacion, Paquete paquete) {
        this.observador = notificacionOperacion;
        this.paquete = paquete;
        imprimirInformacion();
    }

    private void imprimirInformacion() {
        lbGuia.setText(
            this.paquete.getEnvio() != null && this.paquete.getEnvio().getNumGuia() != null
                ? String.valueOf(this.paquete.getEnvio().getNumGuia()) 
                : "N/A"
        );
        lbDimensiones.setText(this.paquete.getDimensiones() != null ? this.paquete.getDimensiones() : "N/A");
        lbPeso.setText(this.paquete.getPeso() != null ? String.valueOf(this.paquete.getPeso()) + " kg" : "N/A");
        lbDescripcion.setText(this.paquete.getDescripcion() != null ? this.paquete.getDescripcion() : "N/A");
    }
        
    @FXML
    private void btnEditar(ActionEvent event) {
        if (controllerPaquetesPane != null) {

            controllerPaquetesPane.obtenerPaqueteHijo(this.paquete, true);
            System.out.println("Entro");
        }
        
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
            Alert alerta = Alertas.mostrarAlertaSimple("Eliminar Paquete", "¿Estás seguro(a) de eliminar el paquete con guía " + lbGuia.getText()
                    + "?", Alert.AlertType.CONFIRMATION);
            Optional<ButtonType> respuesta = alerta.showAndWait();
            if (respuesta.isPresent() && respuesta.get().getText().equals("Aceptar")) {
                Mensaje mensaje = PaqueteDAO.eliminarPaquete(this.paquete.getIdPaquete());
                if (!mensaje.getError()) {
                    if (observador != null) {
                        observador.notificacionOperacion("Paquete Eliminado", "El paquete con guía " + lbGuia.getText() + " ha sido eliminado correctamente.");
                    }
                } else {
                    Utilidades.mostrarAlertaSimple("Eliminar Paquete", "No se ha podido eliminar el paquete, inténtelo más tarde, por favor.", Alert.AlertType.ERROR);
                }
            }
        }


}
