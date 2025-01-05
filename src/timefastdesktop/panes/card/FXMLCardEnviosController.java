package timefastdesktop.panes.card;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import timefastdesktop.modelo.dao.EnviosDAO;
import timefastdesktop.observador.NotificadorOperacion;
import timefastdesktop.panes.EnvioEstadoController;
import timefastdesktop.pojo.Envio;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.utilidades.Alertas;
import timefastdesktop.utilidades.SesionUsuario;
import timefastdesktop.utilidades.Utilidades;


public class FXMLCardEnviosController implements Initializable {

    @FXML
    private Label lbNombreCliente;
    @FXML
    private Label lbCalle;
    @FXML
    private Label lbNumero;
    @FXML
    private Label lbDestino;
    @FXML
    private Label lbCiudad;
    @FXML
    private Label lbEstadoPaquete;
    @FXML
    private Label lbNombreConductor;
    @FXML
    private Label lbCodigoPostal;
    @FXML
    private Label lbEstado;
    @FXML
    private Label lbColonia;

    private Envio envio;
    private NotificadorOperacion observador;

    public void setEnvioData(NotificadorOperacion notificacionOperacion, Envio envio) {
        this.observador = notificacionOperacion;
        this.envio = envio;
        imprimirInformacion();
    }

    private void imprimirInformacion() {
        lbNombreCliente.setText(envio.getCliente() != null && envio.getCliente().getPersona() != null
                ? envio.getCliente().getPersona().getNombre()
                : "No disponible");

        lbCalle.setText(envio.getDestino() != null && envio.getDestino().getCalle() != null
                ? envio.getDestino().getCalle()
                : "N/A");

        lbNumero.setText(envio.getDestino() != null && envio.getDestino().getNumero() != null
                ? envio.getDestino().getNumero()
                : "N/A");

        lbDestino.setText(envio.getDestino() != null && envio.getDestino().getColonia() != null
                ? envio.getDestino().getColonia()
                : "N/A");

        lbCiudad.setText(envio.getDestino() != null && envio.getDestino().getCiudad() != null
                ? envio.getDestino().getCiudad()
                : "N/A");

        lbCodigoPostal.setText(envio.getDestino() != null && envio.getDestino().getCodigoPostal() != null
                ? envio.getDestino().getCodigoPostal()
                : "N/A");

        lbEstado.setText(envio.getDestino() != null && envio.getDestino().getEstado() != null
                ? envio.getDestino().getEstado()
                : "N/A");

        lbEstadoPaquete.setText("Pendiente");
        lbNombreConductor.setText(envio.getConductor() != null && envio.getConductor().getPersona() != null
                ? envio.getConductor().getPersona().getNombre()
                : "No asignado");
    }


    @FXML
    private void btnEditar(ActionEvent event) {

    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No initialization needed
    }

    @FXML
    private void btnEstado(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/timefastdesktop/panes/EnvioEstado.fxml"));
            Parent root = loader.load();

            EnvioEstadoController controller = loader.getController();
            controller.setEnvio(envio); 

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Estado del Envío");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void btnAsignarConductor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/timefastdesktop/panes/card/FXMLAsignarConductor.fxml"));
            Parent root = loader.load();

            FXMLAsignarConductorController controller = loader.getController();

            controller.setEnvio(envio);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Asignar Conductor");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            
            if (controller.isConductorAsignado()) {
                imprimirInformacion(); 
            }

        } catch (IOException e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudo cargar la ventana de asignación de conductor.", Alert.AlertType.ERROR);
        }
    }



}