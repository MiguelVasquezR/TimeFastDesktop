package timefastdesktop;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import timefastdesktop.utilidades.Alertas;

public class FXMLPrincipalController implements Initializable {

    @FXML
    private ImageView itemColaboradores;
    @FXML
    private ImageView itemUnidades;
    @FXML
    private ImageView itemEnvios;
    @FXML
    private ImageView itemUsuarios;
    @FXML
    private ImageView itemPaquetes;
    @FXML
    private ImageView itemLogOut;
    @FXML
    private Rectangle selector;
    @FXML
    private Pane paneSustituto;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cambiarPane(obtenerNombrePane("itemColaboradores"));
    }

    @FXML
    private void seleccionado(MouseEvent event) {
        ImageView seleccionado = (ImageView) event.getSource();

        Map<String, String[]> iconos = new HashMap<>();
        iconos.put("itemColaboradores", new String[]{"recursos/iconsMenu/colaborador.png", "recursos/iconsMenu/colaboradorActivo.png"});
        iconos.put("itemUnidades", new String[]{"recursos/iconsMenu/unidad.png", "recursos/iconsMenu/unidadActivo.png"});
        iconos.put("itemEnvios", new String[]{"recursos/iconsMenu/envios.png", "recursos/iconsMenu/enviosActivo.png"});
        iconos.put("itemUsuarios", new String[]{"recursos/iconsMenu/usuarios.png", "recursos/iconsMenu/usuariosActivo.png"});
        iconos.put("itemPaquetes", new String[]{"recursos/iconsMenu/paquetes.png", "recursos/iconsMenu/paquetesActivo.png"});

        Map<String, Double> posiciones = new HashMap<>();
        posiciones.put("itemColaboradores", 115.0);
        posiciones.put("itemUnidades", 190.0);
        posiciones.put("itemEnvios", 270.0);
        posiciones.put("itemUsuarios", 350.0);
        posiciones.put("itemPaquetes", 430.0);

        for (Map.Entry<String, String[]> entrada : iconos.entrySet()) {
            ImageView item = obtenerItemID(entrada.getKey());
            if (item != null) {
                item.setImage(new Image(getClass().getResourceAsStream(entrada.getValue()[0])));

            }
        }

        String seleccionadoId = seleccionado.getId();
        if (iconos.containsKey(seleccionadoId)) {
            double nuevaPosicion = posiciones.get(seleccionadoId);
            animarSelector(nuevaPosicion);
            seleccionado.setImage(new Image(getClass().getResourceAsStream(iconos.get(seleccionadoId)[1])));
            cambiarPane(obtenerNombrePane(seleccionadoId));
        }
    }

    private void cambiarPane(String nombreFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("panes/" + nombreFXML));
            Parent nuevaVista = loader.load();
            paneSustituto.getChildren().clear();
            paneSustituto.getChildren().add(nuevaVista);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String obtenerNombrePane(String valor) {
        switch (valor) {
            case "itemColaboradores":
                return "FXMLColaboradorPane.fxml";
            case "itemEnvios":
                return "FXMLEnviosPane.fxml";
            case "itemUnidades":
                return "FXMLUnidadesPane.fxml";
            case "itemUsuarios":
                return "FXMLUsuariosPane.fxml";
            case "itemPaquetes":
                return "FXMLPaquetesPane.fxml";
            default:
                return "FXMLColaboradorPane.fxml";
        }
    }

    private ImageView obtenerItemID(String id) {
        switch (id) {
            case "itemColaboradores":
                return itemColaboradores;
            case "itemUnidades":
                return itemUnidades;
            case "itemEnvios":
                return itemEnvios;
            case "itemUsuarios":
                return itemUsuarios;
            case "itemPaquetes":
                return itemPaquetes;
            default:
                return null;
        }
    }

    private void animarSelector(double nuevaPosicion) {
        Duration duracion = Duration.millis(300);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        duracion,
                        new KeyValue(selector.layoutYProperty(), nuevaPosicion, Interpolator.EASE_OUT)
                )
        );
        timeline.play();
    }

    private void cerrarSesion() {
        try {
            Stage escenarioBase = (Stage) selector.getScene().getWindow();
            Parent principal = FXMLLoader.load(getClass().getResource("FXMLLogin.fxml"));
            Scene escenaPrincipal = new Scene(principal);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Inicio de Sesión");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnLogOut(MouseEvent event) {
        Alert alerta = Alertas.mostrarAlertaSimple("Cerrar Sesión", "¿Estás seguro de cerrar sesión?", Alert.AlertType.CONFIRMATION);
        Optional<ButtonType> respuesta = alerta.showAndWait();
        if (respuesta.get() != null && respuesta.get().getText().equals("Aceptar")) {
            cerrarSesion();
        }
    }

}
