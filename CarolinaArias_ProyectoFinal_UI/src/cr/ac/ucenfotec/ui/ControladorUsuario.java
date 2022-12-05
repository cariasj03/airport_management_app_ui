package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Usuario;
import cr.ac.ucenfotec.entidades.Direccion;
import cr.ac.ucenfotec.logica.GestorPersonas;
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
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 03/12/2022
 *
 * Esta clase se encarga de gestionar el formulario FXML de Usuarios
 */

public class ControladorUsuario {
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
    TableView<Usuario> listaUsuarios;
    @FXML
    TableColumn<Usuario, String> tId;
    @FXML
    TableColumn<Usuario, String> tNombre;
    @FXML
    TableColumn<Usuario, String> tApellidos;
    @FXML
    TableColumn<Usuario, LocalDate> tFechaNac;
    @FXML
    TableColumn<Usuario, String> tGenero;
    @FXML
    TableColumn<Usuario, String> tNacionalidad;
    @FXML
    TableColumn<Usuario, String> tCorreo;
    @FXML
    public ObservableList<Usuario> observableUsuarios;

    /**
     * Metodo para registrar un usuario
     *
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void registrar(ActionEvent actionEvent) {
        try {
            if (idText.getText().isEmpty() || nombreText.getText().isEmpty() || apellidosText.getText().isEmpty() || nacionalidadText.getText().isEmpty() || correoText.getText().isEmpty() || provinciaText.getText().isEmpty() || cantonText.getText().isEmpty() || distritoText.getText().isEmpty() || detalleText.getText().isEmpty() || fechaNacDatePicker.getValue() == null || obtenerGenero().equals("N") || passField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin llenar", "Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
            } else {
                GestorPersonas gestorPersonas = new GestorPersonas();
                Usuario usuario = obtenerUsuario();
                String mensaje = gestorPersonas.insertarUsuario(usuario);
                if (mensaje.equals("El usuario fue creado con éxito.")) {
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
     * Metodo para obtener los valores de un usuario en los TextField
     *
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void dobleClick(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            Usuario usuario = (Usuario) listaUsuarios.getSelectionModel().getSelectedItem();
            idText.setText(usuario.getId());
            nombreText.setText(usuario.getNombre());
            apellidosText.setText(usuario.getApellidos());
            nacionalidadText.setText(usuario.getNacionalidad());
            fechaNacDatePicker.setValue(usuario.getFechaNacimiento());
            correoText.setText(usuario.getCorreoElectronico());
            provinciaText.setText(usuario.getDireccion().getProvincia());
            cantonText.setText(usuario.getDireccion().getCanton());
            distritoText.setText(usuario.getDireccion().getDistrito());
            detalleText.setText(usuario.getDireccion().getDetalleDireccion());
            passField.setText(usuario.getContrasena());
            if (usuario.getGenero().equals("F")) {
                femeninoRadio.setSelected(true);
            } else {
                if (usuario.getGenero().equals("M")) {
                    masculinoRadio.setSelected(true);
                } else {
                    if (usuario.getGenero().equals("O")) {
                        otroRadio.setSelected(true);
                    }
                }
            }
        }
    }

    /**
     * Metodo para actualizar un usuario
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarUsuario(ActionEvent actionEvent) {
        if (idText.getText().isEmpty() || nombreText.getText().isEmpty() || apellidosText.getText().isEmpty() || nacionalidadText.getText().isEmpty() || correoText.getText().isEmpty() || provinciaText.getText().isEmpty() || cantonText.getText().isEmpty() || distritoText.getText().isEmpty() || detalleText.getText().isEmpty() || fechaNacDatePicker.getValue() == null || obtenerGenero().equals("N") || passField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin llenar", "Hay campos obligatorios sin llenar.\nPor favor llene todos los campos\nobligatorios.");
        } else {
            GestorPersonas gestorPersonas = new GestorPersonas();
            Usuario usuario = obtenerUsuario();
            String mensaje = gestorPersonas.actualizarUsuario(usuario);
            if (mensaje.equals("El usuario fue actualizado con éxito.")) {
                showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                cargarListaUsuarios();
            } else {
                showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
            }
        }
    }

    /**
     * Metodo para eliminar un usuario
     *
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarUsuario(ActionEvent actionEvent) {
        if (listaUsuarios.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, "No ha seleccionado ningún usuario.", "No ha seleccionado ningún usuario.\nPor favor seleccione un usuario para eliminar.");
        } else {
            GestorPersonas gestorPersonas = new GestorPersonas();
            Usuario usuario = (Usuario) listaUsuarios.getSelectionModel().getSelectedItem();
            String mensaje = gestorPersonas.eliminarUsuario(usuario);
            if (mensaje.equals("El usuario fue eliminado con éxito.")) {
                showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                cargarListaUsuarios();
            } else {
                showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
            }
        }
    }

    /**
     * Metodo para obtener los datos de un usuario de los TextField
     */
    public Usuario obtenerUsuario() {
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
        Usuario usuario = new Usuario(id, nombre, apellidos, nacionalidad, fechaNacimiento, edad, genero, correo, direccion, contrasena);

        return usuario;
    }

    /**
     * Metodo para actualizar el TableView de los usuarios
     */
    public void cargarListaUsuarios () {
        GestorPersonas gestorPersonas = new GestorPersonas();
        listaUsuarios.getItems().clear();
        observableUsuarios = FXCollections.observableArrayList();
        gestorPersonas.listarUsuarios().forEach(usuario -> observableUsuarios.addAll(usuario));
        tId.setCellValueFactory(new PropertyValueFactory<Usuario,String>("id"));
        tNombre.setCellValueFactory(new PropertyValueFactory<Usuario,String>("nombre"));
        tApellidos.setCellValueFactory(new PropertyValueFactory<Usuario,String>("apellidos"));
        tFechaNac.setCellValueFactory(new PropertyValueFactory<Usuario, LocalDate>("fechaNacimiento"));
        tGenero.setCellValueFactory(new PropertyValueFactory<Usuario,String>("genero"));
        tNacionalidad.setCellValueFactory(new PropertyValueFactory<Usuario,String>("nacionalidad"));
        tCorreo.setCellValueFactory(new PropertyValueFactory<Usuario,String>("correoElectronico"));
        listaUsuarios.setItems(observableUsuarios);
    }

    /**
     * Metodo para resetear los valores dem los TextField
     */
    public void resetearValores() {
        cargarListaUsuarios ();
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
     * Metodo para obtener el genero del usuario de los RadioButton
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
            cargarListaUsuarios ();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Pais.fxml"));
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
     * Metodo para ir a la pantalla de puertas
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void puertas (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Puerta.fxml"));
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
