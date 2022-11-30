package cr.ac.ucenfotec.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class UI extends Application {
    public static void main(String[] args) throws IOException, InterruptedException {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login.fxml")));
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
