package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Aeropuerto;
import cr.ac.ucenfotec.entidades.Pais;
import cr.ac.ucenfotec.entidades.Persona;
import cr.ac.ucenfotec.logica.GestorAeropuertos;
import cr.ac.ucenfotec.logica.GestorPaises;
import javafx.beans.property.SimpleStringProperty;
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
    TableColumn<Aeropuerto,String> tPais;
    @FXML
    public ObservableList<Aeropuerto> observableAeropuertos;
    public ComboBox<Pais> paisCB;
    @FXML
    public ObservableList<Pais> observablePaises;
    private Persona personaSesion;

    //Getter y setter para la persona en sesion
    public Persona getPersonaSesion() {
        return personaSesion;
    }
    public void setPersonaSesion(Persona personaSesion) {
        this.personaSesion = personaSesion;
    }


    /**
     * Metodo para registrar un aeropuerto
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void registrar (ActionEvent actionEvent) {
        try {
        if (nombreText.getText().isEmpty() || codigoText.getText().isEmpty() || paisCB.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin llenar", "Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
        } else {
            if (codigoText.getText().length() != 3) {
                showAlert(Alert.AlertType.ERROR, "Revise el código del aeropuerto.", "El código del aeropuerto no tiene el formato adecuado.\nEl código debe estar conformado de exactamente 3 letras.");
            } else {
                GestorAeropuertos gestorAeropuertos = new GestorAeropuertos();
                Aeropuerto aeropuerto = obtenerAeropuerto();
                String mensaje = gestorAeropuertos.insertarAeropuerto(aeropuerto);
                if (mensaje.equals("El aeropuerto fue registrado con éxito.")) {
                    showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                    resetearValores();
                    cargarListaAeropuertos();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
                }
            }
        }
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para obtener los valores de un aeropuerto en los TextField
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void dobleClick(MouseEvent mouseEvent) {
        try {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            Aeropuerto aeropuerto = (Aeropuerto) listaAeropuertos.getSelectionModel().getSelectedItem();
            nombreText.setText(aeropuerto.getNombre());
            codigoText.setText(aeropuerto.getCodigo());
            paisCB.getSelectionModel().select(aeropuerto.getPais());
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
        }
    }

    /**
     * Metodo para actualizar un aeropuerto
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarAeropuerto (ActionEvent actionEvent) {
        try {
        if(nombreText.getText().isEmpty() || codigoText.getText().isEmpty() || paisCB.getValue() == null)
        {
            showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
        } else {
            GestorAeropuertos gestorAeropuertos = new GestorAeropuertos();
            Aeropuerto aeropuerto = obtenerAeropuerto();
            String mensaje = gestorAeropuertos.actualizarAeropuerto(aeropuerto);
            if(mensaje.equals("El aeropuerto fue actualizado con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaAeropuertos ();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para eliminar un aeropuerto
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarAeropuerto (ActionEvent actionEvent) {
        try {
            GestorAeropuertos gestorAeropuertos = new GestorAeropuertos();
            Aeropuerto aeropuerto = (Aeropuerto) listaAeropuertos.getSelectionModel().getSelectedItem();
        if(listaAeropuertos.getSelectionModel().getSelectedItem() == null)
        {
            showAlert(Alert.AlertType.ERROR,"No ha seleccionado ningún aeropuerto.","No ha seleccionado ningún aeropuerto.\nPor favor seleccione un aeropuerto para eliminar.");
        } else {
            if (gestorAeropuertos.tieneVuelosAsignados(aeropuerto)) {
                showAlert(Alert.AlertType.ERROR, "El aeropuerto tiene vuelos asociados.", "El aeropuerto tiene vuelos asociados.\nPor favor elimine los vuelos asociados primero.");
            } else {
                String mensaje = gestorAeropuertos.eliminarAeropuerto(aeropuerto);
                if (mensaje.equals("El aeropuerto fue eliminado con éxito.")) {
                    showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                    cargarListaAeropuertos();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
                }
            }
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
        }
    }

    /**
     * Metodo para obtener los datos de un aeropuerto de los TextField
     */
    public Aeropuerto obtenerAeropuerto() {
        String nombre = nombreText.getText();
        String codigo = codigoText.getText();
        Pais pais = (Pais) paisCB.getValue();
        Aeropuerto aeropuerto = new Aeropuerto(nombre, codigo, pais);
        return aeropuerto;
    }

    /**
     * Metodo para resetear los valores dem los TextField
     */
    public void resetearValores() {
        cargarListaAeropuertos();
        nombreText.setText("");
        codigoText.setText("");
        paisCB.getSelectionModel().clearSelection();
    }

    /**
     * Metodo para actualizar el TableView de los aeropuertos
     */
    public void cargarListaAeropuertos () {
        try {
        GestorAeropuertos gestorAeropuertos = new GestorAeropuertos();
        listaAeropuertos.getItems().clear();
        observableAeropuertos = FXCollections.observableArrayList();
        gestorAeropuertos.listarAeropuertos().forEach(aeropuerto -> observableAeropuertos.addAll(aeropuerto));
        observableAeropuertos = FXCollections.observableArrayList(gestorAeropuertos.listarAeropuertos());
        tNombre.setCellValueFactory(new PropertyValueFactory<Aeropuerto,String>("nombre"));
        tCodigo.setCellValueFactory(new PropertyValueFactory<Aeropuerto,String>("codigo"));
        tPais.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPais().getNombre()));
        listaAeropuertos.setItems(observableAeropuertos);
        cargarComboBoxes();
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para actualizar los ComboBoxes
     */
    public void cargarComboBoxes () {
        try {
        GestorPaises gestorPaises = new GestorPaises();
        observablePaises = FXCollections.observableArrayList(gestorPaises.listarPaises());
        paisCB.setItems(observablePaises);
        Callback<ListView<Pais>, ListCell<Pais>> cellFactory = new Callback<>() {

            @Override
            public ListCell<Pais> call(ListView<Pais> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Pais item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText("(" + item.getCodigo()+ ") " + item.getNombre());
                        }
                    }
                };
            }
        };

        paisCB.setButtonCell(cellFactory.call(null));
        paisCB.setCellFactory(cellFactory);
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
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
     * Metodo para ir a la pantalla de inicio para administradores
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void inicio (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InicioAdmin.fxml"));
        root = loader.load();
        ControladorInicio controladorInicio = loader.getController();
        controladorInicio.setPersonaSesion(personaSesion);
        controladorInicio.mostrarNombrePersona();

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
        ControladorAdministrador controladorAdministrador = loader.getController();
        controladorAdministrador.setPersonaSesion(personaSesion);

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
        ControladorUsuario controladorUsuario = loader.getController();
        controladorUsuario.setPersonaSesion(personaSesion);

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
        ControladorTripulante controladorTripulante = loader.getController();
        controladorTripulante.setPersonaSesion(personaSesion);

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TripulacionAdministrador.fxml"));
        root = loader.load();
        ControladorTripulacion controladorTripulacion = loader.getController();
        controladorTripulacion.setPersonaSesion(personaSesion);

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Pais.fxml"));
        root = loader.load();
        ControladorPais controladorPais = loader.getController();
        controladorPais.setPersonaSesion(personaSesion);

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
        ControladorAeropuerto controladorAeropuerto = loader.getController();
        controladorAeropuerto.setPersonaSesion(personaSesion);

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de aerolineas
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void aerolineas (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Aerolinea.fxml"));
        root = loader.load();
        ControladorAerolinea controladorAerolinea = loader.getController();
        controladorAerolinea.setPersonaSesion(personaSesion);

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VueloAdministrador.fxml"));
        root = loader.load();
        ControladorVuelo controladorVuelo = loader.getController();
        controladorVuelo.setPersonaSesion(personaSesion);

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
        ControladorUbicacion controladorUbicacion = loader.getController();
        controladorUbicacion.setPersonaSesion(personaSesion);

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de puertas
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void puertas (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Puerta.fxml"));
        root = loader.load();
        ControladorPuerta controladorPuerta = loader.getController();
        controladorPuerta.setPersonaSesion(personaSesion);

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo para ir a la pantalla de perfil
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void perfil (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Perfil.fxml"));
        root = loader.load();
        ControladorPerfil controladorPerfil = loader.getController();
        controladorPerfil.setPersonaSesion(personaSesion);
        controladorPerfil.cargarDatosPersona();

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
