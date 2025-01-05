package timefastdesktop.panes.card;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import timefastdesktop.modelo.dao.EnviosDAO;
import timefastdesktop.pojo.Colaborador;
import timefastdesktop.pojo.Envio;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.utilidades.Utilidades;

public class FXMLAsignarConductorController implements Initializable {

    @FXML
    private ComboBox<String> cbConductores;

    private Envio envio;
    private boolean conductorAsignado = false; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarConductores();
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    public boolean isConductorAsignado() {
        return conductorAsignado;
    }

@FXML
private void btnAceptar(ActionEvent event) {
    String conductorSeleccionado = cbConductores.getValue();

    if (conductorSeleccionado != null && !conductorSeleccionado.isEmpty()) {
        int idConductor = obtenerIdConductor(conductorSeleccionado);

        if (idConductor > 0) {
            Mensaje mensaje = EnviosDAO.asignarConductor(envio.getIdEnvio(), idConductor);
            if (!mensaje.getError()) {
                Colaborador conductor = obtenerConductorPorId(idConductor);
                envio.setConductor(conductor);

                Utilidades.mostrarAlertaSimple("Éxito", "Conductor asignado correctamente.", Alert.AlertType.INFORMATION);
                conductorAsignado = true;
                cerrarVentana();
            } else {
                Utilidades.mostrarAlertaSimple("Error", mensaje.getMensaje(), Alert.AlertType.ERROR);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error", "ID del conductor seleccionado no válido.", Alert.AlertType.ERROR);
        }
    } else {
        Utilidades.mostrarAlertaSimple("Advertencia", "Debe seleccionar un conductor.", Alert.AlertType.WARNING);
    }
}


private int obtenerIdConductor(String conductorSeleccionado) {
    Mensaje mensaje = EnviosDAO.obtenerConductores();
    if (!mensaje.getError()) {
        List<Colaborador> listaConductores = (List<Colaborador>) mensaje.getObjeto();
        for (Colaborador conductor : listaConductores) {
            String nombreCompleto = conductor.getPersona().getNombre() + " " +
                                    conductor.getPersona().getApellidoPaterno() + " " +
                                    conductor.getPersona().getApellidoMaterno();
            if (nombreCompleto.equals(conductorSeleccionado)) {
                return conductor.getIdColaborador(); 
            }
        }
    }
    return -1; 
}

private Colaborador obtenerConductorPorId(int idConductor) {
    Mensaje mensaje = EnviosDAO.obtenerConductores();
    if (!mensaje.getError()) {
        List<Colaborador> listaConductores = (List<Colaborador>) mensaje.getObjeto();
        for (Colaborador conductor : listaConductores) {
            if (conductor.getIdColaborador().equals(idConductor)) {
                return conductor;
            }
        }
    }
    return null; // Retorna null si no encuentra el conductor
}



    private void cerrarVentana() {
        // Obtener la ventana actual y cerrarla
        Stage stage = (Stage) cbConductores.getScene().getWindow();
        stage.close();
    }

    private void cargarConductores() {
        Mensaje mensaje = EnviosDAO.obtenerConductores();

        if (!mensaje.getError()) {
            List<Colaborador> listaConductores = (List<Colaborador>) mensaje.getObjeto();

            if (listaConductores != null && !listaConductores.isEmpty()) {
                ObservableList<String> nombresConductores = FXCollections.observableArrayList();
                for (Colaborador conductor : listaConductores) {
                    nombresConductores.add(conductor.getPersona().getNombre() + " " +
                                           conductor.getPersona().getApellidoPaterno() + " " +
                                           conductor.getPersona().getApellidoMaterno());
                }
                cbConductores.setItems(nombresConductores);
            } else {
                Utilidades.mostrarAlertaSimple("Sin Conductores", "No se encontraron conductores disponibles.", Alert.AlertType.INFORMATION);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron obtener los conductores. Intente más tarde.", Alert.AlertType.ERROR);
        }
    }
}

