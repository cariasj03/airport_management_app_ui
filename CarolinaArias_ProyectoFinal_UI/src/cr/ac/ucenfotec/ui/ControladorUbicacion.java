package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Ubicacion;
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
import java.time.format.DateTimeParseException;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 24/11/2022
 *
 * Esta clase se encarga de gestionar el formulario FXML de Ubicaciones
 */
public class ControladorUbicacion {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField codigoText;
    public TextField nivelText;
    @FXML
    TableView<Ubicacion> listaUbicaciones;
    @FXML
    TableColumn<Ubicacion,String> tCodigo;
    @FXML
    TableColumn<Ubicacion, Integer> tNivel;
    @FXML
    public ObservableList<Ubicacion> observableUbicaciones;

    /**
     * Metodo para registrar una ubicacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void registrar (ActionEvent actionEvent) {
        try {
            if(codigoText.getText().isEmpty() || nivelText.getText().isEmpty())
            {
                showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos\nobligatorios.");
            } else {
                Gestor gestor = new Gestor();
                Ubicacion ubicacion = obtenerUbicacion();
                String mensaje = gestor.insertarUbicacion(ubicacion);
                if(mensaje.equals("La ubicación fue registrada con éxito."))
                {
                    showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                    resetearValores();
                } else {
                    showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
                }
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"El campo sólo acepta valores numéricos.","El campo nivel sólo acepta valores numéricos.");
        }
    }

    /**
     * Metodo para obtener los valores de una ubicacion en los TextField
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void dobleClick(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            Ubicacion ubicacion = (Ubicacion) listaUbicaciones.getSelectionModel().getSelectedItem();
            codigoText.setText(ubicacion.getCodigo());
            nivelText.setText(String.valueOf(ubicacion.getNivel()));
        }
    }

    /**
     * Metodo para actualizar una ubicacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarUbicacion (ActionEvent actionEvent) {
        try {
            if(codigoText.getText().isEmpty() || nivelText.getText().isEmpty())
            {
                showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos\nobligatorios.");
            } else {
                Gestor gestor = new Gestor();
                Ubicacion ubicacion = obtenerUbicacion();
                String mensaje = gestor.actualizarUbicacion(ubicacion);
                if(mensaje.equals("La ubicación fue actualizada con éxito."))
                {
                    showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                    cargarListaUbicaciones();
                } else {
                    showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
                }
            }
        } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR,"El campo sólo acepta valores numéricos.","El campo nivel sólo acepta valores numéricos.");
        }
    }

    /**
     * Metodo para eliminar una ubicacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarUbicacion (ActionEvent actionEvent) {
            if(listaUbicaciones.getSelectionModel().getSelectedItem() == null)
            {
                showAlert(Alert.AlertType.ERROR,"No ha seleccionado ninguna ubicación.","No ha seleccionado ninguna ubicación.\nPor favor seleccione una ubicación para eliminar.");
            } else {
                Gestor gestor = new Gestor();
                Ubicacion ubicacion = (Ubicacion) listaUbicaciones.getSelectionModel().getSelectedItem();
                String mensaje = gestor.eliminarUbicacion(ubicacion);
                if(mensaje.equals("La ubicación fue eliminada con éxito."))
                {
                    showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                    cargarListaUbicaciones();
                } else {
                    showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
                }
            }
    }

    /**
     * Metodo para obtener los datos de una ubicacion de los TextField
     */
    public Ubicacion obtenerUbicacion() {
        String codigo = codigoText.getText();
        int horaSalida = Integer.parseInt(nivelText.getText());
        Ubicacion ubicacion = new Ubicacion(codigo, horaSalida);

        return ubicacion;
    }

    /**
     * Metodo para resetear los valores dem los TextField
     */
    public void resetearValores() {
        cargarListaUbicaciones();
        codigoText.setText("");
        nivelText.setText("");
    }

    /**
     * Metodo para actualizar el TableView de las ubicaciones
     */
    public void cargarListaUbicaciones(){
        Gestor gestor = new Gestor();
        listaUbicaciones.getItems().clear();
        observableUbicaciones = FXCollections.observableArrayList();
        gestor.listarUbicaciones().forEach(ubicacion -> observableUbicaciones.addAll(ubicacion));
        tCodigo.setCellValueFactory(new PropertyValueFactory<Ubicacion,String>("codigo"));
        tNivel.setCellValueFactory(new PropertyValueFactory<Ubicacion,Integer>("nivel"));
        listaUbicaciones.setItems(observableUbicaciones);
    }

    /**
     * Metodo para inicializar el ObservableList y el TableView
     */
    @FXML
    public void initialize()
    {
        try {
            cargarListaUbicaciones();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Metodo para mostrar una alerta al usuario
     * @param alertType es de tipo Alert.AlertType y corresponde al tipo de alerta
     * @param title es tipo String y corresponde al título de la alerta
     * @param message  es de tipo String y corresponde al mensaje de la alerta
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
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
     * Metodo para ir a la pantalla de aeropuertos
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void aeropuertos (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Aeropuerto.fxml"));
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
     * Metodo para salir de la aplicacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void salir(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }
}
