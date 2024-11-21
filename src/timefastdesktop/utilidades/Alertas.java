package timefastdesktop.utilidades;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Alertas {

    public static Alert mostrarAlertaSimple(String title, String contentenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(title);
        alerta.setHeaderText(null);
        alerta.setContentText(contentenido);
        if (tipo == Alert.AlertType.CONFIRMATION) {
            ButtonType botonCancelar = new ButtonType("Cancelar");
            ButtonType botonAceptar = new ButtonType("Aceptar");
            alerta.getButtonTypes().setAll(botonCancelar, botonAceptar);
        }
        return alerta;
    }
}
