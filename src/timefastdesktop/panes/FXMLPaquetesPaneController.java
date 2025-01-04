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
import javafx.scene.layout.AnchorPane;

public class FXMLPaquetesPaneController implements Initializable {

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
    private TextField tfCliente;
    @FXML
    private Label lbErrorCliente;
    @FXML
    private ComboBox<String> cbEnvios;
    @FXML
    private ScrollPane scrollClientes;
    @FXML
    private AnchorPane contenedorClientes;

    private Paquete paqueteEditar;
    private Boolean estaEditando = false;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilidades.estilizarBarraScroll(scroll);
        cargarPaquetes();
        cargarIdsEnvio();
        resetearLabelErrors();
    }
    
    private void resetearLabelErrors() {
        lbErrorAlto.setText("");
        lbErrorAncho.setText("");
        lbErrorEnvio.setText("");
        lbErrorProfundidad.setText("");
        lbErrorPeso.setText("");
        lbErrorDescripcion.setText("");
        lbErrorCliente.setText("");
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

        if (tfCliente.getText().trim().isEmpty()) {
            lbErrorCliente.setText("El cliente es obligatorio.");
            esValido = false;
        }

        if (cbEnvios.getValue() == null) {
            lbErrorEnvio.setText("Debe seleccionar un tipo de envío.");
            esValido = false;
        }

        return esValido;
    }

    private void cargarPaquetes() {
        listaPaquetes = PaqueteDAO.obtenerPaquetes();
        fpPaquetes.getChildren().clear();

        if (listaPaquetes != null) {
            if (listaPaquetes.isEmpty()) {
                Utilidades.mostrarAlertaSimple("Sin Paquetes", "No se encontraron paquetes registrados.", Alert.AlertType.INFORMATION);
            } else {
                llenarTarjetasPaquetes(listaPaquetes);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron obtener los paquetes. Intente más tarde.", Alert.AlertType.ERROR);
        }
    }

    private void llenarTarjetasPaquetes(List<Paquete> paquetes) {
        try {
            for (Paquete paquete : paquetes) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/timefastdesktop/panes/card/FXMLCardPaquetes.fxml"));
                Parent tarjeta = loader.load();

                FXMLCardPaquetesController controller = loader.getController();
                controller.setPaqueteData(null, paquete); 

                fpPaquetes.getChildren().add(tarjeta);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron cargar las tarjetas de paquetes.", Alert.AlertType.ERROR);
        }
    }

    private void buscarPaquete() {
        String textoBusqueda = tfBuscar.getText().trim();

        if (textoBusqueda.isEmpty()) {
            cargarPaquetes(); 
            return;
        }

        List<Paquete> paquetesFiltrados = new ArrayList<>();
        for (Paquete paquete : listaPaquetes) {
            if (paquete.getDescripcion() != null && paquete.getDescripcion().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                paquetesFiltrados.add(paquete);
            }
        }

        if (paquetesFiltrados.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Sin Resultados", "No se encontraron paquetes que coincidan con la búsqueda.", Alert.AlertType.INFORMATION);
        } else {
            llenarTarjetasPaquetes(paquetesFiltrados);
        }
    }

    private void cargarIdsEnvio() {
        Mensaje mensaje = PaqueteDAO.obtenerTodosLosIdEnvio();

        if (!mensaje.getError()) {
            List<Integer> idsEnvio = (List<Integer>) mensaje.getObjeto();

            if (idsEnvio != null && !idsEnvio.isEmpty()) {
                cbEnvios.getItems().clear();
                ObservableList<String> idsEnvioObservableList = FXCollections.observableArrayList(
                        idsEnvio.stream()
                                .map(String::valueOf)
                                .collect(Collectors.toList())
                );
                cbEnvios.setItems(idsEnvioObservableList);  
            } else {
                Utilidades.mostrarAlertaSimple("Sin IDs de Envío", "No se encontraron IDs de envío disponibles.", Alert.AlertType.INFORMATION);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se pudieron obtener los IDs de envío. Intente más tarde.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void evtBuscarCliente(ActionEvent event) {
        buscarPaquete();
    }

@FXML
private void botonPresionado(ActionEvent event) {
    if (validarFormulario()) {
        Paquete paquete = new Paquete();

        paquete.setDescripcion(taDescripcion.getText());
        paquete.setDimensiones(tfAlto.getText() + "x" + tfAncho.getText() + "x" + tfProfundidad.getText());
        paquete.setPeso(Double.parseDouble(tfPeso.getText()));
        paquete.setIdEnvio(Integer.parseInt(cbEnvios.getValue()));

        if (estaEditando) {
            paquete.setId(paqueteEditar.getId()); 
            Mensaje msj = PaqueteDAO.editarPaquete(paquete);
            if (msj.getError() == false) {
                Utilidades.mostrarAlertaSimple("Paquete Actualizado", "El paquete ha sido actualizado exitosamente.", Alert.AlertType.INFORMATION);
            } else {
                Utilidades.mostrarAlertaSimple("Error", "El paquete no se ha podido actualizar. Intente más tarde.", Alert.AlertType.ERROR);
            }
            this.estaEditando = false;
            btnGuardar.setText("Guardar"); 
        } else {
            Mensaje msj = PaqueteDAO.agregarPaquete(paquete);
            if (msj.getError() == false) {
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
        this.estaEditando = estaEditando;
        this.paqueteEditar = paquete;

        
        if (paquete != null) {
            System.out.println("entro");
            llenarFormularioEditarPaquete(paquete); 

            
            if (this.estaEditando) {
                btnGuardar.setText("Actualizar");  
            }
        } else {
            System.out.println("Paquete es nulo. No se puede editar.");
        }
    }

        private void llenarFormularioEditarPaquete(Paquete paquete) {
            tfAlto.setText(paquete.getDimensiones().split("x")[0]);  
            tfAncho.setText(paquete.getDimensiones().split("x")[1]);
            tfProfundidad.setText(paquete.getDimensiones().split("x")[2]);
            tfPeso.setText(String.valueOf(paquete.getPeso()));
            taDescripcion.setText(paquete.getDescripcion());
            //cbEnvios.getSelectionModel().select(String.valueOf(paquete.getIdEnvio()));  
        }


    private void limpiarCampos() {
        tfAlto.clear();
        tfAncho.clear();
        tfProfundidad.clear();
        tfPeso.clear();
        taDescripcion.clear();
        tfCliente.clear();
        cbEnvios.getSelectionModel().clearSelection();
    }

}
