package timefastdesktop.panes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import timefastdesktop.modelo.dao.PaqueteDAO;
import timefastdesktop.panes.card.FXMLCardPaquetesController;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.Paquete;
import timefastdesktop.utilidades.Utilidades;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import timefastdesktop.observador.NotificadorOperacion;
import timefastdesktop.pojo.Envio;

public class FXMLPaquetesPaneController implements Initializable, NotificadorOperacion {

    @FXML
    private ScrollPane scroll;
    @FXML
    private TextField tfBuscar;
    @FXML
    private FlowPane fpPaquetes;

    private List<Paquete> listaPaquetes = new ArrayList<>();
    @FXML
    private Label lbUsuario;
    @FXML
    private Button btnBuscar;
    @FXML
    private TextField tfAlto;
    @FXML
    private Label lbErrorAlto;
    @FXML
    private TextField tfAncho;
    @FXML
    private Label lbErrorAncho;
    @FXML
    private Label lbErrorEnvio;
    @FXML
    private TextField tfProfundidad;
    @FXML
    private Label lbErrorProfundidad;
    @FXML
    private TextField tfPeso;
    @FXML
    private Label lbErrorPeso;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private Label lbErrorDescripcion;
    @FXML
    private ComboBox<Envio> cbEnvios;
    @FXML
    private ScrollPane scrollClientes;
    @FXML
    private AnchorPane contenedorClientes;
    @FXML
    private ImageView ivPerfilCola;

    private Paquete paqueteEditar;
    private Boolean estaEditando = false;
    private Envio envioSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cargarPaquetes();
        cargarIdsEnvio();
        resetearLabelErrors();
        Utilidades.estilizarBarraScroll(scroll);
    }

    @Override
    public void notificacionOperacion(String titulo, String mensaje) {
        cargarPaquetes();
        Utilidades.mostrarAlertaSimple(titulo, mensaje, Alert.AlertType.INFORMATION);
    }

    private void resetearLabelErrors() {
        lbErrorAlto.setText("");
        lbErrorAncho.setText("");
        lbErrorEnvio.setText("");
        lbErrorProfundidad.setText("");
        lbErrorPeso.setText("");
        lbErrorDescripcion.setText("");
    }

    private boolean validarFormulario() {
        boolean esValido = true;
        resetearLabelErrors();

        if (tfAlto.getText().trim().isEmpty()) {
            lbErrorAlto.setText("El alto es obligatorio.");
            esValido = false;
        } else {
            try {
                Double.parseDouble(tfAlto.getText());
            } catch (NumberFormatException e) {
                lbErrorAlto.setText("El alto debe ser un valor numérico.");
                esValido = false;
            }
        }

        if (tfAncho.getText().trim().isEmpty()) {
            lbErrorAncho.setText("El ancho es obligatorio.");
            esValido = false;
        } else {
            try {
                Double.parseDouble(tfAncho.getText());
            } catch (NumberFormatException e) {
                lbErrorAncho.setText("El ancho debe ser un valor numérico.");
                esValido = false;
            }
        }

        if (tfProfundidad.getText().trim().isEmpty()) {
            lbErrorProfundidad.setText("La profundidad es obligatoria.");
            esValido = false;
        } else {
            try {
                Double.parseDouble(tfProfundidad.getText());
            } catch (NumberFormatException e) {
                lbErrorProfundidad.setText("La profundidad debe ser un valor numérico.");
                esValido = false;
            }
        }

        if (tfPeso.getText().trim().isEmpty()) {
            lbErrorPeso.setText("El peso es obligatorio.");
            esValido = false;
        } else {
            try {
                Double.parseDouble(tfPeso.getText());
            } catch (NumberFormatException e) {
                lbErrorPeso.setText("El peso debe ser un valor numérico.");
                esValido = false;
            }
        }

        if (taDescripcion.getText().trim().isEmpty()) {
            lbErrorDescripcion.setText("La descripción es obligatoria.");
            esValido = false;
        }

        if (cbEnvios.getValue() == null) {
            lbErrorEnvio.setText("Debe seleccionar un tipo de envío.");
            esValido = false;
        }

        return esValido;
    }

    public void cargarPaquetes() {
        listaPaquetes = PaqueteDAO.obtenerPaquetes();
        fpPaquetes.getChildren().clear();

        if (listaPaquetes != null) {
            if (listaPaquetes.isEmpty()) {
                Utilidades.mostrarAlertaSimple("Sin Paquetes", "No se encontraron paquetes registrados.", Alert.AlertType.INFORMATION);
            } else {
                llenarTarjetasPaquetes(listaPaquetes); // Llenar las tarjetas con la lista completa
            }
        } else {
            Utilidades.mostrarAlertaSimple("Sin paquetes", "Por el momento no se tiene registro de paquetes.", Alert.AlertType.ERROR);
        }
    }

    private void llenarTarjetasPaquetes(List<Paquete> paquetes) {
        try {
            for (Paquete paquete : paquetes) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/timefastdesktop/panes/card/FXMLCardPaquetes.fxml"));
                Parent tarjeta = loader.load();

                FXMLCardPaquetesController controller = loader.getController();
                controller.setPaqueteData(this, paquete);
                controller.setPadreController(this);

                fpPaquetes.getChildren().add(tarjeta);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar las tarjetas de paquetes.", Alert.AlertType.ERROR);
        }
    }

    private void buscarPaquete() {
        String textoBusqueda = tfBuscar.getText().trim();

        fpPaquetes.getChildren().clear();

        if (textoBusqueda.isEmpty()) {

            cargarPaquetes();
            return;
        }

        List<Paquete> paquetesFiltrados = listaPaquetes.stream()
                .filter(paquete -> paquete.getEnvio() != null && paquete.getEnvio().getNumGuia() != null
                && paquete.getEnvio().getNumGuia().toLowerCase().contains(textoBusqueda.toLowerCase()))
                .collect(Collectors.toList());

        if (paquetesFiltrados.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Sin Resultados", "No se encontraron paquetes con el número de guía ingresado.", Alert.AlertType.INFORMATION);
        } else {
            llenarTarjetasPaquetes(paquetesFiltrados);
        }
    }

    private void cargarIdsEnvio() {
        Mensaje mensaje = PaqueteDAO.obtenerTodosEnvio();
        if (!mensaje.getError()) {
            List<Envio> idsEnvio = (List<Envio>) mensaje.getObjeto();

            if (idsEnvio != null && !idsEnvio.isEmpty()) {
                cbEnvios.getItems().clear();
                ObservableList<Envio> idsEnvioObservableList = FXCollections.observableArrayList(idsEnvio);
                cbEnvios.setItems(idsEnvioObservableList);
            } else {
                Utilidades.mostrarAlertaSimple("Sin Envíos", "No se encontraron envíos disponibles.", Alert.AlertType.INFORMATION);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Sin envíos", "No se tiene registro de envíos", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    private void evtBuscarCliente(ActionEvent event) {
        buscarPaquete();
    }

    private void textoCambia(KeyEvent event) {
        String textoBusqueda = tfBuscar.getText().trim();

        fpPaquetes.getChildren().clear();

        if (textoBusqueda.isEmpty()) {
            cargarPaquetes();
        }
    }

    private String construirDimensiones(String alto, String ancho, String profundidad) {
        return String.format("%sx%sx%s", alto.trim(), ancho.trim(), profundidad.trim());
    }

    @FXML
    private void botonPresionado(ActionEvent event) {
        if (validarFormulario()) {
            Paquete paquete = new Paquete();

            paquete.setDescripcion(taDescripcion.getText());
            paquete.setDimensiones(construirDimensiones(tfAlto.getText(), tfAncho.getText(), tfProfundidad.getText()));
            paquete.setPeso(Double.parseDouble(tfPeso.getText()));
            paquete.setIdEnvio(this.envioSeleccionado.getIdEnvio());
            
            System.out.println(this.envioSeleccionado.getCosto());

            if (estaEditando) {
                paquete.setId(this.paqueteEditar.getIdPaquete());
                paquete.setIdPaquete(this.paqueteEditar.getIdPaquete());
                
                Mensaje msj = PaqueteDAO.editarPaquete(paquete);
                if (!msj.getError()) {
                    Utilidades.mostrarAlertaSimple("Paquete Actualizado", "El paquete ha sido actualizado exitosamente.", Alert.AlertType.INFORMATION);
                } else {
                    Utilidades.mostrarAlertaSimple("Error", "El paquete no se ha podido actualizar. Intente más tarde.", Alert.AlertType.ERROR);
                }
                estaEditando = false;
                btnGuardar.setText("Guardar");
            } else {
                Mensaje msj = PaqueteDAO.agregarPaquete(paquete);
                if (!msj.getError()) {
                    Utilidades.mostrarAlertaSimple("Paquete Registrado", "El paquete ha sido registrado exitosamente.", Alert.AlertType.INFORMATION);
                } else {
                    Utilidades.mostrarAlertaSimple("Error", "El paquete no se ha registrado. Intente más tarde.", Alert.AlertType.ERROR);
                }
            }

            limpiarCampos();
            cargarPaquetes();
        }
    }

    public void obtenerPaqueteHijo(Paquete paquete, Boolean estaEditando) {
        if (paquete != null) {
            this.estaEditando = estaEditando;
            this.paqueteEditar = paquete;
            llenarFormularioEditarPaquete(paquete);

            if (estaEditando) {
                btnGuardar.setText("Actualizar");
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error", "El paquete no existe o no se puede editar.", Alert.AlertType.ERROR);
        }
    }

    private void llenarFormularioEditarPaquete(Paquete paquete) {
        try {
            String[] dimensiones = paquete.getDimensiones().split("x");
            tfAlto.setText(dimensiones[0]);
            tfAncho.setText(dimensiones[1]);
            tfProfundidad.setText(dimensiones[2]);
        } catch (Exception e) {
            e.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "Las dimensiones del paquete no tienen el formato esperado.", Alert.AlertType.ERROR);
        }

        tfPeso.setText(String.valueOf(paquete.getPeso()));
        taDescripcion.setText(paquete.getDescripcion());
        cbEnvios.getSelectionModel().select(paquete.getEnvio());
        paquete.getEnvio().setIdEnvio(paquete.getIdEnvio());
        this.envioSeleccionado = paquete.getEnvio();
    }

    private void limpiarCampos() {
        tfAlto.clear();
        tfAncho.clear();
        tfProfundidad.clear();
        tfPeso.clear();
        taDescripcion.clear();
        cbEnvios.getSelectionModel().select(-1);
    }

    @FXML
    private void cbCambio(ActionEvent event) {
        this.envioSeleccionado = cbEnvios.getValue();
    }

    public void datosColaborador(String nombre, Image foto) {
        this.lbUsuario.setText(nombre);
        ivPerfilCola.setImage(foto);
    }

}
