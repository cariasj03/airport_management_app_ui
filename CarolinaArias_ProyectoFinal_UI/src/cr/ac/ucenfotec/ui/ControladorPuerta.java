package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Puerta;
import cr.ac.ucenfotec.entidades.Ubicacion;
import cr.ac.ucenfotec.logica.GestorPuertas;
import cr.ac.ucenfotec.logica.GestorUbicaciones;
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
import javafx.util.Callback;

import java.io.IOException;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 24/11/2022
 *
 * Esta clase se encarga de gestionar el formulario FXML de Puertas
 */
public class ControladorPuerta {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField nombreText;
    public TextField codigoText;
    @FXML
    TableView<Puerta> listaPuertas;
    @FXML
    TableColumn<Puerta,String> tNombre;
    @FXML
    TableColumn<Puerta,String> tCodigo;
    @FXML
    public ObservableList<Puerta> observablePuertas;
    public ComboBox<Ubicacion> ubicacionCB;
    @FXML
    public ObservableList<Ubicacion> observableUbicaciones;


    /**
     * Metodo para registrar una puerta
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void registrar (ActionEvent actionEvent) {
        if (codigoText.getText().isEmpty() || nombreText.getText().isEmpty() || ubicacionCB.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin llenar", "Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
        } else {
                GestorPuertas gestorPuertas = new GestorPuertas();
                Puerta puerta = obtenerPuerta();
                String mensaje = gestorPuertas.insertarPuerta(puerta);
                if (mensaje.equals("La puerta fue registrada con éxito.")) {
                    showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                    resetearValores();
                    cargarListaPuertas();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
                }
            }
    }

    /**
     * Metodo para obtener los valores de una puerta en los TextField
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void dobleClick(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            Puerta puerta = (Puerta) listaPuertas.getSelectionModel().getSelectedItem();
            nombreText.setText(puerta.getNombre());
            codigoText.setText(puerta.getCodigo());
            ubicacionCB.getSelectionModel().select(puerta.getUbicacion());
        }
    }

    /**
     * Metodo para actualizar una puerta
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarPuerta (ActionEvent actionEvent) {
        if(nombreText.getText().isEmpty() || codigoText.getText().isEmpty() || ubicacionCB.getValue() == null)
        {
            showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
        } else {
            GestorPuertas gestorPuertas = new GestorPuertas();
            Puerta puerta = obtenerPuerta();
            String mensaje = gestorPuertas.actualizarPuerta(puerta);
            if(mensaje.equals("La puerta fue actualizada con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaPuertas ();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
    }

    /**
     * Metodo para eliminar una puerta
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarPuerta (ActionEvent actionEvent) {
        if(listaPuertas.getSelectionModel().getSelectedItem() == null)
        {
            showAlert(Alert.AlertType.ERROR,"No ha seleccionado ninguna puerta.","No ha seleccionado ninguna puerta.\nPor favor seleccione una puerta para eliminar.");
        } else {
            GestorPuertas gestorPuertas = new GestorPuertas();
            Puerta puerta = (Puerta) listaPuertas.getSelectionModel().getSelectedItem();
            String mensaje = gestorPuertas.eliminarPuerta(puerta);
            if(mensaje.equals("La puerta fue eliminada con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaPuertas();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
    }

    /**
     * Metodo para obtener los datos de una puerta de los TextField
     */
    public Puerta obtenerPuerta() {
        String codigo = codigoText.getText();
        String nombre = nombreText.getText();
        Ubicacion ubicacion = (Ubicacion) ubicacionCB.getValue();
        Puerta puerta = new Puerta(codigo, nombre, ubicacion);

        return puerta;
    }

    /**
     * Metodo para resetear los valores dem los TextField
     */
    public void resetearValores() {
        cargarListaPuertas();
        nombreText.setText("");
        codigoText.setText("");
        ubicacionCB.getSelectionModel().clearSelection();
    }

    /**
     * Metodo para actualizar el TableView de las puertas
     */
    public void cargarListaPuertas () {
        GestorPuertas gestorPuertas = new GestorPuertas();
        listaPuertas.getItems().clear();
        observablePuertas = FXCollections.observableArrayList();
        gestorPuertas.listarPuertas().forEach(puerta -> observablePuertas.addAll(puerta));
        observablePuertas = FXCollections.observableArrayList(gestorPuertas.listarPuertas());
        tCodigo.setCellValueFactory(new PropertyValueFactory<Puerta,String>("codigo"));
        tNombre.setCellValueFactory(new PropertyValueFactory<Puerta,String>("nombre"));
        listaPuertas.setItems(observablePuertas);
        cargarComboBoxes();
    }

    /**
     * Metodo para actualizar los ComboBoxes
     */
    public void cargarComboBoxes () {
        GestorUbicaciones gestorUbicaciones = new GestorUbicaciones();
        observableUbicaciones = FXCollections.observableArrayList(gestorUbicaciones.listarUbicaciones());
        ubicacionCB.setItems(observableUbicaciones);
        Callback<ListView<Ubicacion>, ListCell<Ubicacion>> cellFactory = new Callback<>() {

            @Override
            public ListCell<Ubicacion> call(ListView<Ubicacion> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Ubicacion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText("(" + item.getCodigo()+ ") - Nivel " + item.getNivel());
                        }
                    }
                };
            }
        };

        ubicacionCB.setButtonCell(cellFactory.call(null));
        ubicacionCB.setCellFactory(cellFactory);
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
            cargarListaPuertas ();
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
     * Metodo para ir a la pantalla de usuarios
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void usuarios (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Usuario.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de tripulantes
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void tripulantes (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tripulante.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de tripulaciones
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void tripulaciones (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tripulacion.fxml"));
        root = loader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de paises
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void paises (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Ubicacion.fxml"));
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
