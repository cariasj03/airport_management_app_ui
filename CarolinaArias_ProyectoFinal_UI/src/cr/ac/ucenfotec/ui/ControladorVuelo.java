package cr.ac.ucenfotec.ui;

import cr.ac.ucenfotec.entidades.Aeropuerto;
import cr.ac.ucenfotec.entidades.Persona;
import cr.ac.ucenfotec.entidades.Vuelo;
import cr.ac.ucenfotec.logica.GestorAeropuertos;
import cr.ac.ucenfotec.logica.GestorVuelos;
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
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * @author Carolina Arias
 * @version 1.0
 * @since 24/11/2022
 *
 * Esta clase se encarga de gestionar el formulario FXML de Vuelos
 */
public class ControladorVuelo {
    private Stage stage;
    private Scene scene;
    private Parent root;
    public TextField numVueloText;
    public TextField horaSalidaText;
    public TextField horaLlegadaText;
    public ComboBox<String> estadoCB;
    @FXML
    public RadioButton salidaRadio;
    @FXML
    public RadioButton llegadaRadio;
    @FXML
    public TextField cantAsientosText;
    public TextField precioText;
    public TextField impuestoText;
    public ComboBox<Aeropuerto> aeropuertoOrigenCB;
    public ComboBox<Aeropuerto> aeropuertoDestinoCB;
    @FXML
    TableView<Vuelo> listaVuelos;
    @FXML
    TableColumn<Vuelo,Integer> tNumVuelo;
    @FXML
    TableColumn<Vuelo, LocalTime> tHoraSalida;
    @FXML
    TableColumn<Vuelo,LocalTime> tHoraLlegada;
    @FXML
    TableColumn<Vuelo, String> tEstado;
    @FXML
    TableColumn<Vuelo, String> tTipo;
    @FXML
    TableColumn<Vuelo,Integer> tCantAsientos;
    @FXML
    TableColumn<Vuelo,Double> tPrecio;
    @FXML
    TableColumn<Vuelo, Double> tImpuesto;
    @FXML
    public ObservableList<Vuelo> observableVuelos;
    @FXML
    public ObservableList<Aeropuerto> observableAeropuertos;
    private Persona personaSesion;

    //Getter y setter para la persona en sesion
    public Persona getPersonaSesion() {
        return personaSesion;
    }
    public void setPersonaSesion(Persona personaSesion) {
        this.personaSesion = personaSesion;
    }

    /**
     * Metodo para registrar un vuelo
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void registrar(ActionEvent actionEvent) {
        try {
            if(numVueloText.getText().isEmpty() || horaSalidaText.getText().isEmpty() || horaLlegadaText.getText().isEmpty() || estadoCB.getValue() == null || obtenerTipoVuelo().equals("N") || cantAsientosText.getText().isEmpty() || precioText.getText().isEmpty() || impuestoText.getText().isEmpty() || aeropuertoOrigenCB.getValue() == null || aeropuertoDestinoCB.getValue() == null)
            {
                showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos\nobligatorios.");
            } else {
                GestorVuelos gestorVuelos = new GestorVuelos();
                Vuelo vuelo = obtenerVuelo();
                String mensaje = gestorVuelos.insertarVuelo(vuelo);
                if(mensaje.equals("El vuelo fue registrado con éxito."))
                {
                    showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                    resetearValores();
                } else {
                    showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
                }
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"El campo sólo acepta valores numéricos.","Los campos:\n• Número de vuelo\n• Cantidad de asientos disponibles\n• Precio de asientos\n• Monto de impuesto\nSólo aceptan valores numéricos.");
        } catch (DateTimeParseException dfe) {
            showAlert(Alert.AlertType.ERROR,"La hora no tiene un formato adecuado.","La hora no tiene un formato adecuado.\nPor favor digítela en el siguiente formato HH:mm (24 horas).");
        }
    }

    /**
     * Metodo para obtener los valores de un vuelo en los TextField
     * @param mouseEvent es de tipo MouseEvent representa algun tipo de accion realizada por el mouse
     */
    public void dobleClick(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
            Vuelo vuelo = (Vuelo) listaVuelos.getSelectionModel().getSelectedItem();
            numVueloText.setText(String.valueOf(vuelo.getNumeroVuelo()));
            horaSalidaText.setText(String.valueOf(vuelo.getHoraSalida()));
            horaLlegadaText.setText(String.valueOf(vuelo.getHoraLlegada()));
            estadoCB.getSelectionModel().select(vuelo.getEstado());
            cantAsientosText.setText(String.valueOf(vuelo.getCantAsientosDiponibles()));
            precioText.setText(String.valueOf(vuelo.getPrecioAsientos()));
            impuestoText.setText(String.valueOf(vuelo.getMontoImpuesto()));
            aeropuertoOrigenCB.getSelectionModel().select(vuelo.getAeropuertoOrigen());
            aeropuertoDestinoCB.getSelectionModel().select(vuelo.getAeropuertoDestino());
            if(vuelo.getTipoVuelo().equals("Salida")) {
                salidaRadio.setSelected(true);
            } else {
                if(vuelo.getTipoVuelo().equals("Llegada")) {
                    llegadaRadio.setSelected(true);
                }
            }
        }
    }

    /**
     * Metodo para actualizar un vuelo
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void actualizarVuelo (ActionEvent actionEvent) {
        try {
            if(numVueloText.getText().isEmpty() || horaSalidaText.getText().isEmpty() || horaLlegadaText.getText().isEmpty() || estadoCB.getValue() == null || obtenerTipoVuelo().equals("N") || cantAsientosText.getText().isEmpty() || precioText.getText().isEmpty() || impuestoText.getText().isEmpty() || aeropuertoOrigenCB.getValue() == null || aeropuertoDestinoCB.getValue() == null)
            {
                showAlert(Alert.AlertType.ERROR,"Hay campos obligatorios sin llenar","Hay campos obligatorios sin llenar.\nPor favor llene todos los campos\nobligatorios.");
            } else {
                GestorVuelos gestorVuelos = new GestorVuelos();
                Vuelo vuelo = obtenerVuelo();
                String mensaje = gestorVuelos.actualizarVuelo(vuelo);
                if(mensaje.equals("El vuelo fue actualizado con éxito."))
                {
                    showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                    cargarListaVuelos();
                } else {
                    showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
                }
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"El campo sólo acepta valores numéricos.","Los campos:\n• Número de vuelo\n• Cantidad de asientos disponibles\n• Precio de asientos\n• Monto de impuesto\nSólo aceptan valores numéricos.");
        } catch (DateTimeParseException dfe) {
            showAlert(Alert.AlertType.ERROR,"La hora no tiene un formato adecuado.","La hora no tiene un formato adecuado.\nPor favor digítela en el siguiente formato HH:mm (24 horas).");
        }
    }

    /**
     * Metodo para eliminar un vuelo
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void eliminarVuelo(ActionEvent actionEvent) {
        try {
        if(listaVuelos.getSelectionModel().getSelectedItem() == null)
        {
            showAlert(Alert.AlertType.ERROR,"No ha seleccionado ningún vuelo.","No ha seleccionado ningún vuelo.\nPor favor seleccione un vuelo para eliminar.");
        } else {
            GestorVuelos gestorVuelos = new GestorVuelos();
            Vuelo vuelo = (Vuelo) listaVuelos.getSelectionModel().getSelectedItem();
            String mensaje = gestorVuelos.eliminarVuelo(vuelo);
            if(mensaje.equals("El vuelo fue eliminado con éxito."))
            {
                showAlert(Alert.AlertType.INFORMATION,"Atención.",mensaje);
                cargarListaVuelos();
            } else {
                showAlert(Alert.AlertType.ERROR,"Atención.",mensaje);
            }
        }
        } catch (NullPointerException e){
            showAlert(Alert.AlertType.ERROR,"Atención.","No se obtuvieron datos, por favor haga click en una línea que no esté vacía.");
        }
    }

    /**
     * Metodo para obtener los datos de un vuelo de los TextField
     */
    public Vuelo obtenerVuelo() {
        int numVuelo = Integer.parseInt(numVueloText.getText());
        LocalTime horaSalida = LocalTime.parse(horaSalidaText.getText());
        LocalTime horaLlegada = LocalTime.parse(horaLlegadaText.getText());
        String estado = estadoCB.getValue();
        String tipoVuelo = obtenerTipoVuelo();
        int cantAsientosDisponibles = Integer.parseInt(cantAsientosText.getText());
        Double precioAsientos = Double.parseDouble(precioText.getText());
        Double montoImpuesto = Double.parseDouble(impuestoText.getText());
        Aeropuerto aeropuertoOrigen = (Aeropuerto) aeropuertoOrigenCB.getValue();
        Aeropuerto aeropuertoDestino = (Aeropuerto) aeropuertoDestinoCB.getValue();
        Vuelo vuelo = new Vuelo(numVuelo, horaSalida, horaLlegada, estado, tipoVuelo, cantAsientosDisponibles, precioAsientos, montoImpuesto, aeropuertoOrigen, aeropuertoDestino);
        return vuelo;
    }

    /**
     * Metodo para obtener el tipo de vuelo de los RadioButton
     */
    public String obtenerTipoVuelo(){
        String tipoVuelo = "";
        if (salidaRadio.isSelected()) {
            tipoVuelo = "Salida";
        } else {
            if (llegadaRadio.isSelected()) {
                tipoVuelo = "Llegada";
            } else {
                    if (!salidaRadio.isSelected() && !llegadaRadio.isSelected()) {
                        tipoVuelo = "N";
                    }
                }
        }
        return tipoVuelo;
    }

    /**
     * Metodo para resetear los valores de los TextField
     */
    public void resetearValores() {
        cargarListaVuelos();
        numVueloText.setText("");
        horaSalidaText.setText("");
        horaLlegadaText.setText("");
        estadoCB.getSelectionModel().clearSelection();
        salidaRadio.setSelected(false);
        llegadaRadio.setSelected(false);
        cantAsientosText.setText("");
        precioText.setText("");
        impuestoText.setText("");
        aeropuertoOrigenCB.getSelectionModel().clearSelection();
        aeropuertoDestinoCB.getSelectionModel().clearSelection();
    }

    /**
     * Metodo para actualizar el TableView de los vuelos
     */
    public void cargarListaVuelos(){
        try {
        GestorVuelos gestorVuelos = new GestorVuelos();
        GestorAeropuertos gestorAeropuertos = new GestorAeropuertos();
        listaVuelos.getItems().clear();
        observableVuelos = FXCollections.observableArrayList();
        gestorVuelos.listarVuelos().forEach(vuelo -> observableVuelos.addAll(vuelo));
        tNumVuelo.setCellValueFactory(new PropertyValueFactory<Vuelo,Integer>("numeroVuelo"));
        tHoraSalida.setCellValueFactory(new PropertyValueFactory<Vuelo,LocalTime>("horaSalida"));
        tHoraLlegada.setCellValueFactory(new PropertyValueFactory<Vuelo,LocalTime>("horaLlegada"));
        tEstado.setCellValueFactory(new PropertyValueFactory<Vuelo, String>("estado"));
        tTipo.setCellValueFactory(new PropertyValueFactory<Vuelo, String>("tipoVuelo"));
        tCantAsientos.setCellValueFactory(new PropertyValueFactory<Vuelo,Integer>("cantAsientosDiponibles"));
        tPrecio.setCellValueFactory(new PropertyValueFactory<Vuelo,Double>("precioAsientos"));
        tImpuesto.setCellValueFactory(new PropertyValueFactory<Vuelo,Double>("montoImpuesto"));
        listaVuelos.setItems(observableVuelos);

        //ComboBox
        observableAeropuertos = FXCollections.observableArrayList(gestorAeropuertos.listarAeropuertos());
        aeropuertoOrigenCB.setItems(observableAeropuertos);
        aeropuertoDestinoCB.setItems(observableAeropuertos);
        estadoCB.setItems(FXCollections.observableArrayList("A tiempo", "Atrasado", "Llegó", "Cancelado", "Registrado", "En sala"));
        Callback<ListView<Aeropuerto>, ListCell<Aeropuerto>> cellFactory = new Callback<>() {

            @Override
            public ListCell<Aeropuerto> call(ListView<Aeropuerto> l) {
                return new ListCell<>() {

                    @Override
                    protected void updateItem(Aeropuerto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getNombre()+ " (" + item.getCodigo() + ")");
                        }
                    }
                };
            }
        };

        aeropuertoOrigenCB.setButtonCell(cellFactory.call(null));
        aeropuertoOrigenCB.setCellFactory(cellFactory);
        aeropuertoDestinoCB.setButtonCell(cellFactory.call(null));
        aeropuertoDestinoCB.setCellFactory(cellFactory);
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
            cargarListaVuelos();
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
     * Metodo para salir de la aplicacion
     * @param actionEvent es de tipo ActionEvent representa algun tipo de accion realizada
     */
    public void salir(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }
}
