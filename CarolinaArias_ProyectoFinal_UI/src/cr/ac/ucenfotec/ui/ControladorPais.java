package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Pais;
import cr.ac.ucenfotec.entidades.Persona;
import cr.ac.ucenfotec.logica.GestorPaises;
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
 * Esta clase se encarga de gestionar el formulario FXML de Paises
 */
public class ControladorPais {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField nombreText;
    public TextField codigoText;
    @FXML
    TableView<Pais> listaPaises;
    @FXML
    TableColumn<Pais,String> tCodigo;
    @FXML
    TableColumn<Pais,String> tNombre;
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
     * Metodo para registrar un pais
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void registrar (ActionEvent actionEvent) {
        try {
        if (codigoText.getText().isEmpty() || nombreText.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin llenar", "Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
        } else {
            if (codigoText.getText().length() != 2) {
                showAlert(Alert.AlertType.ERROR, "Revise el código del país.", "El código del país no tiene el formato adecuado.\nEl código debe estar conformado de exactamente 2 letras.");
            } else {
                GestorPaises gestorPaises = new GestorPaises();
                Pais pais = obtenerPais();
                String mensaje = gestorPaises.insertarPais(pais);
                if (mensaje.equals("El país fue registrado con éxito.")) {
                    showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                    resetearValores();
                    cargarListaPaises();
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
     * Metodo para obtener los valores de un pais en los TextField
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void dobleClick(MouseEvent mouseEvent) {
        try {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            Pais pais = (Pais) listaPaises.getSelectionModel().getSelectedItem();
            codigoText.setText(pais.getCodigo());
            nombreText.setText(pais.getNombre());
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
        }
    }

    /**
     * Metodo para actualizar un pais
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarPais (ActionEvent actionEvent) {
        try {
        if(nombreText.getText().isEmpty() || codigoText.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
        } else {
            GestorPaises gestorPaises = new GestorPaises();
            Pais pais = obtenerPais();
            String mensaje = gestorPaises.actualizarPais(pais);
            if(mensaje.equals("El país fue actualizado con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaPaises ();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para eliminar un pais
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarPais (ActionEvent actionEvent) {
        try {
        GestorPaises gestorPaises = new GestorPaises();
        Pais pais = new Pais();
        if(listaPaises.getSelectionModel().getSelectedItem() == null)
        {
            showAlert(Alert.AlertType.ERROR,"No ha seleccionado ningún país.","No ha seleccionado ningún país.\nPor favor seleccione un país para eliminar.");
        } else {
            pais = (Pais) listaPaises.getSelectionModel().getSelectedItem();
            if(gestorPaises.tieneAeropuertosAsignados(pais)) {
                showAlert(Alert.AlertType.ERROR,"El país tiene aeropuertos asociados.","El país tiene aeropuertos asociados.\nPor favor elimine los aeropuertos asociados primero.");
            } else {
                String mensaje = gestorPaises.eliminarPais(pais);
                if(mensaje.equals("El país fue eliminado con éxito."))
                {
                    showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                    cargarListaPaises();
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
     * Metodo para obtener los datos de un pais de los TextField
     */
    public Pais obtenerPais() {
        String codigo = codigoText.getText();
        String nombre = nombreText.getText();
        Pais pais = new Pais(codigo, nombre);
        return pais;
    }

    /**
     * Metodo para resetear los valores dem los TextField
     */
    public void resetearValores() {
        cargarListaPaises();
        nombreText.setText("");
        codigoText.setText("");
    }

    /**
     * Metodo para actualizar el TableView de los paiss
     */
    public void cargarListaPaises () {
        try {
        GestorPaises gestorPaises = new GestorPaises();
        listaPaises.getItems().clear();
        observablePaises = FXCollections.observableArrayList();
        gestorPaises.listarPaises().forEach(pais -> observablePaises.addAll(pais));
        observablePaises = FXCollections.observableArrayList(gestorPaises.listarPaises());
        tCodigo.setCellValueFactory(new PropertyValueFactory<Pais,String>("codigo"));
        tNombre.setCellValueFactory(new PropertyValueFactory<Pais,String>("nombre"));
        listaPaises.setItems(observablePaises);
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
            cargarListaPaises ();
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
