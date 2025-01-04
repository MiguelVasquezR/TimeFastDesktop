package timefastdesktop.panes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import timefastdesktop.utilidades.Utilidades;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import timefastdesktop.modelo.dao.UnidadDAO;
import timefastdesktop.observador.NotificadorOperacion;
import timefastdesktop.panes.card.FXMLCardUnidadController;
import timefastdesktop.pojo.Mensaje;
import timefastdesktop.pojo.Unidad;

public class FXMLUnidadesPaneController implements Initializable, NotificadorOperacion {

    @FXML
    private Label lbUsuario;
    @FXML
    private TextField tfBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private ImageView viUnidad;
    @FXML
    private TextField tfMarca;
    @FXML
    private TextField tfModelo;
    @FXML
    private TextField tfAno;
    @FXML
    private TextField tfVin;
    @FXML
    private ComboBox<String> cbTipoUnidad;
    @FXML
    private TextField tfNII;
    @FXML
    private Button btnGuardar;
    @FXML
    private ScrollPane spContenedor;
    @FXML
    private FlowPane fpContenedor;

    private File imagenSeleccionada;
    private List<Unidad> unidadesWS;
    private Boolean estaEditando = false;
    private Unidad unidadEditar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Utilidades.estilizarBarraScroll(spContenedor);
        tfNII.setEditable(false);
        llenarTiposUnidad();
        obtenerUnidades();
    }

    @FXML
    private void subitFoto(MouseEvent event) {
        seleccionarArchivo();
    }

    private void guardarUnidad() {
        if (validarCampos()) {
            llenarPojo();
        }
    }

    private void llenarPojo() {
        Unidad unidad = new Unidad();
        unidad.setAnio(tfAno.getText());
        unidad.setMarca(tfMarca.getText());
        unidad.setModelo(tfModelo.getText());
        unidad.setVIN(tfVin.getText());
        unidad.setNumIdentificacion(tfNII.getText());
        unidad.setTipo(cbTipoUnidad.getSelectionModel().getSelectedItem());
        unidad.setId(this.unidadEditar.getId());

        if (estaEditando) {
            Mensaje msj = UnidadDAO.editarUnidad(unidad);
            if (msj.getError() == false) {
                Mensaje msjFoto = new Mensaje();
                if (this.imagenSeleccionada != null) {
                    msjFoto = actualizarFoto(false, unidad.getId());
                } else {
                    String ruta = Utilidades.imagenToArchivo(viUnidad.getImage());
                    imagenSeleccionada = new File(ruta);
                    msjFoto = actualizarFoto(false, unidad.getId());
                }
                if (msjFoto.getError() == false) {
                    Utilidades.mostrarAlertaSimple("Almacenamiento exitoso", "La foto y los datos han sido actualizado correctamente", Alert.AlertType.INFORMATION);
                    vaciarCampos();
                    obtenerUnidades();
                    imagenSeleccionada = null;

                } else {
                    Utilidades.mostrarAlertaSimple("Error al agregar Unidad", msjFoto.getMensaje(), Alert.AlertType.ERROR);
                }
            } else {
                Utilidades.mostrarAlertaSimple("Error al actualizar Unidad", msj.getMensaje(), Alert.AlertType.ERROR);
            }
        } else {
            Mensaje msj = UnidadDAO.agregarUnidad(unidad);
            if (msj != null && msj.getError() == false) {
                Integer id = Integer.parseInt(UnidadDAO.obtenerUltimoID());
                if (id != null || id != 0) {
                    msj = actualizarFoto(Boolean.TRUE, 0);
                    if (msj.getError() == false) {
                        Utilidades.mostrarAlertaSimple("Almacenamiento exitoso", "La foto y los datos han sido agregados correctamente", Alert.AlertType.INFORMATION);
                        vaciarCampos();
                        obtenerUnidades();
                    } else {
                        Utilidades.mostrarAlertaSimple("Error al agregar Unidad", msj.getMensaje(), Alert.AlertType.ERROR);
                    }
                } else {
                    Utilidades.mostrarAlertaSimple("No ha sido posible almacenar la imagen, pero los datos han sido agregada", msj.getMensaje(), Alert.AlertType.INFORMATION);
                }
            } else {
                Utilidades.mostrarAlertaSimple("Error al agregar Unidad", msj.getMensaje(), Alert.AlertType.ERROR);
            }
        }

    }

    private Mensaje actualizarFoto(Boolean estaAgregando, Integer id) {
        Mensaje msj = new Mensaje();
        if (imagenSeleccionada != null) {
            try {
                byte[] byteArray = Utilidades.archivoAByte(imagenSeleccionada);
                if (estaAgregando) {
                    Integer idWS = Integer.parseInt(UnidadDAO.obtenerUltimoID());
                    if (idWS != null || idWS > 0) {
                        msj = UnidadDAO.actualizarFoto(idWS, byteArray);
                    } else {
                        msj.setError(true);
                        msj.setMensaje("No ha sido posible actualizar la foto");
                    }
                } else {
                    msj = UnidadDAO.actualizarFoto(id, byteArray);
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

    private boolean validarCampos() {
        StringBuilder mensaje = new StringBuilder();
        if (tfMarca.getText().isEmpty()) {
            mensaje.append(" Falta la marca");
        }
        if (tfModelo.getText().isEmpty()) {
            mensaje.append(" Falta el Modelo,");
        }
        if (tfAno.getText().isEmpty()) {
            mensaje.append(" Falta el Año,");
        }
        if (tfVin.getText().isEmpty()) {
            mensaje.append(" Falta el VII,");
        }
        if (cbTipoUnidad.getSelectionModel().getSelectedIndex() < 0) {
            mensaje.append(" Selecciona un tipo de Unidad");
        }
        if (mensaje.length() > 1) {
            Utilidades.mostrarAlertaSimple("Error en datos", "Te cuidado" + mensaje, Alert.AlertType.INFORMATION);
            return false;
        } else {
            tfNII.setText(generarNII(tfAno.getText(), tfVin.getText()));
        }
        return true;
    }

    private String generarNII(String ano, String vii) {
        return ano + vii.charAt(0) + vii.charAt(1) + vii.charAt(2) + vii.charAt(3);
    }

    private void vaciarCampos() {
        File file = new File("src/timefastdesktop/recursos/unidadSelector.png");
        Image image = new Image(file.toURI().toString());
        viUnidad.setImage(image);
        tfMarca.clear();
        tfModelo.clear();
        tfAno.clear();
        tfVin.clear();
        cbTipoUnidad.getSelectionModel().select(-1);
        tfNII.clear();
    }

    private void llenarTiposUnidad() {
        String[] tipos = {"Gasolina", "Diesel", "Eléctrica", "Hibrida"};
        cbTipoUnidad.getItems().addAll(tipos);
    }

    private void seleccionarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.jpeg"));
        imagenSeleccionada = fileChooser.showOpenDialog(lbUsuario.getScene().getWindow());
        if (imagenSeleccionada != null) {
            viUnidad.setImage(new Image(imagenSeleccionada.toURI().toString()));
            viUnidad.setFitWidth(150);
            viUnidad.setFitHeight(120);
            viUnidad.setPreserveRatio(true);
        }
    }

    @FXML
    private void guardarDatos(ActionEvent event) {
        guardarUnidad();
    }

    @FXML
    private void buscarUnidad(ActionEvent event) {
        String busqueda = tfBuscar.getText();
        if (!busqueda.isEmpty() && !busqueda.equals(" ")) {
            buscarUnidad(busqueda);
        } else {
            fpContenedor.getChildren().clear();
            obtenerUnidades();
        }
    }

    //Buscar Unidad
    private void buscarUnidad(String busqueda) {
        ArrayList<Unidad> listaFiltrada = new ArrayList<>();
        if (unidadesWS.size() == 0) {
            Utilidades.mostrarAlertaSimple("No hay clientes", "No hay clientes para su búsqueda", Alert.AlertType.INFORMATION);
        } else {
            listaFiltrada.clear();
            fpContenedor.getChildren().clear();
            for (Unidad unidad : unidadesWS) {
                String vin = unidad.getVIN();
                String marca = unidad.getMarca();
                String nii = unidad.getNumIdentificacion();

                if (vin.contains(busqueda)) {
                    listaFiltrada.add(unidad);
                } else if (marca.contains(busqueda)) {
                    listaFiltrada.add(unidad);
                } else if (nii.contains(busqueda)) {
                    listaFiltrada.add(unidad);
                }
            }
            if (listaFiltrada.size() > 0) {
                llenarTarjetaUnidad(listaFiltrada);
            }
        }
    }

    @FXML
    private void textoCambia(KeyEvent event) {
        String texto = tfBuscar.getText();
        if (texto.isEmpty()) {
            obtenerUnidades();
        }
    }

    //Obtener Unidades
    private void obtenerUnidades() {
        this.unidadesWS = UnidadDAO.obtenerUnidades();
        fpContenedor.getChildren().clear();
        if (unidadesWS.size() > 1) {
            llenarTarjetaUnidad(unidadesWS);
        } else {
            Utilidades.mostrarAlertaSimple("Error obtener unidades", "Se tuvo problemas al obtener las unidades, intentelo más tarde", Alert.AlertType.ERROR);
        }
    }

    private void llenarTarjetaUnidad(List<Unidad> lista) {
        try {
            for (Unidad unidad : lista) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/timefastdesktop/panes/card/FXMLCardUnidad.fxml"));
                Parent tarjetaPresentacion = loader.load();
                FXMLCardUnidadController controlador = loader.getController();
                controlador.llenarDatos(unidad, this);
                controlador.obtenerReferenciaPadre(this);
                fpContenedor.getChildren().add(tarjetaPresentacion);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void notificacionOperacion(String titulo, String nombre) {
        obtenerUnidades();
    }

    //editar unidad
    public void llenarDatosFormularioEditar(Unidad unidad) {
        tfMarca.setText(unidad.getMarca());
        tfModelo.setText(unidad.getModelo());
        tfAno.setText(unidad.getAnio());
        tfVin.setText(unidad.getVIN());
        tfNII.setText(unidad.getNumIdentificacion());
        cbTipoUnidad.getSelectionModel().select(obtenerIndexCB(unidad.getTipo()));
        if (unidad.getFoto() != null) {
            viUnidad.setImage(Utilidades.convertirImagenDesdeBase64(unidad.getFoto()));
        }
        tfVin.setEditable(false);
        this.unidadEditar = unidad;
        estaEditando = true;
    }

    private Integer obtenerIndexCB(String tipoUnidad) {
        Integer index = -1;
        int i = 0;
        for (String valor : cbTipoUnidad.getItems()) {
            if (valor.equals(tipoUnidad)) {
                index = i;
                break;
            }
            i++;
        }
        return index;
    }

}
