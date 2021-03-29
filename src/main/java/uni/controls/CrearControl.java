/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.controls;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import uni.panels.Crear;
import javax.swing.DefaultComboBoxModel;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import uni.pojo.VehicleBoxModel;
import uni.implement.VehicleDaoImplement;
import uni.pojo.Vehicle;
import uni.pojo.Vehicle.Transmission;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author gabri
 */
public class CrearControl {

    private final Gson gson;
    private final File file;
    private final Crear crear;
    private DefaultComboBoxModel colorI;
    private DefaultComboBoxModel colorE;
    private DefaultComboBoxModel modelo;
    private Set<VehicleBoxModel> vSubModelos;
    private Set<String> colores;
    private Set<String> modelos;
    private VehicleDaoImplement vehicleDaoImplement;
    private Vehicle vehicle;
    private Transmission transmision;
    private String ruta;
    private FileFilter filter;

    public CrearControl(Crear crear) {
        gson = new Gson();
        vehicleDaoImplement = new VehicleDaoImplement();
        filter = new FileNameExtensionFilter("Imágenes", "png", "jpg");
        this.crear = crear;
        file = new File("C:/Users/Sistemas-11/Desktop/VehicleApp/src/main/recursos/datosVehiculo.json");
        try {
            init();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CrearControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void init() throws FileNotFoundException {
        //Para leer el JSON se necesita de este objeto
        JsonReader jsonReader;
        jsonReader = new JsonReader(new BufferedReader(new FileReader(file)));

        /*
        Antes de leer el archivo JSON hay que especificarle a qué tipo va a convertir,
        si a una lista, a un set, etc.
         */
        Type tipo = new TypeToken<Set<VehicleBoxModel>>() {
        }.getType();

        /*
        Aquí se guardarán todos los objetos del JSON a un set de objetos JAVA (VehicleBoxModel)
         */
        vSubModelos = gson.fromJson(jsonReader, tipo);

        /*
        Se realiza un filtro de las propiedades (modelo y/o color)
        de la lista de objetos (vSubModelos) de la clase VehicleBoxModel
         */
        colores = vSubModelos.stream().map(VehicleBoxModel::getColor).sorted().collect(Collectors.toSet());
        modelos = vSubModelos.stream().map(VehicleBoxModel::getModelo).sorted().collect(Collectors.toSet());

        /*
        Una vez obtenido todos los colores y modelos de los distintos objetos
        se crea el modelo que irán en los combo box del panel Crear
         */
        colorI = new DefaultComboBoxModel(colores.toArray());
        colorE = new DefaultComboBoxModel(colores.toArray());
        modelo = new DefaultComboBoxModel(modelos.toArray());

        /*
        Se les pasan los modelos a sus respectivos combo box
         */
        crear.getComboModelo().setModel(modelo);
        crear.getComboColorE().setModel(colorI);
        crear.getComboColorI().setModel(colorE);

        botonImagenActionPerfomed();
        botonCrearActionPerformed();
        botonNuevoActionPerformed();

    }

    public void botonCrearActionPerformed() {
        crear.getBotonCrearV().addActionListener((ActionEvent e) -> {
            try {
                if (crear()) {
                    JOptionPane.showMessageDialog(null, "Se ha creado con éxito", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo crear el objeto, intente de nuevo", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } catch (IOException ex) {
                Logger.getLogger(CrearControl.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void botonNuevoActionPerformed() {
        crear.getBotonNuevoV().addActionListener((ActionEvent e) -> {
            limpiar();
        });
    }
    
    private void botonImagenActionPerfomed() {
        crear.getBotonBuscarImagen().addActionListener((ActionEvent e) -> {
            BuscarImagen(e);
        });
    }
    
    private void BuscarImagen(ActionEvent e) {
        JFileChooser JfileChooser = new JFileChooser();
        JfileChooser.setFileFilter(filter);
        int Opcion;
        Opcion = JfileChooser.showOpenDialog(crear);
        if (Opcion == JFileChooser.CANCEL_OPTION) {
            return;
        }
        
        ruta = JfileChooser.getSelectedFile().getPath();
        crear.getjTextFieldImagen().setText(ruta);
    }

    private boolean crear() throws IOException {
        if (validarTextField(crear.getTfStockNumber())) {
            return false;
        }
        int stockNumber = Integer.parseInt(crear.getTfStockNumber().getText());

        String marca = crear.getComboMarcas().getSelectedItem().toString();

        if (validarTextField(crear.getTfEstilo())) {
            return false;
        }
        String estilo = crear.getTfEstilo().getText();

        String colorExterior = crear.getComboColorE().getSelectedItem().toString();

        String miles = crear.getSpinnerKm().getValue().toString();

        transmision = crear.getRadioAuto().isSelected() ? Transmission.AUTOMATICA : Transmission.MANUAL;

        int año = Integer.parseInt(crear.getSpinnerAño().getValue().toString());

        String modelo = crear.getComboModelo().getSelectedItem().toString();

        if (crear.getJfVin().getText() == null || crear.getJfVin().getText().equalsIgnoreCase("")) {
            return false;
        }
        String vin = crear.getJfVin().getText();

        String colorInterior = crear.getComboColorI().getSelectedItem().toString();

        float precio = Float.parseFloat(crear.getSpinnerPrecio().getValue().toString());

        if (validarTextField(crear.getTfMotor())) {
            return false;
        }
        String motor = crear.getTfMotor().getText();

        if (ruta.isEmpty()) {
            return false;
        }

        String estado = crear.getComboEstado().getSelectedItem().toString();

        vehicle = new Vehicle(stockNumber, año, marca, modelo, estilo, vin, colorExterior, colorInterior, miles, precio, transmision, motor, ruta, estado);
        vehicleDaoImplement.crear(vehicle);
        limpiar();

        return true;
    }

    private boolean validarTextField(JTextField textField) {
        return textField.getText() == null || textField.getText().equalsIgnoreCase(" ");
    }

    private void limpiar() {
        crear.getTfStockNumber().setText("");
        crear.getTfEstilo().setText("");
        crear.getSpinnerKm().setValue(0);
        crear.getSpinnerAño().setValue(0);
        crear.getJfVin().setText("");
        crear.getSpinnerPrecio().setValue(0);
        crear.getTfMotor().setText("");
        crear.getjTextFieldImagen().setText("");
    }
}
