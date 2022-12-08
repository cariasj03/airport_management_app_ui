package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Persona;
import cr.ac.ucenfotec.logica.GestorPersonas;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    public void iniciarSesion (ActionEvent actionEvent) throws IOException {
        String id = idText.getText();
        String contrasena = passField.getText();
        GestorPersonas gestorPersonas = new GestorPersonas();
        Persona personaSesion = gestorPersonas.buscarPersona(id);

        if(idText.getText().isEmpty() || passField.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR,"Debe ingresar todos los campos.","Debe ingresar tanto el usuario como la contraseña.\nPor favor ingréselos y vuelva a intentarlo.");
        } else {
            if (personaSesion == null) {
                showAlert(Alert.AlertType.ERROR,"La persona no se encuentra registrada.","La persona no se encuentra registrada.\nPor favor revise las credenciales y vuelva a intentarlo.");
            } else {
                Persona personaLogin = new Persona(id, contrasena);
                if(!gestorPersonas.loginPersona(personaLogin)) {
                    showAlert(Alert.AlertType.ERROR,"Credenciales incorrectas.","La identificación o contraseña ingresados son incorrectos.\nPor favor revíselos y vuelva a intentarlo.");
                } else {
                    cambiarPantalla(personaSesion, actionEvent);
                }
            }

        }
    }

    public void iniciarSesionClick (ActionEvent actionEvent) throws IOException {
        try {
            iniciarSesion(actionEvent);
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    private void cambiarPantalla(Persona tmpPersona, ActionEvent actionEvent){
        try {
            GestorPersonas gestorPersonas = new GestorPersonas();
            int tipoPersona = gestorPersonas.tipoPersona(tmpPersona);
            switch (tipoPersona) {
                case 1:
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("InicioAdmin.fxml"));
                    root = loader.load();
                    ControladorInicio controladorInicio = loader.getController();
                    controladorInicio.setPersonaSesion(tmpPersona);
                    controladorInicio.mostrarNombrePersona();

                    stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                    break;
                case 2:
                    break;
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    /**
     * Metodo para obtener los valores de una persona en los TextField
     * @param keyEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse

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
                Persona persona = new Persona(id, contrasena);
                if(!gestor.loginPersona(persona)) {
                    showAlert(Alert.AlertType.ERROR,"Credenciales incorrectas.","La identificación o contraseña ingresados son incorrectos.\nPor favor revíselos y vuelva a intentarlo.");
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("InicioAdministrador.fxml"));
                    root = loader.load();

                    stage = (Stage) ((Node) keyEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            }
        }
    }
    */

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
