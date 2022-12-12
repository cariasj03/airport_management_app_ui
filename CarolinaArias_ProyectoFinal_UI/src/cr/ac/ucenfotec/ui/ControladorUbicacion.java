package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Persona;
import cr.ac.ucenfotec.entidades.Ubicacion;
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
import java.io.IOException;

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
    private Persona personaSesion;

    //Getter y setter para la persona en sesion
    public Persona getPersonaSesion() {
        return personaSesion;
    }
    public void setPersonaSesion(Persona personaSesion) {
        this.personaSesion = personaSesion;
    }

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
                GestorUbicaciones gestorUbicaciones = new GestorUbicaciones();
                Ubicacion ubicacion = obtenerUbicacion();
                String mensaje = gestorUbicaciones.insertarUbicacion(ubicacion);
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
        try {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            Ubicacion ubicacion = (Ubicacion) listaUbicaciones.getSelectionModel().getSelectedItem();
            codigoText.setText(ubicacion.getCodigo());
            nivelText.setText(String.valueOf(ubicacion.getNivel()));
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
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
                GestorUbicaciones gestorUbicaciones = new GestorUbicaciones();
                Ubicacion ubicacion = obtenerUbicacion();
                String mensaje = gestorUbicaciones.actualizarUbicacion(ubicacion);
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
        try {
        GestorUbicaciones gestorUbicaciones = new GestorUbicaciones();
        Ubicacion ubicacion = new Ubicacion();
            if(listaUbicaciones.getSelectionModel().getSelectedItem() == null)
            {
                showAlert(Alert.AlertType.ERROR,"No ha seleccionado ninguna ubicación.","No ha seleccionado ninguna ubicación.\nPor favor seleccione una ubicación para eliminar.");
            } else {
                ubicacion = (Ubicacion) listaUbicaciones.getSelectionModel().getSelectedItem();
                if(gestorUbicaciones.tienePuertaAsignada(ubicacion)) {
                    showAlert(Alert.AlertType.ERROR,"La ubicación tiene puertas asociadas.","La ubicación tiene puertas asociadas.\nPor favor elimine las puertas asociadas primero.");
                } else {
                    String mensaje = gestorUbicaciones.eliminarUbicacion(ubicacion);
                    if(mensaje.equals("La ubicación fue eliminada con éxito."))
                    {
                        showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                        cargarListaUbicaciones();
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
        try {
        GestorUbicaciones gestorUbicaciones = new GestorUbicaciones();
        listaUbicaciones.getItems().clear();
        observableUbicaciones = FXCollections.observableArrayList();
        gestorUbicaciones.listarUbicaciones().forEach(ubicacion -> observableUbicaciones.addAll(ubicacion));
        tCodigo.setCellValueFactory(new PropertyValueFactory<Ubicacion,String>("codigo"));
        tNivel.setCellValueFactory(new PropertyValueFactory<Ubicacion,Integer>("nivel"));
        listaUbicaciones.setItems(observableUbicaciones);
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
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
