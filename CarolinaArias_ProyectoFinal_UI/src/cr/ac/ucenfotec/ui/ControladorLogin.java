package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Administrador;
import cr.ac.ucenfotec.logica.Gestor;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 24/11/2022
 *
 * Esta clase se encarga de gestionar el formulario FXML del Login
 */
public class ControladorLogin {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField idText;
    public PasswordField passField;

    public void iniciarSesionClick (ActionEvent actionEvent) throws IOException {
        String id = idText.getText();
        String contrasena = passField.getText();
        Gestor gestor = new Gestor();

        if(idText.getText().isEmpty() || passField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR,"Debe ingresar todos los campos.","Debe ingresar tanto el usuario como la contraseña.\nPor favor ingréselos y vuelva a intentarlo.");
            return;
        } else {
            Administrador administrador = new Administrador(id, contrasena);
            if(!gestor.loginAdministrador(administrador)) {
                showAlert(Alert.AlertType.ERROR,"Credenciales incorrectas.","La identificación o contraseña ingresados son incorrectos.\nPor favor revíselos y vuelva a intentarlo.");
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Inicio.fxml"));
                root = loader.load();

                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    /**
     * Metodo para obtener los valores de un administrador en los TextField
     * @param keyEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void iniciarSesionEnter(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            String id = idText.getText();
            String contrasena = passField.getText();
            Gestor gestor = new Gestor();

            if(idText.getText().isEmpty() || passField.getText().isEmpty())
            {
                showAlert(Alert.AlertType.ERROR,"Debe ingresar todos los campos.","Debe ingresar tanto el usuario como la contraseña.\nPor favor ingréselos y vuelva a intentarlo.");
                return;
            } else {
                Administrador administrador = new Administrador(id, contrasena);
                if(!gestor.loginAdministrador(administrador)) {
                    showAlert(Alert.AlertType.ERROR,"Credenciales incorrectas.","La identificación o contraseña ingresados son incorrectos.\nPor favor revíselos y vuelva a intentarlo.");
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Inicio.fxml"));
                    root = loader.load();

                    stage = (Stage) ((Node) keyEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
