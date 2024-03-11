package com.example.examend;


import com.example.examend.db.ConnectionProvider;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.ResourceBundle;

public class HelloController {

    @FXML
    private TextField nombre;

    @FXML
    private ComboBox<String> sexo;

    @FXML
    private TextField peso;

    @FXML
    private TextField edad;

    @FXML
    private TextField talla;

    @FXML
    private ComboBox<String> tipoActividad;

    @FXML
    private TextField obs;

    @FXML
    private Button guar;

    @FXML
    private Button desc;

    @FXML
    private Label mensajeLabel;

    @FXML
    private void initialize() {
        // Inicializar ComboBox con opciones para sexo y tipo de actividad
        sexo.getItems().addAll("Hombre", "Mujer");
        tipoActividad.getItems().addAll("Sedentario", "Moderado", "Activo", "Muy activo");

    }

    @FXML
    private void handleGuardar(ActionEvent event) {
        try {
            String nombreCliente = nombre.getText();
            String sexoCliente = sexo.getValue();
            double pesoCliente = Double.parseDouble(peso.getText());
            int edadCliente = Integer.parseInt(edad.getText());
            double tallaCliente = Double.parseDouble(talla.getText());
            String tipoActividadCliente = tipoActividad.getValue();

            double metabolismoBasal = calcularMetabolismoBasal(sexoCliente, pesoCliente, edadCliente, tallaCliente);
            double factorActividad = obtenerFactorActividad(sexoCliente, tipoActividadCliente);
            double gastoEnergeticoTotal = metabolismoBasal * factorActividad;

            // Mostrar resultado en el Label
            mensajeLabel.setStyle("-fx-background-color: #aaffaa;");
            mensajeLabel.setText("El cliente " + nombreCliente + " tiene un GER de " + metabolismoBasal + " y un GET de " + gastoEnergeticoTotal);
        } catch (NumberFormatException e) {
            mensajeLabel.setStyle("-fx-background-color: #ffaaaa;");
            mensajeLabel.setText("Ingrese valores numéricos en los campos requeridos.");
        }
    }

    private double calcularMetabolismoBasal(String sexo, double peso, int edad, double talla) {
        if ("Hombre".equals(sexo)) {
            return 66.473 + 13.751 * peso + 5.0033 * talla - 6.755 * edad;
        } else {
            return 655.0955 + 9.463 * peso + 1.8496 * talla - 4.6756 * edad;
        }
    }

    private double obtenerFactorActividad(String sexo, String tipoActividad) {
        if ("Hombre".equals(sexo)) {
            switch (tipoActividad) {
                case "Sedentario":
                    return 1.3;
                case "Moderado":
                    return 1.6;
                case "Activo":
                    return 1.7;
                case "Muy activo":
                    return 2.1;
            }
        } else {
            switch (tipoActividad) {
                case "Sedentario":
                    return 1.3;
                case "Moderado":
                    return 1.5;
                case "Activo":
                    return 1.6;
                case "Muy activo":
                    return 1.9;
            }
        }
        return 1.0; // Valor por defecto
    }

    @FXML
    public void descargarPDF( ActionEvent actionEvent ) {


        //Obtener Conexion del provider
        Connection c = null;
        try {
            c = ConnectionProvider.getConnection( );
        } catch ( SQLException e ) {
            throw new RuntimeException( e );
        }

        HashMap hm = new HashMap<>( );
        // hm.put( "annio", 1997 ); //parametro
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport( "ExamenDijrxml.jasper" , hm , c );
        } catch ( JRException e ) {
            throw new RuntimeException( e );
        }

        JRViewer viewer = new JRViewer( jasperPrint );

        JFrame frame = new JFrame( "Listado de Clientes" );
        frame.getContentPane( ).add( viewer );
        frame.setExtendedState( JFrame.MAXIMIZED_BOTH );
        frame.setVisible( true );

        System.out.print( "Done!" );


        /*  Exportar a PDF    */
        JRPdfExporter exp = new JRPdfExporter( );
        exp.setExporterInput( new SimpleExporterInput( jasperPrint ) );
        exp.setExporterOutput( new SimpleOutputStreamExporterOutput( "clientes.pdf" ) );
        exp.setConfiguration( new SimplePdfExporterConfiguration( ) );
        try {
            exp.exportReport( );
        } catch ( JRException e ) {
            throw new RuntimeException( e );
        }

        System.out.print( "Done!" );

    }
}
