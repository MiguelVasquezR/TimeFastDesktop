package timefastdesktop.panes;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import timefastdesktop.modelo.dao.ClienteDAO;
import timefastdesktop.pojo.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
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
    @FXML
    private ImageView ivPerfilCola;

    private List<Cliente> listaClientes;
    private Boolean estaEditando = false;
    private List<Envio> listaEnvios;
    private Envio envioEditar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        llenarContenedorEnvios();
        esconderLabelsDeError();
        cargarClientes();
        Utilidades.estilizarBarraScroll(scrollEnvios);
        Utilidades.estilizarBarraScroll(scroll);
        tfNumGuia.setDisable(false);
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String first10Digits = uuid.substring(0, 22);
        tfNumGuia.setText(first10Digits);
    }

    private void cargarClientes() {
        listaClientes = ClienteDAO.obtenerClientes();
        if (listaClientes != null && !listaClientes.isEmpty()) {
            ObservableList<String> nombresClientes = FXCollections.observableArrayList();
            for (Cliente cliente : listaClientes) {
                Persona persona = cliente.getPersona();
                if (persona != null) {
                    String nombreCompleto = persona.getNombre() + " "
                            + persona.getApellidoPaterno() + " "
                            + persona.getApellidoMaterno();
                    nombresClientes.add(nombreCompleto);
                }
            }
            cbCliente.setItems(nombresClientes);

        } else {
            Utilidades.mostrarAlertaSimple("Sin Clientes",
                    "No se encontraron clientes disponibles.", Alert.AlertType.INFORMATION);
        }
    }

    private void obtenerDireccionClienteSeleccionado() {
        try {
            String nombre = cbCliente.getValue();
            if (nombre != null || !nombre.isEmpty()) {
                ObservableList<String> direccionClientes = FXCollections.observableArrayList();
                for (Cliente cliente : listaClientes) {
                    Persona persona = cliente.getPersona();
                    String nombreCompleto = persona.getNombre() + " "
                            + persona.getApellidoPaterno() + " "
                            + persona.getApellidoMaterno();

                    if (nombreCompleto.equals(nombre)) {
                        Direccion direccion = cliente.getDireccion();
                        String direccionString = direccion.getCalle() + " #" + direccion.getNumero() + " Col. "
                                + direccion.getColonia() + " C.P." + direccion.getCodigoPostal() + " "
                                + direccion.getCiudad() + ", " + direccion.getCiudad();
                        direccionClientes.add(direccionString);
                    }
                }
                cbOrigen.setItems(direccionClientes);
            }
        } catch (Exception e) {
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
                        Type tipoListaEnvios = new TypeToken<List<Envio>>() {
                        }.getType();
                        listaEnvios = gson.fromJson(jsonEnvios, tipoListaEnvios);

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
            Utilidades.mostrarAlertaSimple("Sin envío",
                    "No se tiene registros de envíos", Alert.AlertType.INFORMATION
            );
        }
    }

    private void llenarTarjetasEnvios(List<Envio> lista) {
        try {
            for (Envio envio : lista) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/timefastdesktop/panes/card/FXMLCardEnvios.fxml"));
                Parent tarjetaPresentacion = loader.load();
                FXMLCardEnviosController controller = loader.getController();
                controller.setEnvioData(this, envio, this); // Asignar el observador
                fpEnvios.getChildren().add(tarjetaPresentacion);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void notificacionOperacion(String titulo, String nombre) {
        Utilidades.mostrarAlertaSimple(titulo, nombre, Alert.AlertType.INFORMATION);
        llenarContenedorEnvios();
    }

    public void llenarFormularioEditar(Envio envio) {
        if (envio != null) {
            this.envioEditar = envio;
            Cliente cliente = envio.getCliente();
            if (cliente != null) {
                Persona persona = cliente.getPersona();
                if (persona != null) {
                    String nombreCompleto = persona.getNombre() + " "
                            + persona.getApellidoPaterno() + " "
                            + persona.getApellidoMaterno();
                    cbCliente.setValue(nombreCompleto);
                }
            }

            Direccion direccion = envio.getDestino();
            if (direccion != null) {
                tfCalle.setText(direccion.getCalle());
                tfColonia.setText(direccion.getColonia());
                tfNumero.setText(direccion.getNumero());
                tfCP.setText(direccion.getCodigoPostal());
                tfCiudad.setText(direccion.getCiudad());
                tfEstado.setText(direccion.getEstado());
            }

            tfNumGuia.setText(envio.getNumGuia());

            tfPrecio.setText(String.valueOf(envio.getCosto()));

            estaEditando = true;
            obtenerDireccionClienteSeleccionado();
            cbOrigen.getSelectionModel().select(2);
        } else {
            Utilidades.mostrarAlertaSimple("Error", "No se ha seleccionado un envío válido para editar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void botonPresionado(ActionEvent event) {
        if (validarFormulario()) {
            Envio envio = construirEnvio();
            if (envio != null) {

                if (estaEditando) {
                    envio.setIdEnvio(this.envioEditar.getIdEnvio());
                    envio.setIdCliente(this.envioEditar.getCliente().getId());
                    envio.setIdOrigen(this.envioEditar.getOrigen().getIdDireccion());
                    envio.getDestino().setIdDireccion(this.envioEditar.getDestino().getIdDireccion());
                    envio.setIdDestino(this.envioEditar.getDestino().getIdDireccion());
                    envio.setOrigen(this.envioEditar.getOrigen());
                    Mensaje msjWS = EnviosDAO.actualizarEnvio(envio);
                    if (msjWS.getError() == false) {
                        Utilidades.mostrarAlertaSimple("Actualizado correctamente", "Se ha actualizado correctamente el envío", Alert.AlertType.INFORMATION);
                        llenarContenedorEnvios();
                        limpiarCampos();
                    } else {
                        Utilidades.mostrarAlertaSimple("Error al actualizar el envio", "Por el momento no es posible actualizar el envío, intentelo más tarde.", Alert.AlertType.ERROR);
                    }

                } else {
                    Mensaje msjWS = EnviosDAO.agregarEnvio(envio);
                    if (msjWS.getError() == false) {
                        Utilidades.mostrarAlertaSimple("Almacenado correctamente", "Se ha guardado correctamente el envío", Alert.AlertType.INFORMATION);
                        llenarContenedorEnvios();
                        limpiarCampos();
                    } else {
                        Utilidades.mostrarAlertaSimple("Error al agregar el envio", "Por el momento no es posible agregar el envío, intentelo más tarde.", Alert.AlertType.ERROR);
                    }
                }

            }
        }
    }

// Método para validar los campos del formulario
    private boolean validarFormulario() {
        boolean valido = true;
        esconderLabelsDeError();

        if (tfCalle.getText().isEmpty()) {
            lbErrorCalle.setText("La calle es obligatoria.");
            lbErrorCalle.setVisible(true);
            valido = false;
        }

        if (tfColonia.getText().isEmpty()) {
            lbErrorColonia.setText("La colonia es obligatoria.");
            lbErrorColonia.setVisible(true);
            valido = false;
        }

        if (tfNumero.getText().isEmpty()) {
            lbErrorNumero.setText("El número es obligatorio.");
            lbErrorNumero.setVisible(true);
            valido = false;
        } else if (!Utilidades.esNumero((tfNumero.getText()))) {
            lbErrorNumero.setText("Debe ser un número.");
            lbErrorNumero.setVisible(true);
            valido = false;
        }

        if (tfCP.getText().isEmpty()) {
            lbErrorCP.setText("El código postal es obligatorio.");
            lbErrorCP.setVisible(true);
            valido = false;
        }

        if (tfCiudad.getText().isEmpty()) {
            lbErrorCiudad.setText("La ciudad es obligatoria.");
            lbErrorCiudad.setVisible(true);
            valido = false;
        }

        if (tfEstado.getText().isEmpty()) {
            lbErrorEstado.setText("El estado es obligatorio.");
            lbErrorEstado.setVisible(true);
            valido = false;
        }

        if (cbCliente.getValue() == null || cbCliente.getValue().isEmpty()) {
            lbErrorCliente.setText("Debe seleccionar un cliente.");
            lbErrorCliente.setVisible(true);
            valido = false;
        }

        if (cbOrigen.getValue() == null || cbOrigen.getValue().isEmpty()) {
            lbErrorOrigen.setText("Debe seleccionar una dirección de origen.");
            lbErrorOrigen.setVisible(true);
            valido = false;
        }

        if (tfNumGuia.getText().isEmpty()) {
            lbErrorNumGuia.setText("El número de guía es obligatorio.");
            lbErrorNumGuia.setVisible(true);
            valido = false;
        }

        if (tfPrecio.getText().isEmpty()) {
            lbErrorPrecio.setText("El precio es obligatorio.");
            lbErrorPrecio.setVisible(true);
            valido = false;
        } else if (!Utilidades.esNumero((tfPrecio.getText()))) {
            lbErrorPrecio.setText("Debe ser un número.");
            lbErrorPrecio.setVisible(true);
            valido = false;
        }

        return valido;
    }

    private Envio construirEnvio() {
        try {
            Cliente cliente = obtenerClienteSeleccionado();
            if (cliente == null) {
                Utilidades.mostrarAlertaSimple("Error", "Debe seleccionar un cliente válido.", Alert.AlertType.WARNING);
                return null;
            }

            Direccion direccion = new Direccion();
            direccion.setCalle(tfCalle.getText());
            direccion.setColonia(tfColonia.getText());
            direccion.setNumero(tfNumero.getText());
            direccion.setCodigoPostal(tfCP.getText());
            direccion.setCiudad(tfCiudad.getText());
            direccion.setEstado(tfEstado.getText());

            Envio envio = new Envio();
            envio.setIdCliente(obtenerClienteSeleccionado().getId());
            envio.setCliente(cliente);
            envio.setDestino(direccion);
            envio.setNumGuia(tfNumGuia.getText());
            envio.setCosto(Double.parseDouble(tfPrecio.getText()));

            return envio;
        } catch (NumberFormatException e) {
            Utilidades.mostrarAlertaSimple("Error", "El precio debe ser un número válido.", Alert.AlertType.ERROR);
            return null;
        }
    }

    private Cliente obtenerClienteSeleccionado() {
        String clienteSeleccionado = cbCliente.getValue();
        if (clienteSeleccionado != null && !clienteSeleccionado.isEmpty()) {
            for (Cliente cliente : listaClientes) {
                Persona persona = cliente.getPersona();
                if (persona != null) {
                    String nombreCompleto = persona.getNombre() + " "
                            + persona.getApellidoPaterno() + " "
                            + persona.getApellidoMaterno();
                    if (nombreCompleto.equals(clienteSeleccionado)) {
                        return cliente;
                    }
                }
            }
        }
        return null;
    }

    private void btnSeleccionarCliente(ActionEvent event) {
        Cliente cliente = obtenerClienteSeleccionado();
        if (cliente != null) {
            Utilidades.mostrarAlertaSimple("Cliente Seleccionado",
                    "ID: " + cliente.getId() + "\nNombre: " + cliente.getPersona().getNombre()
                    + "\nTeléfono: " + cliente.getTelefono(), Alert.AlertType.INFORMATION);
        } else {
            Utilidades.mostrarAlertaSimple("Error",
                    "Debe seleccionar un cliente válido.", Alert.AlertType.WARNING);
        }
    }

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

    @FXML
    private void seleccionaDireccion(ActionEvent event) {
        obtenerDireccionClienteSeleccionado();
    }

    //Buscar
    private void buscarNumGuia(String numGuia) {
        if (!numGuia.isEmpty()) {
            List<Envio> filtrado = new ArrayList<>();
            for (Envio envio : listaEnvios) {
                if (envio.getNumGuia().contains(numGuia)) {
                    filtrado.add(envio);
                }
            }
            fpEnvios.getChildren().clear();
            llenarTarjetasEnvios(filtrado);

        }
    }

    @FXML
    private void textoCambia(KeyEvent event) {
        String textoBusqueda = tfBuscar.getText().trim();
        fpEnvios.getChildren().clear();
        if (textoBusqueda.isEmpty()) {
            llenarContenedorEnvios();
        } else {
            buscarNumGuia(tfBuscar.getText());
        }
    }

    @FXML
    private void buscarEnvio(ActionEvent event) {
        if (!tfBuscar.getText().isEmpty()) {
            buscarNumGuia(tfBuscar.getText());
        }
    }

    public void datosColaborador(String nombreColaborador, Image fotoPerfil) {
        lbUsuario.setText(nombreColaborador);
        ivPerfilCola.setImage(fotoPerfil);
    }

    private void limpiarCampos() {
        tfCalle.clear();
        tfColonia.clear();
        tfNumero.clear();
        tfCP.clear();
        tfCiudad.clear();
        tfEstado.clear();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String first10Digits = uuid.substring(0, 22);
        tfNumGuia.setText(first10Digits);
        tfPrecio.clear();
        cbOrigen.getSelectionModel().clearSelection();
        cbCliente.getSelectionModel().clearSelection();
        estaEditando = false;
    }

}
