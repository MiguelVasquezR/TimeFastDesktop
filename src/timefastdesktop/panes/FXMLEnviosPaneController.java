package timefastdesktop.panes;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import timefastdesktop.modelo.dao.EnviosDAO;
import timefastdesktop.panes.card.FXMLCardEnviosController;
import timefastdesktop.pojo.Direccion;
import timefastdesktop.pojo.Envio;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.utilidades.Utilidades;
import timefastdesktop.observador.NotificadorOperacion;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Map;

import timefastdesktop.modelo.dao.ClienteDAO;
import timefastdesktop.pojo.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import timefastdesktop.pojo.Persona;



public class FXMLEnviosPaneController implements Initializable, NotificadorOperacion {

    @FXML
    private ScrollPane scroll;
    @FXML
    private Label lbUsuario;
    @FXML
    private Button btnBuscar;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TextField tfCalle;
    @FXML
    private Label lbErrorCalle;
    @FXML
    private TextField tfColonia;
    @FXML
    private Label lbErrorColonia;
    @FXML
    private TextField tfNumero;
    @FXML
    private Label lbErrorNumero;
    @FXML
    private TextField tfCP;
    @FXML
    private Label lbErrorCP;
    @FXML
    private TextField tfCiudad;
    @FXML
    private Label lbErrorCiudad;
    @FXML
    private TextField tfEstado;
    @FXML
    private Label lbErrorEstado;
    @FXML
    private Label lbErrorCliente;
    @FXML
    private ComboBox<String> cbOrigen;
    @FXML
    private Label lbErrorOrigen;
    @FXML
    private TextField tfNumGuia;
    @FXML
    private Label lbErrorNumGuia;
    @FXML
    private TextField tfPrecio;
    @FXML
    private Label lbErrorPrecio;
    @FXML
    private ScrollPane scrollEnvios;
    @FXML
    private AnchorPane contenedorEnvios;
    @FXML
    private FlowPane fpEnvios;

    @FXML
    private ComboBox<String> cbCliente; 

    private List<Cliente> listaClientes;

@Override
public void initialize(URL url, ResourceBundle rb) {
    Utilidades.estilizarBarraScroll(scrollEnvios);
    llenarContenedorEnvios();
    esconderLabelsDeError();
    cargarClientes();
    cargarDireccionesOrigen(); 
}


    
    private void cargarClientes() {
        listaClientes = ClienteDAO.obtenerClientes();

        if (listaClientes != null && !listaClientes.isEmpty()) {
            ObservableList<String> nombresClientes = FXCollections.observableArrayList();
            for (Cliente cliente : listaClientes) {
                Persona persona = cliente.getPersona();
                if (persona != null) {
                    String nombreCompleto = persona.getNombre() + " " + 
                                            persona.getApellidoPaterno() + " " + 
                                            persona.getApellidoMaterno();
                    nombresClientes.add(nombreCompleto);
                }
            }
            cbCliente.setItems(nombresClientes); 
        } else {
            Utilidades.mostrarAlertaSimple("Sin Clientes", 
                    "No se encontraron clientes disponibles.", Alert.AlertType.INFORMATION);
        }
    }


private void esconderLabelsDeError() {
    lbErrorCalle.setVisible(false);
    lbErrorColonia.setVisible(false);
    lbErrorNumero.setVisible(false);
    lbErrorCP.setVisible(false);
    lbErrorCiudad.setVisible(false);
    lbErrorEstado.setVisible(false);
    lbErrorCliente.setVisible(false);
    lbErrorOrigen.setVisible(false);
    lbErrorNumGuia.setVisible(false);
    lbErrorPrecio.setVisible(false);
}
    
    private void llenarContenedorEnvios() {
    Mensaje respuesta = EnviosDAO.obtenerTodosLosEnvios();

    fpEnvios.getChildren().clear();

    if (respuesta != null && !respuesta.getError()) {
        try {
            if (respuesta.getObjeto() instanceof Map) {
                Map<String, Object> objeto = (Map<String, Object>) respuesta.getObjeto();

                if (objeto.containsKey("value") && objeto.get("value") instanceof String) {
                    String jsonEnvios = (String) objeto.get("value");

                   
                    Gson gson = new Gson();
                    Type tipoListaEnvios = new TypeToken<List<Envio>>() {}.getType();
                    List<Envio> listaEnvios = gson.fromJson(jsonEnvios, tipoListaEnvios);

                    if (listaEnvios != null && !listaEnvios.isEmpty()) {
                        // Filtra y valida los datos antes de mostrarlos
                        listaEnvios.removeIf(envio -> envio.getCliente() == null || envio.getDestino() == null);
                        if (listaEnvios.isEmpty()) {
                            Utilidades.mostrarAlertaSimple("No hay registros válidos",
                                    "Se encontraron envíos, pero los datos están incompletos.", Alert.AlertType.WARNING);
                        } else {
                            llenarTarjetasEnvios(listaEnvios);
                        }
                    } else {
                        Utilidades.mostrarAlertaSimple("No hay registros",
                                "Por el momento no se tienen registros de envíos.", Alert.AlertType.INFORMATION);
                    }
                } else {
                    Utilidades.mostrarAlertaSimple("Datos inesperados",
                            "El campo 'value' no tiene el formato esperado.", Alert.AlertType.ERROR);
                }
            } else {
                Utilidades.mostrarAlertaSimple("Datos inesperados",
                        "El objeto recibido no tiene la estructura esperada.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            Utilidades.mostrarAlertaSimple("Error al procesar los datos",
                    "Hubo un problema al interpretar los datos de envíos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    } else {
        String mensajeError = (respuesta != null) ? respuesta.getMensaje() : "Error desconocido.";
        Utilidades.mostrarAlertaSimple("Problemas al obtener envíos",
                "Por el momento no se pueden obtener los envíos. Detalles: " + mensajeError, Alert.AlertType.ERROR);
    }
}

    private void llenarTarjetasEnvios(List<Envio> lista) {
        try {
            for (Envio envio : lista) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/timefastdesktop/panes/card/FXMLCardEnvios.fxml"));
                Parent tarjetaPresentacion = loader.load();
                FXMLCardEnviosController controller = loader.getController();
                controller.setEnvioData(this, envio);
                fpEnvios.getChildren().add(tarjetaPresentacion);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void notificacionOperacion(String titulo, String nombre) {
        llenarContenedorEnvios();
    }
    
    @FXML
    private void botonPresionado(ActionEvent event) {
        //if (validarFormulario()) {
          //  llenarPojo();
        //}
    }
    
private Cliente obtenerClienteSeleccionado() {
    String clienteSeleccionado = cbCliente.getValue();
    if (clienteSeleccionado != null && !clienteSeleccionado.isEmpty()) {
        for (Cliente cliente : listaClientes) {
            Persona persona = cliente.getPersona();
            if (persona != null) {
                String nombreCompleto = persona.getNombre() + " " + 
                                        persona.getApellidoPaterno() + " " + 
                                        persona.getApellidoMaterno();
                if (nombreCompleto.equals(clienteSeleccionado)) {
                    return cliente; 
                }
            }
        }
    }
    return null; 
}

    
@FXML
private void btnSeleccionarCliente(ActionEvent event) {
    Cliente cliente = obtenerClienteSeleccionado();
    if (cliente != null) {
        Utilidades.mostrarAlertaSimple("Cliente Seleccionado", 
                "ID: " + cliente.getId() + "\nNombre: " + cliente.getPersona().getNombre() +
                "\nTeléfono: " + cliente.getTelefono(), Alert.AlertType.INFORMATION);
    } else {
        Utilidades.mostrarAlertaSimple("Error", 
                "Debe seleccionar un cliente válido.", Alert.AlertType.WARNING);
    }
}


private void cargarDireccionesOrigen() {
    Mensaje respuesta = EnviosDAO.obtenerDireccionesOrigen();

    if (respuesta != null && !respuesta.getError()) {
        List<Direccion> listaDirecciones = (List<Direccion>) respuesta.getObjeto();

        if (listaDirecciones != null && !listaDirecciones.isEmpty()) {
            ObservableList<String> idsDirecciones = FXCollections.observableArrayList();

            for (Direccion direccion : listaDirecciones) {
                idsDirecciones.add(String.valueOf(direccion.getIdDireccion())); // Agregar solo el ID como String
            }

            cbOrigen.setItems(idsDirecciones); // Establecer los IDs en el ComboBox
        } else {
            Utilidades.mostrarAlertaSimple("Sin Direcciones", 
                    "No se encontraron direcciones de origen disponibles.", Alert.AlertType.INFORMATION);
        }
    } else {
        String mensajeError = (respuesta != null) ? respuesta.getMensaje() : "Error desconocido.";
        Utilidades.mostrarAlertaSimple("Error al Obtener Direcciones", 
                "No fue posible cargar las direcciones de origen. Detalles: " + mensajeError, Alert.AlertType.ERROR);
    }
}


    @FXML
    private void seleccionarDireccionOrigen(ActionEvent event) {
        String direccionSeleccionada = cbOrigen.getValue();
        if (direccionSeleccionada != null && !direccionSeleccionada.isEmpty()) {
            Utilidades.mostrarAlertaSimple("Dirección Seleccionada", 
                    "Has seleccionado la dirección: " + direccionSeleccionada, Alert.AlertType.INFORMATION);
        } else {
            Utilidades.mostrarAlertaSimple("Error", 
                    "Por favor selecciona una dirección válida.", Alert.AlertType.WARNING);
        }
    }


}