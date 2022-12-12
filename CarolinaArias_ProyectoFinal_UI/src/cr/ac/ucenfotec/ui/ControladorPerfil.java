package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Direccion;
import cr.ac.ucenfotec.entidades.Persona;
import cr.ac.ucenfotec.logica.GestorPersonas;
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
import javafx.stage.Stage;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 24/11/2022
 *
 * Esta clase se encarga de gestionar el formulario FXML de Perfil
 */

public class ControladorPerfil {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public Label idLabel;
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
    private Persona personaSesion;

    //Getter y setter para la persona en sesion
    public Persona getPersonaSesion() {
        return personaSesion;
    }
    public void setPersonaSesion(Persona personaSesion) {
        this.personaSesion = personaSesion;
    }

    /**
     * Metodo para obtener los valores de la persona en la sesion en los TextField
     */
    public void cargarDatosPersona() {
        try {
            GestorPersonas gestorPersonas = new GestorPersonas();
            Persona persona = gestorPersonas.buscarPersona(personaSesion.getId());
                idLabel.setText(persona.getId());
                nombreText.setText(persona.getNombre());
                apellidosText.setText(persona.getApellidos());
                nacionalidadText.setText(persona.getNacionalidad());
                fechaNacDatePicker.setValue(persona.getFechaNacimiento());
                correoText.setText(persona.getCorreoElectronico());
                provinciaText.setText(persona.getDireccion().getProvincia());
                cantonText.setText(persona.getDireccion().getCanton());
                distritoText.setText(persona.getDireccion().getDistrito());
                detalleText.setText(persona.getDireccion().getDetalleDireccion());
                passField.setText(persona.getContrasena());
                if (persona.getGenero().equals("F")) {
                    femeninoRadio.setSelected(true);
                } else {
                    if (persona.getGenero().equals("M")) {
                        masculinoRadio.setSelected(true);
                    } else {
                        if (persona.getGenero().equals("O")) {
                            otroRadio.setSelected(true);
                        }
                    }
                }
            } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para actualizar los datos de la persona en la sesion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarDatos(ActionEvent actionEvent) {
        try {
            if (nombreText.getText().isEmpty() || apellidosText.getText().isEmpty() || nacionalidadText.getText().isEmpty() || correoText.getText().isEmpty() || provinciaText.getText().isEmpty() || cantonText.getText().isEmpty() || distritoText.getText().isEmpty() || detalleText.getText().isEmpty() || fechaNacDatePicker.getValue() == null || obtenerGenero().equals("N") || passField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin llenar", "Hay campos obligatorios sin llenar.\nPor favor llene todos los campos\nobligatorios.");
            } else {
                GestorPersonas gestorPersonas = new GestorPersonas();
                Persona persona = obtenerPersona();
                String mensaje = gestorPersonas.actualizarPersona(persona);
                if (mensaje.equals("Los datos del perfil fueron actualizados con éxito.")) {
                    showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                    cargarDatosPersona();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
                }
            }
        } catch (DateTimeParseException dfe) {
            showAlert(Alert.AlertType.ERROR,"La fecha no tiene un formato adecuado.","La fecha no tiene un formato adecuado.\nPor favor digítela con el formato adecuado.");
        }
    }

    /**
     * Metodo para obtener los datos de la persona de los TextField
     */
    public Persona obtenerPersona() {
        String id = idLabel.getText();
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
        Persona persona = new Persona(id, nombre, apellidos, nacionalidad, fechaNacimiento, edad, genero, correo, direccion, contrasena);
        return persona;
    }

    /**
     * Metodo para obtener el genero del persona de los RadioButton
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
     * Metodo para ir a la pantalla de inicio para administradores
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void inicioUsuario (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InicioUsuario.fxml"));
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
    public void tripulacionAdministrador (ActionEvent actionEvent) throws IOException {
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
     * Metodo para ir a la pantalla de tripulaciones
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void tripulacionUsuario (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TripulacionUsuario.fxml"));
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
    public void vueloAdministrador (ActionEvent actionEvent) throws IOException {
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
     * Metodo para ir a la pantalla de vuelos
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void vueloUsuario (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("VueloUsuario.fxml"));
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

