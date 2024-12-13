package timefastdesktop.panes;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import timefastdesktop.modelo.dao.ClienteDAO;
import timefastdesktop.panes.card.FXMLCardClienteController;
import timefastdesktop.pojo.Cliente;
import timefastdesktop.pojo.Direccion;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.Persona;
import timefastdesktop.utilidades.Utilidades;
import timefastdesktop.observador.NotificadorOperacion;

public class FXMLUsuariosPaneController implements Initializable, NotificadorOperacion {

    @FXML
    private Label lbUsuario;
    @FXML
    private TextField tfBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private ScrollPane scroll;
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
    private TextField tfNombre;
    @FXML
    private Label lbErrorNombre;
    @FXML
    private TextField tfPaterno;
    @FXML
    private Label lbErrorPaterno;
    @FXML
    private Label lbErrorMaterno;
    @FXML
    private TextField tfTelefono;
    @FXML
    private Label lbErrorTelefono;
    @FXML
    private TextField tfCorreo;
    @FXML
    private Label lbErrorCorreo;
    @FXML
    private TextField tfMaterno;
    @FXML
    private AnchorPane contenedorClientes;

    private List<Cliente> listaCliente = new ArrayList<Cliente>();

    @FXML
    private ScrollPane scrollClientes;
    @FXML
    private FlowPane fpClientes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetearLabelErrors();
        llenarContenedorClientes();
        Utilidades.estilizarBarraScroll(scroll);
        Utilidades.estilizarBarraScroll(scrollClientes);
    }

    public void llenarPojo() {

        Direccion direccion = new Direccion();
        direccion.setCalle(tfCalle.getText());
        direccion.setColonia(tfColonia.getText());
        direccion.setNumero(tfNumero.getText());
        direccion.setCodigoPostal(tfCP.getText());
        direccion.setCiudad(tfCiudad.getText());
        direccion.setEstado(tfEstado.getText());

        Persona persona = new Persona();
        persona.setNombre(tfNombre.getText());
        persona.setApellidoPaterno(tfPaterno.getText());
        persona.setApellidoMaterno(tfMaterno.getText());
        persona.setCorreo(tfCorreo.getText());

        Cliente cliente = new Cliente();
        cliente.setPersona(persona);
        cliente.setDireccion(direccion);
        cliente.setTelefono(tfTelefono.getText());

        Mensaje msj = ClienteDAO.agregarCliente(cliente);

        if (msj.getError() == false) {
            Utilidades.mostrarAlertaSimple("Cliente Registrado!", "El cliente " + persona.getNombre() + " ha sido registrado exitosamente", Alert.AlertType.INFORMATION);
            limpiarCampos();
            llenarContenedorClientes();
        } else {
            Utilidades.mostrarAlertaSimple("Cliente No Registrado!", "El cliente " + persona.getNombre() + " no ha sido registrado, intentelo más tarde", Alert.AlertType.ERROR);
        }

    }

    public boolean validarFormulario() {
        boolean esValido = true;
        resetearLabelErrors();

        if (tfCalle.getText() == null || tfCalle.getText().trim().isEmpty()) {
            lbErrorCalle.setText("La calle es obligatoria.");
            esValido = false;
        }

        if (tfColonia.getText() == null || tfColonia.getText().trim().isEmpty()) {
            lbErrorColonia.setText("La colonia es obligatoria.");
            esValido = false;
        }

        if (tfNumero.getText() == null || tfNumero.getText().trim().isEmpty()) {
            lbErrorNumero.setText("El número es obligatorio.");
            esValido = false;
        } else {
            try {
                Integer.parseInt(tfNumero.getText());
            } catch (NumberFormatException e) {
                lbErrorNumero.setText("El número debe ser un valor numérico.");
                esValido = false;
            }
        }

        if (tfCP.getText() == null || tfCP.getText().trim().isEmpty()) {
            lbErrorCP.setText("El código postal es obligatorio.");
            esValido = false;
        } else if (tfCP.getText().length() != 5) {
            lbErrorCP.setText("El código postal debe tener 5 dígitos.");
            esValido = false;
        } else {
            try {
                Integer.parseInt(tfCP.getText());
            } catch (NumberFormatException e) {
                lbErrorCP.setText("El código postal debe ser numérico.");
                esValido = false;
            }
        }

        if (tfCiudad.getText() == null || tfCiudad.getText().trim().isEmpty()) {
            lbErrorCiudad.setText("La ciudad es obligatoria.");
            esValido = false;
        }

        if (tfEstado.getText() == null || tfEstado.getText().trim().isEmpty()) {
            lbErrorEstado.setText("El estado es obligatorio.");
            esValido = false;
        }

        if (tfNombre.getText() == null || tfNombre.getText().trim().isEmpty()) {
            lbErrorNombre.setText("El nombre es obligatorio.");
            esValido = false;
        }

        if (tfPaterno.getText() == null || tfPaterno.getText().trim().isEmpty()) {
            lbErrorPaterno.setText("El apellido paterno es obligatorio.");
            esValido = false;
        }

        if (tfMaterno.getText() == null || tfMaterno.getText().trim().isEmpty()) {
            lbErrorPaterno.setText("El apellido paterno es obligatorio.");
            esValido = false;
        }

        if (tfTelefono.getText() == null || tfTelefono.getText().trim().isEmpty()) {
            lbErrorTelefono.setText("El teléfono es obligatorio.");
            esValido = false;
        } else if (tfTelefono.getText().length() != 10) {
            lbErrorTelefono.setText("El teléfono debe tener 10 dígitos.");
            esValido = false;
        } else {
            try {
                Long.parseLong(tfTelefono.getText());
            } catch (NumberFormatException e) {
                lbErrorTelefono.setText("El teléfono debe ser numérico.");
                esValido = false;
            }
        }

        if (tfCorreo.getText() == null || tfCorreo.getText().trim().isEmpty()) {
            lbErrorCorreo.setText("El correo electrónico es obligatorio.");
            esValido = false;
        } else if (!tfCorreo.getText().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            lbErrorCorreo.setText("El correo electrónico no es válido.");
            esValido = false;
        }

        return esValido;
    }

    private void resetearLabelErrors() {
        lbErrorCalle.setText("");
        lbErrorColonia.setText("");
        lbErrorNumero.setText("");
        lbErrorCP.setText("");
        lbErrorCiudad.setText("");
        lbErrorEstado.setText("");
        lbErrorNombre.setText("");
        lbErrorPaterno.setText("");
        lbErrorMaterno.setText("");
        lbErrorTelefono.setText("");
        lbErrorCorreo.setText("");
    }

    private void limpiarCampos() {
        tfBuscar.clear();
        tfCalle.clear();
        tfColonia.clear();
        tfNumero.clear();
        tfCP.clear();
        tfCiudad.clear();
        tfEstado.clear();
        tfNombre.clear();
        tfPaterno.clear();
        tfMaterno.clear();
        tfTelefono.clear();
        tfCorreo.clear();
    }

    @FXML
    private void botonPresionado(ActionEvent event) {
        if (validarFormulario()) {
            llenarPojo();
        }
    }

    //Cargar clientes en el pane
    private void llenarContenedorClientes() {
        listaCliente = ClienteDAO.obtenerClientes();
        fpClientes.getChildren().clear();
        if (listaCliente != null) {
            if (listaCliente.size() == 0) {
                Utilidades.mostrarAlertaSimple("No hay registro", "Por el momento no se tiene registros de clientes", Alert.AlertType.INFORMATION);
            } else {
                llenarTarjetasClientes();
            }
        } else {
            Utilidades.mostrarAlertaSimple("Problemas al obtener clientes",
                    "Por el momento no se pueden obtener los clientes, intente más tarde, por favor.", Alert.AlertType.INFORMATION);
        }
    }

    private void llenarTarjetasClientes() {
        try {
            for (Cliente cliente : listaCliente) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/timefastdesktop/panes/card/FXMLCardCliente.fxml"));
                Parent tarjetaPresentacion = loader.load();
                FXMLCardClienteController controller = loader.getController();
                controller.setClienteData(this, cliente);
                fpClientes.getChildren().add(tarjetaPresentacion);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void notificacionOperacion(String titulo, String nombre) {
        llenarContenedorClientes();
    }

}
