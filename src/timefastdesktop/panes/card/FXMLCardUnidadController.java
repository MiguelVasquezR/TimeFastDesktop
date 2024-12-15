/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timefastdesktop.panes.card;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;
import timefastdesktop.modelo.dao.ClienteDAO;
import timefastdesktop.modelo.dao.UnidadDAO;
import timefastdesktop.observador.NotificadorOperacion;
import timefastdesktop.panes.FXMLUnidadesPaneController;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.Unidad;
import timefastdesktop.utilidades.Alertas;
import timefastdesktop.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author vasquez
 */
public class FXMLCardUnidadController implements Initializable {

    @FXML
    private Label lbMarca;
    @FXML
    private Label lbModelo;
    @FXML
    private Label lbAnio;
    @FXML
    private Label lbVIN;
    @FXML
    private Label lbTipo;
    @FXML
    private Label lbNII;
    @FXML
    private Label lbConductor;
    @FXML
    private ImageView ivImagen;
    
    private Unidad unidadCliente;
    private NotificadorOperacion observador;
    private FXMLUnidadesPaneController padre;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    public void obtenerReferenciaPadre(FXMLUnidadesPaneController padre){
        this.padre = padre;
    }

    public void llenarDatos(Unidad unidad, NotificadorOperacion observador) {
        lbMarca.setText(unidad.getMarca());
        lbModelo.setText(unidad.getModelo());
        lbAnio.setText(unidad.getAnio());
        lbVIN.setText(unidad.getVIN());
        lbTipo.setText(unidad.getTipo());
        lbNII.setText(unidad.getNumIdentificacion());
        ivImagen.setImage(Utilidades.convertirImagenDesdeBase64(unidad.getFoto()));
        if (unidad.getConductos() != null) {
            String nombre = unidad.getConductos().getPersona().getNombre() + " "
                    + unidad.getConductos().getPersona().getApellidoMaterno() + " "
                    + unidad.getConductos().getPersona().getApellidoPaterno();
            lbConductor.setText(nombre);
        } else {
            lbConductor.setText("Conductor no asignado");
        }
        
        this.unidadCliente = unidad;
        this.observador = observador;
    }


    @FXML
    private void btnEditar(ActionEvent event) {
        padre.llenarDatosFormularioEditar(this.unidadCliente);
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
        eliminarUnidad();
    }
    
    private void eliminarUnidad(){
       Alert alerta = Alertas.mostrarAlertaSimple("Eliminar Unidad", "¿Estás seguro(a) de eliminar la unidad?", Alert.AlertType.INFORMATION);
        Optional<ButtonType> respuesta = alerta.showAndWait();
        if (respuesta.get() != null && respuesta.get().getText().equals("Aceptar")) {
            Mensaje mensaje = UnidadDAO.eliminarUnidad(this.unidadCliente.getId());
            if (mensaje.getError() == false) {
                Utilidades.mostrarAlertaSimple("Eliminar Unidad", "La unidad ha sido eliminada", Alert.AlertType.INFORMATION);
                this.observador.notificacionOperacion("Cliente Eliminado", "");
            } else {
                Utilidades.mostrarAlertaSimple("Eliminar Unidad", "No se ha podido eliminar la unidad, intentelo más tarde, por favor.", Alert.AlertType.ERROR);
            }
        }
    }

}
