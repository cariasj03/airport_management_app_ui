package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Administrador;
import cr.ac.ucenfotec.entidades.Direccion;
import cr.ac.ucenfotec.entidades.Persona;
import cr.ac.ucenfotec.logica.GestorPersonas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 24/11/2022
 *
 * Esta clase se encarga de gestionar el formulario FXML de Administradores
 */

public class ControladorAdministrador {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField idText;
    public TextField nombreText;
    public TextField apellidosText;
    public TextField nacionalidadText;
    public DatePicker fechaNacDatePicker;
    @FXML
    public RadioButton femeninoRadio;
    @FXML
    public RadioButton masculinoRadio;
    @FXML
    public RadioButton otroRadio;
    public TextField correoText;
    public TextField provinciaText;
    public TextField cantonText;
    public TextField distritoText;
    public TextField detalleText;
    public PasswordField passField;
    @FXML
    TableView<Administrador> listaAdministradores;
    @FXML
    TableColumn<Administrador, String> tId;
    @FXML
    TableColumn<Administrador, String> tNombre;
    @FXML
    TableColumn<Administrador, String> tApellidos;
    @FXML
    TableColumn<Administrador, LocalDate> tFechaNac;
    @FXML
    TableColumn<Administrador, String> tGenero;
    @FXML
    TableColumn<Administrador, String> tNacionalidad;
    @FXML
    TableColumn<Administrador, String> tCorreo;
    @FXML
    public ObservableList<Administrador> observableAdministradores;
    private ControladorInicio controladorInicio = new ControladorInicio();
    private Persona personaSesion;

    //Getter y setter para la persona en sesion
    public Persona getPersonaSesion() {
        return personaSesion;
    }
    public void setPersonaSesion(Persona personaSesion) {
        this.personaSesion = personaSesion;
    }


    /**
     * Metodo para registrar un administrador
     *
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void registrar(ActionEvent actionEvent) {
        try {
            if (idText.getText().isEmpty() || nombreText.getText().isEmpty() || apellidosText.getText().isEmpty() || nacionalidadText.getText().isEmpty() || correoText.getText().isEmpty() || provinciaText.getText().isEmpty() || cantonText.getText().isEmpty() || distritoText.getText().isEmpty() || detalleText.getText().isEmpty() || fechaNacDatePicker.getValue() == null || obtenerGenero().equals("N") || passField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin llenar", "Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
            } else {
                GestorPersonas gestorPersonas = new GestorPersonas();
                Administrador administrador = obtenerAdministrador();
                String mensaje = gestorPersonas.insertarAdministrador(administrador);
                if (mensaje.equals("El administrador fue creado con éxito.")) {
                    showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                    resetearValores();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
                }
            }
        } catch (DateTimeParseException dfe) {
            showAlert(Alert.AlertType.ERROR,"La fecha no tiene un formato adecuado.","La fecha no tiene un formato adecuado.\nPor favor digítela con el formato adecuado.");
        }
    }

    /**
     * Metodo para obtener los valores de un administrador en los TextField
     *
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void dobleClick(MouseEvent mouseEvent) {
        try {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            Administrador administrador = (Administrador) listaAdministradores.getSelectionModel().getSelectedItem();
            idText.setText(administrador.getId());
            nombreText.setText(administrador.getNombre());
            apellidosText.setText(administrador.getApellidos());
            nacionalidadText.setText(administrador.getNacionalidad());
            fechaNacDatePicker.setValue(administrador.getFechaNacimiento());
            correoText.setText(administrador.getCorreoElectronico());
            provinciaText.setText(administrador.getDireccion().getProvincia());
            cantonText.setText(administrador.getDireccion().getCanton());
            distritoText.setText(administrador.getDireccion().getDistrito());
            detalleText.setText(administrador.getDireccion().getDetalleDireccion());
            passField.setText(administrador.getContrasena());
            if (administrador.getGenero().equals("F")) {
                femeninoRadio.setSelected(true);
            } else {
                if (administrador.getGenero().equals("M")) {
                    masculinoRadio.setSelected(true);
                } else {
                    if (administrador.getGenero().equals("O")) {
                        otroRadio.setSelected(true);
                    }
                }
            }
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
        }
    }

    /**
     * Metodo para actualizar un administrador
     *
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarAdministrador(ActionEvent actionEvent) {
        try {
        if (idText.getText().isEmpty() || nombreText.getText().isEmpty() || apellidosText.getText().isEmpty() || nacionalidadText.getText().isEmpty() || correoText.getText().isEmpty() || provinciaText.getText().isEmpty() || cantonText.getText().isEmpty() || distritoText.getText().isEmpty() || detalleText.getText().isEmpty() || fechaNacDatePicker.getValue() == null || obtenerGenero().equals("N") || passField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin llenar", "Hay campos obligatorios sin llenar.\nPor favor llene todos los campos\nobligatorios.");
        } else {
            GestorPersonas gestorPersonas = new GestorPersonas();
            Administrador administrador = obtenerAdministrador();
            String mensaje = gestorPersonas.actualizarAdministrador(administrador);
            if (mensaje.equals("El administrador fue actualizado con éxito.")) {
                showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                cargarListaAdministradores();
            } else {
                showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
            }
        }
        } catch (DateTimeParseException dfe) {
            showAlert(Alert.AlertType.ERROR,"La fecha no tiene un formato adecuado.","La fecha no tiene un formato adecuado.\nPor favor digítela con el formato adecuado.");
        }
    }

    /**
     * Metodo para eliminar un administrador
     *
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarAdministrador(ActionEvent actionEvent) {
        try {
        if (listaAdministradores.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, "No ha seleccionado ningún administrador.", "No ha seleccionado ningún administrador.\nPor favor seleccione un administrador para eliminar.");
        } else {
            GestorPersonas gestorPersonas = new GestorPersonas();
            Administrador administrador = (Administrador) listaAdministradores.getSelectionModel().getSelectedItem();
            String mensaje = gestorPersonas.eliminarAdministrador(administrador);
            if (mensaje.equals("El administrador fue eliminado con éxito.")) {
                showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                cargarListaAdministradores();
            } else {
                showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
            }
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
        }
    }

    /**
     * Metodo para obtener los datos de un administrador de los TextField
     */
    public Administrador obtenerAdministrador() {
        String id = idText.getText();
        String nombre = nombreText.getText();
        String apellidos = apellidosText.getText();
        String nacionalidad = nacionalidadText.getText();
        LocalDate fechaNacimiento = fechaNacDatePicker.getValue();
        LocalDate hoy = LocalDate.now();
        Period periodo = Period.between(fechaNacimiento, LocalDate.now());
        int edad = periodo.getYears();
        String genero = obtenerGenero();
        String correo = correoText.getText();
        String provincia = provinciaText.getText();
        String canton = cantonText.getText();
        String distrito = distritoText.getText();
        String detalle = detalleText.getText();
        String contrasena = passField.getText();
        Direccion direccion = new Direccion(provincia, canton, distrito, detalle);
        Administrador administrador = new Administrador(id, nombre, apellidos, nacionalidad, fechaNacimiento, edad, genero, correo, direccion, contrasena);
        return administrador;
    }

    /**
     * Metodo para actualizar el TableView de los administradores
     */
    public void cargarListaAdministradores () {
        try {
        GestorPersonas gestorPersonas = new GestorPersonas();
        listaAdministradores.getItems().clear();
        observableAdministradores = FXCollections.observableArrayList();
        gestorPersonas.listarAdministradores().forEach(administrador -> observableAdministradores.addAll(administrador));
        tId.setCellValueFactory(new PropertyValueFactory<Administrador,String>("id"));
        tNombre.setCellValueFactory(new PropertyValueFactory<Administrador,String>("nombre"));
        tApellidos.setCellValueFactory(new PropertyValueFactory<Administrador,String>("apellidos"));
        tFechaNac.setCellValueFactory(new PropertyValueFactory<Administrador, LocalDate>("fechaNacimiento"));
        tGenero.setCellValueFactory(new PropertyValueFactory<Administrador,String>("genero"));
        tNacionalidad.setCellValueFactory(new PropertyValueFactory<Administrador,String>("nacionalidad"));
        tCorreo.setCellValueFactory(new PropertyValueFactory<Administrador,String>("correoElectronico"));
        listaAdministradores.setItems(observableAdministradores);
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para resetear los valores dem los TextField
     */
    public void resetearValores() {
        cargarListaAdministradores ();
        idText.setText("");
        nombreText.setText("");
        apellidosText.setText("");
        nacionalidadText.setText("");
        fechaNacDatePicker.setValue(null);
        femeninoRadio.setSelected(false);
        masculinoRadio.setSelected(false);
        otroRadio.setSelected(false);
        correoText.setText("");
        provinciaText.setText("");
        cantonText.setText("");
        distritoText.setText("");
        detalleText.setText("");
        passField.setText("");
    }

    /**
     * Metodo para obtener el genero del administrador de los RadioButton
     */
    public String obtenerGenero (){
        String genero = "";
        if (femeninoRadio.isSelected()) {
            genero = "F";
        } else {
            if (masculinoRadio.isSelected()) {
                genero = "M";
            } else {
                if(otroRadio.isSelected()) {
                    genero = "O";
                } else {
                    if (!femeninoRadio.isSelected() && !masculinoRadio.isSelected() && !otroRadio.isSelected()) {
                        genero = "N";
                    }
                }
            }
        }
        return genero;
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
     * Metodo para inicializar el ObservableList y el TableView
     */
    @FXML
    public void initialize()
    {
        try {
            cargarListaAdministradores ();
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
