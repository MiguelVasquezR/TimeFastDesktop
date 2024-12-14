/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import timefastdesktop.modelo.dao.ClienteDAO;
import timefastdesktop.observador.NotificadorOperacion;
import timefastdesktop.panes.FXMLUsuariosPaneController;
import timefastdesktop.pojo.Cliente;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.utilidades.Alertas;
import timefastdesktop.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author vasquez
 */
public class FXMLCardClienteController implements Initializable {

    @FXML
    private Label lbNombre;
    @FXML
    private Label lbApellidos;
    @FXML
    private Label lbDireccion;
    @FXML
    private Label lbTeléfono;
    @FXML
    private Label lbCorreo;

    private Cliente cliente;
    private NotificadorOperacion observador;
    private FXMLUsuariosPaneController controlerUsuariosPane;

    public void setPadreController(FXMLUsuariosPaneController controlerUsuariosPane) {
        this.controlerUsuariosPane = controlerUsuariosPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setClienteData(NotificadorOperacion notificacionOperacion, Cliente cliente) {
        this.observador = notificacionOperacion;
        this.cliente = cliente;
        this.cliente.setIdDireccion(cliente.getDireccion().getIdDireccion());
        this.cliente.setIdPersona(cliente.getPersona().getIdPersona());
        imprimirInformacion();
    }

    private void imprimirInformacion() {
        lbNombre.setText(this.cliente.getPersona().getNombre());
        String apellidos = this.cliente.getPersona().getApellidoPaterno();
        String apellidoMaterno = this.cliente.getPersona().getApellidoMaterno();
        if (apellidoMaterno != null && !apellidoMaterno.trim().isEmpty()) {
            apellidos += " " + apellidoMaterno;
        }
        lbApellidos.setText(apellidos);
        String direccion = String.format("%s #%s Col. %s",
                this.cliente.getDireccion().getCalle() != null ? this.cliente.getDireccion().getCalle() : "N/A",
                this.cliente.getDireccion().getNumero() != null ? this.cliente.getDireccion().getNumero() : "N/A",
                this.cliente.getDireccion().getColonia() != null ? this.cliente.getDireccion().getColonia() : "N/A");
        lbDireccion.setText(direccion);
        lbTeléfono.setText(this.cliente.getTelefono() != null && !this.cliente.getTelefono().isEmpty() ? this.cliente.getTelefono() : "No disponible");
        lbCorreo.setText(this.cliente.getPersona().getCorreo() != null && !this.cliente.getPersona().getCorreo().isEmpty()
                ? this.cliente.getPersona().getCorreo() : "No disponible");
    }

    @FXML
    private void btnEditar(ActionEvent event) {
        if (controlerUsuariosPane != null) {
            controlerUsuariosPane.obtenerClienteHijo(this.cliente, true);
        }
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
        Alert alerta = Alertas.mostrarAlertaSimple("Eliminar Cliente", "¿Estás seguro(a) de eliminar el cliente " + lbNombre.getText()
                + "?", Alert.AlertType.INFORMATION);
        Optional<ButtonType> respuesta = alerta.showAndWait();
        if (respuesta.get() != null && respuesta.get().getText().equals("Aceptar")) {
            Mensaje mensaje = ClienteDAO.eliminarCliente(this.cliente);
            if (mensaje.getError() == false) {
                Utilidades.mostrarAlertaSimple("Eliminar Cliente", "El cliente ha sido eliminado", Alert.AlertType.INFORMATION);
                this.observador.notificacionOperacion("Cliente Eliminado", "");
            } else {
                Utilidades.mostrarAlertaSimple("Eliminar Cliente", "No se ha podido eliminar el cliente, intentelo más tarde, por favor.", Alert.AlertType.ERROR);
            }
        }
    }

}
