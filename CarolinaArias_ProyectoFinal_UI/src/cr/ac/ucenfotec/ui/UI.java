package cr.ac.ucenfotec.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 24/11/2022
 *
 * Esta clase se encarga de gestionar la interfaz de usuario
 */

public class UI extends Application {
    public static void main(String[] args) throws IOException, InterruptedException {
        launch(args);
    }

    /**
     * Metodo start requerido para iniciar la aplicacion en JavaFx
     */
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Inicio.fxml")));
            Image icon = new Image("icon.png");
            stage.getIcons().add(icon);
            stage.setTitle("Sistema de Gestión de Aeropuertos");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
