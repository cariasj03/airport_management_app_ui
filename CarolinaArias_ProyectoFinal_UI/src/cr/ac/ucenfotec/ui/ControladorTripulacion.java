package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Persona;
import cr.ac.ucenfotec.entidades.Tripulacion;
import cr.ac.ucenfotec.entidades.Tripulante;
import cr.ac.ucenfotec.logica.GestorPersonas;
import cr.ac.ucenfotec.logica.GestorTripulaciones;
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
import java.time.LocalDate;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 03/12/2022
 *
 * Esta clase se encarga de gestionar el formulario FXML de Tripulaciones
 */
public class ControladorTripulacion {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField nombreClaveText;
    public TextField codigoText;
    @FXML
    TableView<Tripulacion> listaTripulaciones;
    @FXML
    TableColumn<Tripulacion,String> tCodigo;
    @FXML
    TableColumn<Tripulacion,String> tNombreClave;
    @FXML
    public ObservableList<Tripulacion> observableTripulaciones;
    @FXML
    TableView<Tripulante> listaTripulantes;
    @FXML
    TableColumn<Tripulante, String> tId;
    @FXML
    TableColumn<Tripulante, String> tNombre;
    @FXML
    TableColumn<Tripulante, String> tApellidos;
    @FXML
    TableColumn<Tripulante, LocalDate> tFechaNac;
    @FXML
    TableColumn<Tripulante, String> tGenero;
    @FXML
    TableColumn<Tripulante, String> tNacionalidad;
    @FXML
    TableColumn<Tripulante, String> tCorreo;
    @FXML
    public ObservableList<Tripulante> observableTripulantes;
    public ComboBox<Tripulacion> tripulacionCB;
    public ComboBox<Tripulante> tripulanteCB;
    private Persona personaSesion;

    //Getter y setter para la persona en sesion
    public Persona getPersonaSesion() {
        return personaSesion;
    }
    public void setPersonaSesion(Persona personaSesion) {
        this.personaSesion = personaSesion;
    }

    public void registrar (ActionEvent actionEvent) {
        try {
        if (codigoText.getText().isEmpty() || nombreClaveText.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin llenar", "Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
        } else {
                GestorTripulaciones gestorTripulaciones = new GestorTripulaciones();
                Tripulacion tripulacion = obtenerTripulacion();
                String mensaje = gestorTripulaciones.insertarTripulacion(tripulacion);
                if (mensaje.equals("La tripulación fue registrada con éxito.")) {
                    showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                    codigoText.setText("");
                    nombreClaveText.setText("");
                    cargarListas();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
                }
        }
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para obtener los valores de un tripulacion en los TextField
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void dobleClick(MouseEvent mouseEvent) {
        try {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            Tripulacion tripulacion = (Tripulacion) listaTripulaciones.getSelectionModel().getSelectedItem();
            codigoText.setText(tripulacion.getCodigo());
            nombreClaveText.setText(tripulacion.getNombreClave());
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
        }
    }

    /**
     * Metodo para actualizar un tripulacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarTripulacion (ActionEvent actionEvent) {
        try {
        if(codigoText.getText().isEmpty() || nombreClaveText.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
        } else {
            GestorTripulaciones gestorTripulaciones = new GestorTripulaciones();
            Tripulacion tripulacion = obtenerTripulacion();
            String mensaje = gestorTripulaciones.actualizarTripulacion(tripulacion);
            if(mensaje.equals("La tripulación fue actualizada con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListas ();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para eliminar una tripulacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarTripulacion (ActionEvent actionEvent) {
        try {
        GestorTripulaciones gestorTripulaciones = new GestorTripulaciones();
        Tripulacion tripulacion = new Tripulacion();
        if(listaTripulaciones.getSelectionModel().getSelectedItem() == null)
        {
            showAlert(Alert.AlertType.ERROR,"No ha seleccionado ninguna tripulación.","No ha seleccionado ninguna tripulación.\nPor favor seleccione una tripulación para eliminar.");
        } else {
            tripulacion = (Tripulacion) listaTripulaciones.getSelectionModel().getSelectedItem();
            if (gestorTripulaciones.tieneTripulantes(tripulacion)) {
                showAlert(Alert.AlertType.ERROR,"La tripulación tiene tripulantes asociados.","La tripulación tiene tripulantes asociados.\nPor favor elimine los tripulantes de la tripulación primero.");
            } else {
                String mensaje = gestorTripulaciones.eliminarTripulacion(tripulacion);
                if(mensaje.equals("La tripulación fue eliminada con éxito."))
                {
                    showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                    cargarListas();
                } else {
                    showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
                }
            }
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
        }
    }

    /**
     * Metodo para agregar un tripulante a una tripulacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void agregarTripulante (ActionEvent actionEvent) {
        try {
        if (tripulacionCB.getValue() == null || tripulanteCB.getValue() == null){
            showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin seleccionar.", "Debe seleccionar tanto la tripulación como el tripulante para realizar esta acción.");
        } else {
            GestorTripulaciones gestorTripulaciones = new GestorTripulaciones();
            Tripulacion tripulacion = (Tripulacion) tripulacionCB.getValue();
            Tripulante tripulante = (Tripulante) tripulanteCB.getValue();
            String mensaje = gestorTripulaciones.agregarTripulanteATripulacion(tripulante, tripulacion);
            if (mensaje.equals("El tripulante no puede ser agregado a la tripulación porque ya se encuentra en ella.") || mensaje.equals("El tripulante no puede ser agregado a la tripulación porque ya se encuentra en otra tripulación.")) {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                cargarListaTripulantes(actionEvent);
            }
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor seleccione todos los campos necesarios.");
        }
    }

    /**
     * Metodo para eliminar un tripulante de una tripulacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarTripulante (ActionEvent actionEvent) {
        try {
        if(listaTripulantes.getSelectionModel().getSelectedItem() == null)
        {
            showAlert(Alert.AlertType.ERROR,"No ha seleccionado ningún tripulante.","No ha seleccionado ningún tripulante.\nPor favor seleccione un tripulante para eliminar.");
        } else {
            GestorTripulaciones gestorTripulaciones = new GestorTripulaciones();
            Tripulacion tripulacion = (Tripulacion) tripulacionCB.getValue();
            Tripulante tripulante = (Tripulante) listaTripulantes.getSelectionModel().getSelectedItem();
            String mensaje = gestorTripulaciones.eliminarTripulanteDeTripulacion(tripulante, tripulacion);
            if(mensaje.equals("El tripulante no puede ser eliminado de la tripulación porque no se encuentra en ella."))
            {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            } else {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaTripulantes(actionEvent);
            }
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor seleccione todos los campos necesarios.");
        }
    }

    /**
     * Metodo para obtener los datos de un tripulacion de los TextField
     */
    public Tripulacion obtenerTripulacion() {
        String codigo = codigoText.getText();
        String nombreClave = nombreClaveText.getText();
        Tripulacion tripulacion = new Tripulacion(codigo, nombreClave);
        return tripulacion;
    }

    /**
     * Metodo para resetear los valores dem los TextField
     */
    public void resetearValores() {
        cargarListas();
        codigoText.setText("");
        nombreClaveText.setText("");
    }

    /**
     * Metodo para actualizar el TableView de las tripulaciones
     */
    public void cargarListas () {
        try {
        GestorTripulaciones gestorTripulaciones = new GestorTripulaciones();
        listaTripulaciones.getItems().clear();
        observableTripulaciones = FXCollections.observableArrayList();
        gestorTripulaciones.listarTripulaciones().forEach(tripulacion -> observableTripulaciones.addAll(tripulacion));
        observableTripulaciones = FXCollections.observableArrayList(gestorTripulaciones.listarTripulaciones());
        tCodigo.setCellValueFactory(new PropertyValueFactory<Tripulacion,String>("codigo"));
        tNombreClave.setCellValueFactory(new PropertyValueFactory<Tripulacion,String>("nombreClave"));
        listaTripulaciones.setItems(observableTripulaciones);
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
        GestorPersonas gestorPersonas = new GestorPersonas();
        observableTripulantes = FXCollections.observableArrayList(gestorPersonas.listarTripulantes());
        tripulacionCB.setItems(observableTripulaciones);
        tripulanteCB.setItems(observableTripulantes);
        Callback<ListView<Tripulacion>, ListCell<Tripulacion>> cellFactory = new Callback<>() {

            @Override
            public ListCell<Tripulacion> call(ListView<Tripulacion> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Tripulacion item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText("(" + item.getCodigo()+ ") " + item.getNombreClave());
                        }
                    }
                };
            }
        };

        tripulacionCB.setButtonCell(cellFactory.call(null));
        tripulacionCB.setCellFactory(cellFactory);

        Callback<ListView<Tripulante>, ListCell<Tripulante>> cellFactory1 = new Callback<>() {
            @Override
            public ListCell<Tripulante> call(ListView<Tripulante> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Tripulante item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getId()+ " - " + item.getNombre() + " " + item.getApellidos());
                        }
                    }
                };
            }
        };

        tripulanteCB.setButtonCell(cellFactory1.call(null));
        tripulanteCB.setCellFactory(cellFactory1);
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para actualizar el TableView de los tripulantes
     */
    public void cargarListaTripulantes(ActionEvent actionEvent) {
        try {
        GestorTripulaciones gestorTripulaciones = new GestorTripulaciones();
        Tripulacion tripulacionSeleccionada = (Tripulacion) tripulacionCB.getValue();
        listaTripulantes.getItems().clear();
        if (tripulacionSeleccionada == null) {
            listaTripulantes.setItems(FXCollections.observableArrayList());
        } else {
            observableTripulantes = FXCollections.observableArrayList();
            gestorTripulaciones.listarTripulantesDeTripulacion(tripulacionSeleccionada).forEach(tripulante -> observableTripulantes.addAll(tripulante));
            tId.setCellValueFactory(new PropertyValueFactory<Tripulante,String>("id"));
            tNombre.setCellValueFactory(new PropertyValueFactory<Tripulante,String>("nombre"));
            tApellidos.setCellValueFactory(new PropertyValueFactory<Tripulante,String>("apellidos"));
            tFechaNac.setCellValueFactory(new PropertyValueFactory<Tripulante, LocalDate>("fechaNacimiento"));
            tGenero.setCellValueFactory(new PropertyValueFactory<Tripulante,String>("genero"));
            tNacionalidad.setCellValueFactory(new PropertyValueFactory<Tripulante,String>("nacionalidad"));
            tCorreo.setCellValueFactory(new PropertyValueFactory<Tripulante,String>("correoElectronico"));
            listaTripulantes.setItems(observableTripulantes);
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor seleccione todos los campos necesarios.");
        }
    }

    /**
     * Metodo para mostrar una alerta al tripulante
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
     * Metodo para inicializar el ObservableList y el TableView
     */
    @FXML
    public void initialize() {
        try {
            cargarListas();
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
     * Metodo para salir de la aplicacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void salir(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }
}
