/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.controls;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import uni.pojo.Vehicle;
import uni.pojo.VehicleBoxModel;
import uni.panels.PanelActualizar;
import uni.implement.VehicleDaoImplement;

/**
 *
 * @author gabri
 */
public class PanelActualizarControl {

    private final PanelActualizar panelActualizar;
    private Vehicle vehicle;
    private final VehicleDaoImplement vDao = new VehicleDaoImplement();
    private File file;
    private Gson gson;
    private JsonReader jsonReader;
    private Set<VehicleBoxModel> boxModel;
    private Set<String> colores;
    private Set<String> modelos;
    private DefaultComboBoxModel colorEModel;
    private DefaultComboBoxModel colorIModel;
    private DefaultComboBoxModel modeloVehiculoModel;

    public PanelActualizarControl(PanelActualizar panel, Vehicle vehicle) {
        this.panelActualizar = panel;
        this.vehicle = vehicle;
        gson = new Gson();
        try {
            cargarCombos();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PanelActualizarControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        init(vehicle);
        panel.getBotonActualizar().addActionListener((ActionEvent e) -> {
            botonActualizarActionPerformed(e);
        });
    }

    //Es necesario cargar el formulario con la información del vehículo seleccionado
    private void init(Vehicle v) {
        /*
        ADVERTENCIA: primero se tienen que cargar los JComboBox con sus respectivos modelos
        antes de establecerles su valor seleccionado (es el que trae el objeto vehículo consigo).
        Dicha acción está contenida en el constructor de esta clase
        */
        panelActualizar.getTfStockNumber().setText("" + v.getStockNumber());
        panelActualizar.getComboMarcas().setSelectedItem(v.getMarca());
        panelActualizar.getTfEstilo().setText(v.getEtilo());
        panelActualizar.getComboColorE().setSelectedItem(v.getColorExterior());
        panelActualizar.getSpinnerKm().setValue((int) Double.parseDouble(v.getMiles()));
        panelActualizar.getRadioAuto().setSelected((v.getTransmision() == Vehicle.Transmission.AUTOMATICA));
        panelActualizar.getRadioManual().setSelected((v.getTransmision() == Vehicle.Transmission.MANUAL));
        panelActualizar.getjTextFieldImagen().setText(v.getImagen());
        panelActualizar.getSpinnerAño().setValue(v.getAño());
        panelActualizar.getComboModelo().setSelectedItem(v.getModelo());
        panelActualizar.getJfVin().setText(v.getVin());
        panelActualizar.getComboColorI().setSelectedItem(v.getColorInterior());
        panelActualizar.getSpinnerPrecio().setValue(v.getPrecio());
        panelActualizar.getTfMotor().setText(v.getMotor());
        panelActualizar.getComboEstado().setSelectedItem(v.getEstado());
    }

    private void cargarCombos() throws FileNotFoundException {
        /*
        Se obtiene el JSON para cargar la información
        de los combobox utilizando la misma URL en el otro panel
         */
        file = new File(PanelCrearControl.urlFile);
        jsonReader = new JsonReader(new BufferedReader(new FileReader(file)));

        /*
        Se prepara el tipo de estructura de datos al que
        será convertido el JSON
         */
        Type tipo = new TypeToken<Set<VehicleBoxModel>>() {
        }.getType();
        boxModel = gson.fromJson(jsonReader, tipo);

        /*
        Se realiza un map del Set que se obtuvo del JSON escogiendo específicamente
        lo que se necesita (si los colores o los modelos)
         */
        colores = boxModel.stream().map(VehicleBoxModel::getColor).sorted().collect(Collectors.toSet());
        modelos = boxModel.stream().map(VehicleBoxModel::getModelo).sorted().collect(Collectors.toSet());

        /*
        Al ser Set se deben convertir a arreglos para establecer
        los modelos de los combobox
         */
        colorIModel = new DefaultComboBoxModel(colores.toArray());
        colorEModel = new DefaultComboBoxModel(colores.toArray());
        modeloVehiculoModel = new DefaultComboBoxModel(modelos.toArray());

        /*
        Se asignan los modelos a sus respectivos combobox
         */
        panelActualizar.getComboColorE().setModel(colorEModel);
        panelActualizar.getComboColorI().setModel(colorIModel);
        panelActualizar.getComboModelo().setModel(modeloVehiculoModel);
    }

    private void botonActualizarActionPerformed(ActionEvent e) {
        try {
            if (actualizar(vehicle)) {
                JOptionPane.showMessageDialog(null, "¡Se ha actualizado correctamente!", "Actualizar", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el vehículo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            Logger.getLogger(PanelActualizarControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean actualizar(Vehicle vehicle) throws IOException {
        /*
        Se obtiene un vehículo como parámetro y se va modificando
        este mismo con los nuevos datos establecidos en el formulario
         */
        vehicle.setId(vehicle.getId());

        if (validarTextField(panelActualizar.getTfStockNumber())) {
            return false;
        }
        vehicle.setStockNumber(Integer.parseInt(panelActualizar.getTfStockNumber().getText()));

        vehicle.setMarca(panelActualizar.getComboMarcas().getSelectedItem().toString());

        if (validarTextField(panelActualizar.getTfEstilo())) {
            return false;
        }
        vehicle.setEstilo(panelActualizar.getTfEstilo().getText());

        vehicle.setColorExterior(panelActualizar.getComboColorE().getSelectedItem().toString());

        vehicle.setMiles(panelActualizar.getSpinnerKm().getValue().toString());

        vehicle.setTransmision(panelActualizar.getRadioAuto().isSelected() ? Vehicle.Transmission.AUTOMATICA : Vehicle.Transmission.MANUAL);

        vehicle.setAño(Integer.parseInt(panelActualizar.getSpinnerAño().getValue().toString()));

        vehicle.setModelo(panelActualizar.getComboModelo().getSelectedItem().toString());

        if (panelActualizar.getJfVin().getText() == null || panelActualizar.getJfVin().getText().equalsIgnoreCase("")) {
            return false;
        }
        vehicle.setVin(panelActualizar.getJfVin().getText());

        vehicle.setColorInterior(panelActualizar.getComboColorI().getSelectedItem().toString());

        vehicle.setPrecio(Float.parseFloat(panelActualizar.getSpinnerPrecio().getValue().toString()));

        if (validarTextField(panelActualizar.getTfMotor())) {
            return false;
        }
        vehicle.setMotor(panelActualizar.getTfMotor().getText());

        String ruta = panelActualizar.getjTextFieldImagen().getText();
        if (ruta.isEmpty()) {
            return false;
        }
        vehicle.setImagen(ruta);

        vehicle.setEstado(panelActualizar.getComboEstado().getSelectedItem().toString());

        //Se manda el objeto ya actualizado a modificarse en el data
        vDao.actualizar(vehicle);

        return true;
    }

    private boolean validarTextField(JTextField textField) {
        return textField.getText() == null || textField.getText().equalsIgnoreCase(" ");
    }
}
