package timefastdesktop.utilidades;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;


public class Utilidades {

    public static void estilizarBarraScroll(ScrollPane scroll) {
        Platform.runLater(() -> {
            scroll.lookupAll(".scroll-bar").forEach(scrollBar -> {
                scrollBar.setStyle(
                        "-fx-background-color: transparent;"
                        + "-fx-background-radius: 15;"
                        + "-fx-pref-width: 8px;"
                        + "-fx-pref-height: 8px;");
            });
            scroll.lookupAll(".scroll-bar .thumb").forEach(thumb -> {
                thumb.setStyle(
                        "-fx-background-color:  #205375;"
                        + "-fx-background-radius: 15;");
            });
            scroll.lookupAll(".scroll-bar .increment-button").forEach(button -> button.setOpacity(0));
            scroll.lookupAll(".scroll-bar .decrement-button").forEach(button -> button.setOpacity(0));
        });
    }

    public static void mostrarAlertaSimple(String title, String contentenido, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(title);
        alerta.setHeaderText(null);
        alerta.setContentText(contentenido);
        alerta.showAndWait();
    }

    public static byte[] archivoAByte(File archivo) {
        try {
            FileInputStream fis = null;
            fis = new FileInputStream(archivo);
            byte[] byteArray = new byte[(int) archivo.length()];
            fis.read(byteArray);
            return byteArray;
        } catch (Exception ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public static Image convertirImagenDesdeBase64(String base64) {
        try {
            if (base64 != null && !base64.equals("")) {
                byte[] imgBytes = Base64.getDecoder().decode(cleanBase64(base64));
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imgBytes);
                return new Image(byteArrayInputStream);
            } else {
                return null;
            }
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String cleanBase64(String base64) {
        base64 = base64.replaceAll("[^A-Za-z0-9+/=]", "");
        int length = base64.length();
        int paddingRequired = 4 - (length % 4);
        if (paddingRequired != 4) {
            for (int i = 0; i < paddingRequired; i++) {
                base64 += "=";
            }
        }
        return base64;
    }

    public static String imagenToArchivo(Image imagen) {
        String ruta = "src/timefastdesktop/recursos/imagen.jpeg";
        try {
            File directorio = new File("src/timefastdesktop/recursos/");
            if (!directorio.exists()) {
                boolean dirsCreado = directorio.mkdirs();
                if (dirsCreado) {
                    System.out.println("Directorio creado: " + directorio.getAbsolutePath());
                } else {
                    System.out.println("No se pudo crear el directorio.");
                }
            }
            BufferedImage bufferedImage = javafx.embed.swing.SwingFXUtils.fromFXImage(imagen, null);
            File file = new File(ruta);
            if (file.exists()) {
                System.out.println("El archivo ya existe, se sobrescribirá.");
            } else {
                if (file.createNewFile()) {
                    System.out.println("El archivo fue creado con éxito.");
                } else {
                    System.out.println("No se pudo crear el archivo.");
                    return null;
                }
            }
            boolean guardado = ImageIO.write(bufferedImage, "jpeg", file);
            if (guardado) {
                System.out.println("Imagen convertida y guardada como archivo.");
                return ruta;
            } else {
                System.out.println("No se pudo guardar la imagen.");
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
