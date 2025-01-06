package timefastdesktop.panes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import timefastdesktop.modelo.dao.ColaboradorDAO;
import timefastdesktop.observador.NotificadorOperacion;
import timefastdesktop.panes.card.FXMLCardColaboradorController;
import timefastdesktop.pojo.Colaborador;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.Persona;
import timefastdesktop.pojo.RolColaborador;
import timefastdesktop.utilidades.Utilidades;

public class FXMLColaboradorPaneController implements Initializable, NotificadorOperacion {

    @FXML
    private Label lbUsuario;
    @FXML
    private TextField tfBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private ScrollPane scroll;
    @FXML
    private ImageView ivPerfil;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfPaterno;
    @FXML
    private TextField tfMaterno;
    @FXML
    private TextField tfCurp;
    @FXML
    private TextField tfCorreo;
    @FXML
    private TextField psPassword;
    @FXML
    private TextField pfConfirmarPassword;
    @FXML
    private TextField tfNumLicencia;
    @FXML
    private ComboBox<String> cbRol;
    @FXML
    private Button btn;
    @FXML
    private Label lbNumeroLicencia;
    @FXML
    private Label lbFotoError;
    @FXML
    private Label lbErrorNombre;
    @FXML
    private Label lbErrorPaterno;
    @FXML
    private Label lbErrorMaterno;
    @FXML
    private Label lbErrorCurp;
    @FXML
    private Label lbErrorNumPersonal;
    @FXML
    private Label lbErrorCorreo;
    @FXML
    private Label lbErrorContrasena;
    @FXML
    private Label lbErrorConfirmar;
    @FXML
    private Label lbErrorRol;
    @FXML
    private Label lbErrorLicencia;
    @FXML
    private TextField tfNumeroColaborador;

    private Boolean estaEditando = false;
    private File imagenSeleccionada = null;
    @FXML
    private ScrollPane scrollColaborador;
    @FXML
    private AnchorPane contenedorColaborador;
    @FXML
    private FlowPane fpColaborador;

    private List<Colaborador> listaColaborador = null;
    private Colaborador colaboradorEditar = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        llenarComboBox();

        llenarContenedorColaboradores();
        btn.setLayoutY(829);
        Utilidades.estilizarBarraScroll(scroll);

        obtenerNuevaContrasena();
    }

    private void obtenerNuevaContrasena() {
        String password = generatePassword(16);
        psPassword.setText(password);
        pfConfirmarPassword.setText(password);

    }

    public static String generatePassword(int length) {

        String validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(validCharacters.length());
            password.append(validCharacters.charAt(index));
        }
        return password.toString();
    }

    private void llenarComboBox() {
        String[] tipos = {"Administrador", "Ejecutivo de tienda", "Conductor"};
        cbRol.getItems().addAll(tipos);
    }

    private void validarConductor() {
        if (cbRol.getSelectionModel().getSelectedItem().equals("Conductor") && cbRol != null) {
            tfNumLicencia.setVisible(true);
            lbNumeroLicencia.setVisible(true);
            btn.setLayoutY(910);
        } else {
            tfNumLicencia.setVisible(false);
            lbNumeroLicencia.setVisible(false);
            btn.setLayoutY(829);
        }
    }

    private boolean validarCampos() {
        boolean esValido = true;

        if (imagenSeleccionada == null) {
            lbFotoError.setText("Debe seleccionar una imagen");
            lbFotoError.setStyle("-fx-text-fill: red;");
            esValido = false;
        } else {
            lbFotoError.setText("Seleccionar Fotografía");
            lbFotoError.setStyle("-fx-text-fill: white;");
        }

        if (tfNombre.getText().trim().isEmpty()) {
            lbErrorNombre.setText("El nombre es obligatorio.");
            esValido = false;
        } else {
            lbErrorNombre.setText("");
        }

        if (tfPaterno.getText().trim().isEmpty()) {
            lbErrorPaterno.setText("El apellido paterno es obligatorio.");
            esValido = false;
        } else {
            lbErrorPaterno.setText("");
        }

        if (tfMaterno.getText().trim().isEmpty()) {
            lbErrorMaterno.setText("El apellido materno es obligatorio.");
            esValido = false;
        } else {
            lbErrorMaterno.setText("");
        }

        if (tfCurp.getText().trim().isEmpty()) {
            lbErrorCurp.setText("La CURP es obligatoria.");
            esValido = false;
        } else {
            lbErrorCurp.setText("");
        }

        if (tfNumeroColaborador.getText().trim().isEmpty()) {
            lbErrorNumPersonal.setText("El correo es obligatorio.");
            esValido = false;
        } else {
            lbErrorNumPersonal.setText("");
        }

        if (tfCorreo.getText().trim().isEmpty()) {
            lbErrorCorreo.setText("El correo es obligatorio.");
            esValido = false;
        } else {
            lbErrorCorreo.setText("");
        }

        if (cbRol.getSelectionModel().getSelectedItem() == null) {
            lbErrorRol.setText("Debe seleccionar un rol.");
            esValido = false;
        } else {
            lbErrorRol.setText("");
        }

        if ("Conductor".equals(cbRol.getSelectionModel().getSelectedItem()) && tfNumLicencia.getText().trim().isEmpty()) {
            lbErrorLicencia.setText("El número de licencia es obligatorio para conductores.");
            esValido = false;
        } else {
            lbErrorLicencia.setText("");
        }

        return esValido;
    }

    private Colaborador generarColaboradorDesdeFormulario() {
        Persona persona = new Persona();
        persona.setNombre(tfNombre.getText().trim());
        persona.setApellidoPaterno(tfPaterno.getText().trim());
        persona.setApellidoMaterno(tfMaterno.getText().trim());
        persona.setCorreo(tfCorreo.getText().trim());
        persona.setCURP(tfCurp.getText().trim());

        RolColaborador rolColaborador = new RolColaborador();
        String rolSeleccionado = cbRol.getSelectionModel().getSelectedItem();
        if (rolSeleccionado != null) {
            rolColaborador.setRol(rolSeleccionado);
        }
        if ("Conductor".equals(rolSeleccionado)) {
            rolColaborador.setNumLicencia(tfNumLicencia.getText().trim());
        }
        Colaborador colaborador = new Colaborador();
        colaborador.setNoPersonal(tfNumeroColaborador.getText());
        colaborador.setPersona(persona);
        colaborador.setContrasena(psPassword.getText().trim());
        colaborador.setRol(rolColaborador);

        return colaborador;
    }

    private Mensaje actualizarFotoColaborador(Boolean estaAgregando, Integer id) {
        Mensaje msj = new Mensaje();
        if (imagenSeleccionada != null) {
            try {
                byte[] byteArray = Utilidades.archivoAByte(imagenSeleccionada);
                if (estaAgregando) {
                    Integer idWS = Integer.parseInt(ColaboradorDAO.obtenerUltimoID());
                    if (idWS != null || idWS > 0) {
                        msj = ColaboradorDAO.actualizarFoto(idWS, byteArray);
                    } else {
                        msj.setError(true);
                        msj.setMensaje("No ha sido posible actualizar la foto");
                    }
                } else {
                    msj = ColaboradorDAO.actualizarFoto(id, byteArray);
                }
            } catch (Exception e) {
                e.printStackTrace();
                msj.setError(true);
                msj.setMensaje("No ha sido posible actualizar la foto.");
            }
        } else {
            msj.setError(true);
            msj.setMensaje("No se tiene seleccionada una imagen, seleccione una e intente de nuevo.");
        }
        return msj;

    }

    private void vaciarCampos() {
        tfNombre.clear();
        tfPaterno.clear();
        tfMaterno.clear();
        tfCurp.clear();
        tfNumeroColaborador.clear();
        tfCorreo.clear();
        psPassword.clear();
        pfConfirmarPassword.clear();
        tfNumLicencia.clear();
        cbRol.getSelectionModel().select(-1);
        File file = new File("src/timefastdesktop/recursos/colaboradorPerfil.png");
        Image image = new Image(file.toURI().toString());
        ivPerfil.setImage(image);

        obtenerNuevaContrasena();
    }

    //Seleccionar Foto
    private void seleccionarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.jpeg"));
        imagenSeleccionada = fileChooser.showOpenDialog(lbUsuario.getScene().getWindow());
        if (imagenSeleccionada != null) {
            ivPerfil.setImage(new Image(imagenSeleccionada.toURI().toString()));
            ivPerfil.setFitWidth(100);
            ivPerfil.setFitHeight(150);
        }

    }

    @FXML
    private void btnGuardarColaborador(ActionEvent event) {

        if (estaEditando) {
            String ruta = Utilidades.imagenToArchivo(ivPerfil.getImage());
            this.imagenSeleccionada = new File(ruta);
        }

        if (validarCampos()) {
            if (estaEditando) {
                Colaborador colaborador = generarColaboradorDesdeFormulario();
                colaborador.setIdColaborador(this.colaboradorEditar.getIdColaborador());
                colaborador.getPersona().setIdPersona(this.colaboradorEditar.getPersona().getIdPersona());
                colaborador.getRol().setIdRolColaborador(this.colaboradorEditar.getRol().getIdRolColaborador());
                Mensaje msj = ColaboradorDAO.editarColaborador(colaborador);
                if (msj.getError() == false) {
                    Mensaje msjFoto = new Mensaje();
                    if (this.imagenSeleccionada != null) {
                        msjFoto = actualizarFotoColaborador(false, colaborador.getIdColaborador());
                    } else {
                        String ruta = Utilidades.imagenToArchivo(ivPerfil.getImage());
                        imagenSeleccionada = new File(ruta);
                        msjFoto = actualizarFotoColaborador(false, colaborador.getIdColaborador());
                    }
                    if (msjFoto.getError() == false) {
                        Utilidades.mostrarAlertaSimple("Almacenamiento exitoso", "La foto y los datos han sido actualizado correctamente", Alert.AlertType.INFORMATION);
                        vaciarCampos();
                        llenarContenedorColaboradores();
                        imagenSeleccionada = null;
                        this.estaEditando = false;
                    } else {
                        Utilidades.mostrarAlertaSimple("Error al agregar el colaborador", msjFoto.getMensaje(), Alert.AlertType.ERROR);
                    }
                } else {
                    Utilidades.mostrarAlertaSimple("Error al actualizar el colaborador", msj.getMensaje(), Alert.AlertType.ERROR);
                }
            } else {
                Mensaje msj = ColaboradorDAO.agregarColaborador(generarColaboradorDesdeFormulario());
                if (msj.getError() == false) {
                    Integer id = Integer.parseInt(ColaboradorDAO.obtenerUltimoID());
                    if (id != null || id > 0) {
                        msj = actualizarFotoColaborador(true, id);
                        if (msj.getError() == false) {
                            Utilidades.mostrarAlertaSimple("Almacenamiento exitoso", "La foto y los datos han sido agregados correctamente", Alert.AlertType.INFORMATION);
                            vaciarCampos();
                        } else {
                            Utilidades.mostrarAlertaSimple("Error en colaborador", "No fue posible actualizar la foto del colaborador, intente editando al colaborador", Alert.AlertType.ERROR);
                        }
                    } else {
                        Utilidades.mostrarAlertaSimple("Error en colaborador", "Por el momento no es posible actualizar la fotografía del colaborador, intente más tarde", Alert.AlertType.ERROR);
                    }
                } else {
                    Utilidades.mostrarAlertaSimple("Error en colaborador", "Por el momento no es posible agregar al colaborador, intente más tarde", Alert.AlertType.ERROR);
                }
            }
        }

    }

    private void llenarContenedorColaboradores() {
        listaColaborador = ColaboradorDAO.obtenerClientes();
        fpColaborador.getChildren().clear();
        if (listaColaborador != null) {
            if (listaColaborador.size() == 0) {
                Utilidades.mostrarAlertaSimple("No hay registro", "Por el momento no se tiene registros de colaboradores", Alert.AlertType.INFORMATION);
            } else {
                llenarTarjetasClientes(this.listaColaborador);
            }
        } else {
            Utilidades.mostrarAlertaSimple("Problemas al obtener colaboradores",
                    "Por el momento no se pueden obtener los colaboradores, intente más tarde, por favor.", Alert.AlertType.INFORMATION);
        }
    }

    private void llenarTarjetasClientes(List<Colaborador> lista) {
        try {
            for (Colaborador colaborador : lista) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/timefastdesktop/panes/card/FXMLCardColaborador.fxml"));
                Parent tarjetaPresentacion = loader.load();
                FXMLCardColaboradorController controller = loader.getController();
                controller.setClienteData(this, colaborador);
                controller.setPadreController(this);
                fpColaborador.getChildren().add(tarjetaPresentacion);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void cambioComboBox(ActionEvent event) {
        validarConductor();
    }

    @FXML
    private void seleccionarFoto(MouseEvent event) {
        seleccionarArchivo();
    }

    @Override
    public void notificacionOperacion(String titulo, String nombre) {
        llenarContenedorColaboradores();
    }

    public void llenarDatosFormularioEditar(Colaborador colaborador) {
        this.colaboradorEditar = colaborador;
        tfNombre.setText(colaborador.getPersona().getNombre());
        tfPaterno.setText(colaborador.getPersona().getApellidoPaterno());
        tfMaterno.setText(colaborador.getPersona().getApellidoMaterno());
        tfCurp.setText(colaborador.getPersona().getCURP());
        tfCorreo.setText(colaborador.getPersona().getCorreo());
        tfNumLicencia.setText(colaborador.getRol().getNumLicencia());
        tfNumeroColaborador.setText(colaborador.getNoPersonal());
        psPassword.setText(colaborador.getContrasena());
        pfConfirmarPassword.setText(colaborador.getContrasena());
        cbRol.getSelectionModel().select(colaborador.getRol().getRol());
        ivPerfil.setImage(Utilidades.convertirImagenDesdeBase64(colaborador.getPersona().getFotoBase64()));
        this.estaEditando = true;
    }

    //Barra de búsqueda
    @FXML
    private void textoCambia(KeyEvent event) {
        String texto = tfBuscar.getText();
        if (texto.isEmpty()) {
            llenarContenedorColaboradores();
        }
    }

    @FXML
    private void buscarUnidad(ActionEvent event) {
        String busqueda = tfBuscar.getText();
        if (!busqueda.isEmpty() && !busqueda.equals(" ")) {
            buscarUnidad(busqueda);
        } else {
            fpColaborador.getChildren().clear();
            llenarContenedorColaboradores();
        }
    }

    private void buscarUnidad(String busqueda) {
        ArrayList<Colaborador> listaFiltrada = new ArrayList<>();
        if (listaColaborador.size() == 0) {
            Utilidades.mostrarAlertaSimple("No hay clientes", "No hay clientes para su búsqueda", Alert.AlertType.INFORMATION);
        } else {
            listaFiltrada.clear();
            fpColaborador.getChildren().clear();
            for (Colaborador colaborador : listaColaborador) {
                String nombre = colaborador.getPersona().getNombre();
                String nuPersonal = colaborador.getNoPersonal();
                String rol = colaborador.getRol().getRol();

                if (nombre.toLowerCase().contains(busqueda)) {
                    listaFiltrada.add(colaborador);
                } else if (nuPersonal.toLowerCase().contains(busqueda)) {
                    listaFiltrada.add(colaborador);
                } else if (rol.toLowerCase().contains(busqueda)) {
                    listaFiltrada.add(colaborador);
                }
            }
            if (listaFiltrada.size() > 0) {
                llenarTarjetasClientes(listaFiltrada);
            }
        }
    }

}
