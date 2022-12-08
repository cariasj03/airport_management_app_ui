package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Aerolinea;
import cr.ac.ucenfotec.entidades.Persona;
import cr.ac.ucenfotec.logica.GestorAerolineas;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 24/11/2022
 *
 * Esta clase se encarga de gestionar el formulario FXML de Aerolineas
 */
public class ControladorAerolinea {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField cedulaText;
    public TextField nombreText;
    public TextField nombreEmpresaText;
    @FXML
    TableView<Aerolinea> listaAerolineas;
    @FXML
    TableColumn<Aerolinea,Integer> tCedula;
    @FXML
    TableColumn<Aerolinea,String> tNombre;
    @FXML
    TableColumn<Aerolinea,String> tNombreEmpresa;
    @FXML
    private ImageView logoIV;
    @FXML
    public ObservableList<Aerolinea> observableAerolineas;
    File file;
    private Persona personaSesion;

    //Getter y setter para la persona en sesion
    public Persona getPersonaSesion() {
        return personaSesion;
    }
    public void setPersonaSesion(Persona personaSesion) {
        this.personaSesion = personaSesion;
    }

    /**
     * Metodo para registrar una aerolinea
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void registrar (ActionEvent actionEvent) throws IOException {
        try {
        if (cedulaText.getText().isEmpty() || nombreText.getText().isEmpty() || nombreEmpresaText.getText().isEmpty() || file == null) {
            showAlert(Alert.AlertType.ERROR, "Hay campos obligatorios sin llenar", "Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
        } else {
                GestorAerolineas gestorAerolineas = new GestorAerolineas();
                Aerolinea aerolinea = obtenerAerolinea();
                aerolinea.setLogo(new FileInputStream(file));
                String mensaje = gestorAerolineas.insertarAerolinea(aerolinea);
                if (mensaje.equals("La aerolínea fue registrada con éxito.")) {
                    showAlert(Alert.AlertType.INFORMATION, "Atención.", mensaje);
                    resetearValores();
                    cargarListaAerolineas();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Atención.", mensaje);
                }
            }
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para cargar la iamgen para el logo de una aerolinea
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void cargarImagen(ActionEvent actionEvent) throws IOException {
        try {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        fileChooser.getExtensionFilters().addAll(extensionFilterPNG, extensionFilterJPG);

        file = fileChooser.showOpenDialog(null);
        BufferedImage bufferedImage = ImageIO.read(file);
        Image image = SwingFXUtils.toFXImage(bufferedImage,null);
        logoIV.setImage(image);
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","No ha cargado ninguna imagen. Por favor cargue una imagen e intente de nuevo.");
        }
    }

    /**
     * Metodo para obtener los valores de una aerolinea en los TextField
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void dobleClick(MouseEvent mouseEvent) throws IOException {
        try {
        if (mouseEvent.getClickCount() == 2) {
            GestorAerolineas gestorAerolineas = new GestorAerolineas();
            Aerolinea aerolinea = listaAerolineas.getSelectionModel().getSelectedItem();
            cedulaText.setText(aerolinea.getCedulaJuridica());
            nombreText.setText(aerolinea.getNombreComercial());
            nombreEmpresaText.setText(aerolinea.getNombreEmpresaDuenna());
            String cedulaJuridica = aerolinea.getCedulaJuridica();
            Aerolinea aerolineaLogo = gestorAerolineas.buscarAerolinea(cedulaJuridica);
            Image logo = new Image(aerolineaLogo.getLogo());
            logoIV.setImage(logo);
        }
        } catch (NullPointerException e){
                showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
        }
    }

    /**
     * Metodo para mostrar el logo de una aerolinea en el ImageView
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void mostrarLogo(MouseEvent mouseEvent) throws IOException {
        try {
            if (mouseEvent.isPrimaryButtonDown()) {
                GestorAerolineas gestorAerolineas = new GestorAerolineas();
                Aerolinea aerolinea = listaAerolineas.getSelectionModel().getSelectedItem();
                String cedulaJuridica = aerolinea.getCedulaJuridica();
                Aerolinea aerolineaLogo = gestorAerolineas.buscarAerolinea(cedulaJuridica);
                Image logo = new Image(aerolineaLogo.getLogo());
                logoIV.setImage(logo);
            }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
        }
    }

    /**
     * Metodo para actualizar una aerolinea
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarAerolinea (ActionEvent actionEvent) throws IOException {
        try {
        if(cedulaText.getText().isEmpty() || nombreText.getText().isEmpty() || nombreEmpresaText.getText().isEmpty())
        {
            showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos obligatorios.");
        } else {
            GestorAerolineas gestorAerolineas = new GestorAerolineas();
            Aerolinea aerolinea = obtenerAerolinea();
            String mensaje = gestorAerolineas.actualizarAerolinea(aerolinea);
            if(mensaje.equals("La aerolínea fue actualizada con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaAerolineas ();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para actualizar el logo de una aerolinea
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarLogoAerolinea (ActionEvent actionEvent) throws IOException {
        try {
            if(listaAerolineas.getSelectionModel().getSelectedItem() == null)
            {
                showAlert(Alert.AlertType.ERROR,"No ha seleccionado ninguna aerolínea para actualizar el logo.","No ha seleccionado ninguna aerolínea.\nPor favor seleccione una aerolínea para actualizar el logo.");
            } else {
            cargarImagen(actionEvent);
            GestorAerolineas gestorAerolineas = new GestorAerolineas();
            Aerolinea aerolinea = listaAerolineas.getSelectionModel().getSelectedItem();
            aerolinea.setLogo(new FileInputStream(file));
            String mensaje = gestorAerolineas.actualizarLogoAerolinea(aerolinea);
            if(mensaje.equals("El logo de la aerolínea fue actualizado con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaAerolineas ();
                resetearValores();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
            }
        } catch (Exception e){
            System.out.println("Ha ocurrido un error, por favor inténtelo de nuevo.");
        }
    }

    /**
     * Metodo para eliminar una aerolinea
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarAerolinea (ActionEvent actionEvent) {
        try {
        if(listaAerolineas.getSelectionModel().getSelectedItem() == null)
        {
            showAlert(Alert.AlertType.ERROR,"No ha seleccionado ninguna aerolínea.","No ha seleccionado ninguna aerolínea.\nPor favor seleccione una aerolínea para eliminar.");
        } else {
            GestorAerolineas gestorAerolineas = new GestorAerolineas();
            Aerolinea aerolinea = (Aerolinea) listaAerolineas.getSelectionModel().getSelectedItem();
            String mensaje = gestorAerolineas.eliminarAerolinea(aerolinea);
            if(mensaje.equals("La aerolínea fue eliminada con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaAerolineas();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
        }
    }

    /**
     * Metodo para obtener los datos de una aerolinea de los TextField
     */
    public Aerolinea obtenerAerolinea() throws IOException {
        String cedulaJuridica = cedulaText.getText();
        String nombreComercial = nombreText.getText();
        String nombreEmpresaDuenna = nombreEmpresaText.getText();
        Aerolinea aerolinea = new Aerolinea(nombreComercial, cedulaJuridica, nombreEmpresaDuenna);
        return aerolinea;
    }

    /**
     * Metodo para resetear los valores de los TextField
     */
    public void resetearValores() {
        cargarListaAerolineas();
        cedulaText.setText("");
        nombreText.setText("");
        nombreEmpresaText.setText("");
        logoIV.setImage(null);
        file = null;
    }

    /**
     * Metodo para actualizar el TableView de las aerolineas
     */
    public void cargarListaAerolineas () {
        try {
        GestorAerolineas gestorAerolineas = new GestorAerolineas();
        listaAerolineas.getItems().clear();
        observableAerolineas = FXCollections.observableArrayList();
        gestorAerolineas.listarAerolineas().forEach(aerolinea -> observableAerolineas.addAll(aerolinea));
        observableAerolineas = FXCollections.observableArrayList(gestorAerolineas.listarAerolineas());
        tCedula.setCellValueFactory(new PropertyValueFactory<Aerolinea,Integer>("cedulaJuridica"));
        tNombre.setCellValueFactory(new PropertyValueFactory<Aerolinea,String>("nombreComercial"));
        tNombreEmpresa.setCellValueFactory(new PropertyValueFactory<Aerolinea,String>("nombreEmpresaDuenna"));
        listaAerolineas.setItems(observableAerolineas);
        } catch (Exception e){
            showAlert(Alert.AlertType.ERROR,"Error.","Ha ocurrido un error, por favor inténtelo de nuevo.");
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
     * Metodo para inicializar el ObservableList y el TableView
     */
    @FXML
    public void initialize() {
        try {
            cargarListaAerolineas ();
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
