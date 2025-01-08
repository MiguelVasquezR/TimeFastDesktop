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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import timefastdesktop.modelo.dao.ColaboradorDAO;
import timefastdesktop.modelo.dao.UnidadDAO;
import timefastdesktop.observador.NotificadorOperacion;
import timefastdesktop.panes.FXMLColaboradorPaneController;
import timefastdesktop.panes.FXMLUsuariosPaneController;
import timefastdesktop.pojo.Colaborador;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.utilidades.Alertas;
import timefastdesktop.utilidades.Utilidades;

public class FXMLCardColaboradorController implements Initializable {

    @FXML
    private ImageView ivPerfil;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbCurp;
    @FXML
    private Label lbNumPersonal;
    @FXML
    private Label lbCorreo;
    @FXML
    private Label lbRol;

    private Colaborador colaborador;
    private NotificadorOperacion observador;
    private FXMLColaboradorPaneController controladorColaboradorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

    public void setPadreController(FXMLColaboradorPaneController controladorColaboradorPane) {
        this.controladorColaboradorPane = controladorColaboradorPane;
    }

    public void setClienteData(NotificadorOperacion notificacionOperacion, Colaborador colaborador) {
        this.observador = notificacionOperacion;
        this.colaborador = colaborador;
        this.colaborador.setIdPersona(colaborador.getPersona().getIdPersona());
        imprimirInformacion();
    }

    private void imprimirInformacion() {
        lbNombre.setText(this.colaborador.getPersona().getNombre() + " "+ this.colaborador.getPersona().getApellidoPaterno() + " "+ this.colaborador.getPersona().getApellidoMaterno());
        lbCurp.setText(this.colaborador.getPersona().getCURP());
        lbCorreo.setText(this.colaborador.getPersona().getCorreo());
        lbNumPersonal.setText(this.colaborador.getNoPersonal());
        ivPerfil.setImage(Utilidades.convertirImagenDesdeBase64(this.colaborador.getPersona().getFotoBase64()));
        String rolTexto = this.colaborador.getRol().getRol();
        String numLicencia = this.colaborador.getRol().getNumLicencia();
        if ("Conductor".equals(rolTexto) && numLicencia != null && !numLicencia.isEmpty()) {
            lbRol.setText(rolTexto + " Número de licencia: " + numLicencia);
        } else {
            lbRol.setText(rolTexto);
        }
    }

    @FXML
    private void btnEditar(ActionEvent event) {
        controladorColaboradorPane.llenarDatosFormularioEditar(this.colaborador);
    }

    @FXML
    private void btnEliminar(ActionEvent event) {
        Alert alerta = Alertas.mostrarAlertaSimple("Eliminar Colaborador", "¿Estás seguro(a) de eliminar al colaborador?", Alert.AlertType.INFORMATION);
        Optional<ButtonType> respuesta = alerta.showAndWait();
        if (respuesta.get() != null && respuesta.get().getText().equals("Aceptar")) {
            Mensaje mensaje = ColaboradorDAO.eliminarColaborador(this.colaborador);
            if (mensaje.getError() == false) {
                Utilidades.mostrarAlertaSimple("Eliminar Colaborador", "El colaborador ha sido eliminado", Alert.AlertType.INFORMATION);
                this.observador.notificacionOperacion("Colaborador Eliminado", "");
            } else {
                Utilidades.mostrarAlertaSimple("Eliminar Colaborador", "No se ha podido eliminar el Colaborador, intentelo más tarde, por favor.", Alert.AlertType.ERROR);
            }
        }
    }
    
}
