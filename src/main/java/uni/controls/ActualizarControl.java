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
import uni.pojo.Vehicle.Transmission;
import uni.pojo.VehicleBoxModel;
import uni.panels.Actualizar;
import uni.implement.VehicleDaoImplement;

/**
 *
 * @author gabri
 */
public class ActualizarControl {
    
    private final Actualizar panel;
    private Transmission transmision;
    private Vehicle vehicle;
    private final VehicleDaoImplement vDao = new VehicleDaoImplement();
    private File file;
    private Gson gson;
    private JsonReader jsonReader;
    private Set<VehicleBoxModel> boxModel;
    private Set<String> colores;
    private Set<String> modelos;
    private DefaultComboBoxModel colorE;
    private DefaultComboBoxModel colorI;
    private DefaultComboBoxModel modelo;

    public ActualizarControl(Actualizar panel, Vehicle vehicle) {
        this.panel = panel;
        this.vehicle = vehicle;
        gson = new Gson();
        try {
            cargarCombos();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ActualizarControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        init(vehicle);
        panel.getBotonActualizar().addActionListener((ActionEvent e) -> {
            botonActualizarActionPerformed(e);
        });
    }
    
    private void init(Vehicle v) {
       panel.getTfStockNumber().setText("" + v.getStockNumber());
       panel.getComboMarcas().setSelectedItem(v.getMarca());
       panel.getTfEstilo().setText(v.getEtilo());
       panel.getComboColorE().setSelectedItem(v.getColorExterior());
       panel.getSpinnerKm().setValue((int) Double.parseDouble(v.getMiles()));
       panel.getRadioAuto().setSelected((v.getTransmision() == Vehicle.Transmission.AUTOMATICA));
       panel.getRadioManual().setSelected((v.getTransmision() == Vehicle.Transmission.MANUAL));
       panel.getjTextFieldImagen().setText(v.getImagen());
       panel.getSpinnerAño().setValue(v.getAño());
       panel.getComboModelo().setSelectedItem(v.getModelo());
       panel.getJfVin().setText(v.getVin());
       panel.getComboColorI().setSelectedItem(v.getColorInterior());
       panel.getSpinnerPrecio().setValue(v.getPrecio());
       panel.getTfMotor().setText(v.getMotor());
       panel.getComboEstado().setSelectedItem(v.getEstado());
    }
    
    private void cargarCombos() throws FileNotFoundException {
        file = new File(CrearControl.urlFile);
        jsonReader = new JsonReader(new BufferedReader(new FileReader(file)));
        Type tipo = new TypeToken<Set<VehicleBoxModel>>(){}.getType();
        boxModel = gson.fromJson(jsonReader, tipo);
        colores = boxModel.stream().map(VehicleBoxModel::getColor).sorted().collect(Collectors.toSet());
        modelos = boxModel.stream().map(VehicleBoxModel::getModelo).sorted().collect(Collectors.toSet());
        colorI = new DefaultComboBoxModel(colores.toArray());
        colorE = new DefaultComboBoxModel(colores.toArray());
        modelo = new DefaultComboBoxModel(modelos.toArray());
        panel.getComboColorE().setModel(colorE);
        panel.getComboColorI().setModel(colorI);
        panel.getComboModelo().setModel(modelo);
    }
    
    private void botonActualizarActionPerformed(ActionEvent e) {
        try {
            if (actualizar(vehicle)) {
                JOptionPane.showMessageDialog(null, "¡Se ha actualizado correctamente!", "Actualizar", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo actualizar el vehículo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            Logger.getLogger(ActualizarControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean actualizar(Vehicle vehicle) throws IOException {
        vehicle.setId(vehicle.getId());
        if (validarTextField(panel.getTfStockNumber())) {
            return false;
        }
        vehicle.setStockNumber(Integer.parseInt(panel.getTfStockNumber().getText()));
        vehicle.setMarca(panel.getComboMarcas().getSelectedItem().toString());
        if (validarTextField(panel.getTfEstilo())) {
            return false;
        }
        vehicle.setEstilo(panel.getTfEstilo().getText());
        vehicle.setColorExterior(panel.getComboColorE().getSelectedItem().toString());
        vehicle.setMiles(panel.getSpinnerKm().getValue().toString());
        vehicle.setTransmision(panel.getRadioAuto().isSelected() ? Vehicle.Transmission.AUTOMATICA : Vehicle.Transmission.MANUAL);
        vehicle.setAño(Integer.parseInt(panel.getSpinnerAño().getValue().toString()));
        vehicle.setModelo(panel.getComboModelo().getSelectedItem().toString());
        if (panel.getJfVin().getText() == null || panel.getJfVin().getText().equalsIgnoreCase("")) {
            return false;
        }
        vehicle.setVin(panel.getJfVin().getText());
        vehicle.setColorInterior(panel.getComboColorI().getSelectedItem().toString());
        vehicle.setPrecio(Float.parseFloat(panel.getSpinnerPrecio().getValue().toString()));
        if (validarTextField(panel.getTfMotor())) {
            return false;
        }
        vehicle.setMotor(panel.getTfMotor().getText());
        String ruta = panel.getjTextFieldImagen().getText();
        if (ruta.isEmpty()) {
            return false;
        }
        vehicle.setImagen(ruta);
        vehicle.setEstado(panel.getComboEstado().getSelectedItem().toString());
        vDao.actualizar(vehicle);
        return true;
    }
    
    private boolean validarTextField(JTextField textField) {
        return textField.getText() == null || textField.getText().equalsIgnoreCase(" ");
    }
}
