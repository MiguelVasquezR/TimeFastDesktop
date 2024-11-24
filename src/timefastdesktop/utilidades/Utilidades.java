package timefastdesktop.utilidades;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;

public class Utilidades {

    public static void estilizarBarraScroll(ScrollPane scroll) {
        Platform.runLater(() -> {
            scroll.lookupAll(".scroll-bar").forEach(scrollBar -> {
                scrollBar.setStyle(
                        "-fx-background-color: transparent;"
                        + "-fx-background-radius: 15;"
                        + "-fx-pref-width: 8px;"
                        + "-fx-pref-height: 8px;"
                );
            });
            scroll.lookupAll(".scroll-bar .thumb").forEach(thumb -> {
                thumb.setStyle(
                        "-fx-background-color:  #205375;"
                        + "-fx-background-radius: 15;"
                );
            });
            scroll.lookupAll(".scroll-bar .increment-button").forEach(button -> button.setOpacity(0));
            scroll.lookupAll(".scroll-bar .decrement-button").forEach(button -> button.setOpacity(0));
        });
    }

}
