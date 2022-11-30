package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Aeropuerto;
import cr.ac.ucenfotec.logica.Gestor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 24/11/2022
 *
 * Esta clase se encarga de gestionar el formulario FXML de Aeropuertos
 */
public class ControladorAeropuerto {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField nombreText;
    public TextField codigoText;
    @FXML
    TableView<Aeropuerto> listaAeropuertos;
    @FXML
    TableColumn<Aeropuerto,String> tNombre;
    @FXML
    TableColumn<Aeropuerto,String> tCodigo;
    @FXML
    public ObservableList<Aeropuerto> observableAeropuertos;



    public void registrar (ActionEvent actionEvent) {
        String nombre = nombreText.getText();
        String codigo = codigoText.getText().toUpperCase();


        if (nombreText.getText().isEmpty() || codigoText.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin llenar", "Hay campos obligatorios sin llenar.\nPor favor llene todos los campos\nobligatorios.");
            return;
        } else {
            if (codigoText.getText().length() != 3) {
                showAlert(Alert.AlertType.ERROR, "Revise el código del aeropuerto.", "El código del aeropuerto no tiene el formato adecuado.\nEl código debe estar conformado de exactamente 3 letras.");
                return;
            } else {
                Gestor gestor = new Gestor();
                Aeropuerto aeropuerto = new Aeropuerto(nombre, codigo);
                String mensaje = gestor.insertarAeropuerto(aeropuerto);
                if (mensaje.equals("El aeropuerto fue registrado con éxito.")) {
                    showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                    nombreText.setText("");
                    codigoText.setText("");
                    observableAeropuertos = FXCollections.observableArrayList(gestor.listarAeropuertos());
                    listaAeropuertos.setItems(observableAeropuertos);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
                }
            }
        }
    }

    /**
     * Metodo para obtener los valores de un aeropuerto en los TextField
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void dobleClick(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            Aeropuerto aeropuerto = (Aeropuerto) listaAeropuertos.getSelectionModel().getSelectedItem();
            nombreText.setText(aeropuerto.getNombre());
            codigoText.setText(aeropuerto.getCodigo());
        }
    }

    /**
     * Metodo para actualizar un aeropuerto
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarAeropuerto (ActionEvent actionEvent) {
        if(nombreText.getText().isEmpty() || codigoText.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos\nobligatorios.");
        } else {
            Gestor gestor = new Gestor();
            Aeropuerto aeropuerto = obtenerAeropuerto();
            String mensaje = gestor.actualizarAeropuerto(aeropuerto);
            if(mensaje.equals("El aeropuerto fue actualizado con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaAeropuertos ();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
    }

    /**
     * Metodo para eliminar un aeropuerto
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarAeropuerto (ActionEvent actionEvent) {
        if(listaAeropuertos.getSelectionModel().getSelectedItem() == null)
        {
            showAlert(Alert.AlertType.ERROR,"No ha seleccionado ningún aeropuerto.","No ha seleccionado ningún aeropuerto.\nPor favor seleccione un aeropuerto para eliminar.");
        } else {
            Gestor gestor = new Gestor();
            Aeropuerto aeropuerto = (Aeropuerto) listaAeropuertos.getSelectionModel().getSelectedItem();
            String mensaje = gestor.eliminarAeropuerto(aeropuerto);
            if(mensaje.equals("El aeropuerto fue eliminado con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaAeropuertos();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
    }

    /**
     * Metodo para obtener los datos de un aeropuerto de los TextField
     */
    public Aeropuerto obtenerAeropuerto() {
        String nombre = nombreText.getText();
        String codigo = codigoText.getText();
        Aeropuerto aeropuerto = new Aeropuerto( nombre, codigo);

        return aeropuerto;
    }

    /**
     * Metodo para resetear los valores dem los TextField
     */
    public void resetearValores() {
        cargarListaAeropuertos();
        nombreText.setText("");
        codigoText.setText("");
    }

    /**
     * Metodo para actualizar el TableView de los aeropuertos
     */
    public void cargarListaAeropuertos () {
        Gestor gestor = new Gestor();
        listaAeropuertos.getItems().clear();
        observableAeropuertos = FXCollections.observableArrayList();
        gestor.listarAeropuertos().forEach(aeropuerto -> observableAeropuertos.addAll(aeropuerto));
        observableAeropuertos = FXCollections.observableArrayList(gestor.listarAeropuertos());
        tNombre.setCellValueFactory(new PropertyValueFactory<Aeropuerto,String>("nombre"));
        tCodigo.setCellValueFactory(new PropertyValueFactory<Aeropuerto,String>("codigo"));
        listaAeropuertos.setItems(observableAeropuertos);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Metodo para inicializar el ObservableList y el TableView
     */
    @FXML
    public void initialize() {
        try {
            cargarListaAeropuertos ();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Metodo para ir a la pantalla de inicio
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void inicio (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Inicio.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de administradores
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void administradores (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Administrador.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de vuelos
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void vuelos (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Vuelo.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de ubicaciones
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void ubicaciones (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Ubicacion.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para salir de la aplicacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void salir(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }
}
