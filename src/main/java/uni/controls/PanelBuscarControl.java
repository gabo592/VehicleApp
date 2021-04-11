/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.controls;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.JOptionPane;
import uni.implement.VehicleDaoImplement;
import uni.panels.PanelBuscar;
import uni.pojo.Vehicle;
import uni.observable.SujetoVehiculo;
import uni.observable.Observador;
import uni.views.DialogoCrear;
import uni.vehicleapp.VehicleApp;
import uni.views.DialogoActualizar;

/**
 *
 * @author gabri
 */
public final class PanelBuscarControl implements SujetoVehiculo {
    private List<Observador> observadores = new ArrayList<>();
    private DialogoActualizar dialogoActualizar;
    private DialogoCrear dialogoCrear;
    private static PanelBuscar panelBuscar;
    private List<Vehicle> vehiculos;
    private DefaultTableModel modeloTabla;
    private TableRowSorter tableRowSorter;
    private JTable tabla;
    private VehicleDaoImplement vDao;
    private VehicleApp app;
    private final static String nombreColumnas[] = new String[]{
        "ID", "Stock Number", "Año", "Marca", "Modelo", "Estilo", "Vin", "Color Interior", "Color Exterior", "Millas", "Precio", "Transmisión", "Motor", "Imagen", "Estado"
    };

    public PanelBuscarControl(PanelBuscar buscar) {
        PanelBuscarControl.panelBuscar = buscar;
        vDao = new VehicleDaoImplement();
        
        try {
            cargarTabla();
        } catch (IOException ex) {
            Logger.getLogger(PanelBuscarControl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Asignación de eventos a los botones
        buscar.getBotonNuevo().addActionListener((ActionEvent e) -> {
            agregar();
        });
        
        buscar.getBotonActualizar().addActionListener((ActionEvent e) -> {
            modificar();
        });
        
        buscar.getBotonEliminar().addActionListener((ActionEvent e) -> {
            eliminar();
        });
    }
    
    public void cargarTabla() throws IOException {
        vehiculos = vDao.leer();
        if (vehiculos.isEmpty()) {
            panelBuscar.getTablaVehiculos().setModel(new DefaultTableModel());
            return;
        }
        
        /*
        Las filas son la cantidad de vehículos existentes en el archivo.
        Las columnas son la cantidad total de atributos de cada objeto.
        */
        int filas = vehiculos.size();
        int columnas = nombreColumnas.length;
        String data[][] = new String[filas][columnas];
        for (int i = 0; i < filas; i++) {
            data[i][0] = Integer.toString(vehiculos.get(i).getId());
            data[i][1] = Integer.toString(vehiculos.get(i).getStockNumber());
            data[i][2] = Integer.toString(vehiculos.get(i).getAño());
            data[i][3] = vehiculos.get(i).getMarca();
            data[i][4] = vehiculos.get(i).getModelo();
            data[i][5] = vehiculos.get(i).getEtilo();
            data[i][6] = vehiculos.get(i).getVin();
            data[i][7] = vehiculos.get(i).getColorInterior();
            data[i][8] = vehiculos.get(i).getColorExterior();
            data[i][9] = vehiculos.get(i).getMiles();
            data[i][10] = Double.toString(vehiculos.get(i).getPrecio());
            data[i][11] = vehiculos.get(i).getTransmision().toString();
            data[i][12] = vehiculos.get(i).getMotor();
            data[i][13] = vehiculos.get(i).getImagen();
            data[i][14] = vehiculos.get(i).getEstado();
        }
        
        modeloTabla = new DefaultTableModel(data, nombreColumnas);
        panelBuscar.getTablaVehiculos().setModel(modeloTabla);
        filtrar(modeloTabla);
    }
    
    private void filtrar(DefaultTableModel modeloTabla) {
        panelBuscar.getFieldBuscarID().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                fieldBuscarKeyEvent(e, modeloTabla, panelBuscar.getFieldBuscarID(), 0);
            }
        });
        
        panelBuscar.getFieldBuscarEstado().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent ev) {
                fieldBuscarKeyEvent(ev, modeloTabla, panelBuscar.getFieldBuscarEstado(), 14);
            }
        });
    }
    
    private void fieldBuscarKeyEvent(KeyEvent e, DefaultTableModel modeloTabla, JTextField textField, int columna) {
        tableRowSorter = new TableRowSorter(modeloTabla);
        tableRowSorter.setRowFilter(RowFilter.regexFilter(textField.getText(), columna));
        panelBuscar.getTablaVehiculos().setRowSorter(tableRowSorter);
    }
    
    public TableModel getTableModel() {
        return panelBuscar.getTablaVehiculos().getModel();
    }

    @Override
    public void agregar() {
        dialogoCrear = new DialogoCrear(app, true);
        dialogoCrear.setVisible(true);
        try {
            cargarTabla();
            notificar();
        } catch (IOException ex) {
            Logger.getLogger(PanelBuscarControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void modificar() {
        tabla = panelBuscar.getTablaVehiculos();
        
        if (tabla.getSelectedRows().length > 1) {
            JOptionPane.showMessageDialog(null, "¡No se pueden modificar varios objetos a la vez!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (tabla.getSelectedRows().length <= 0) {
            JOptionPane.showMessageDialog(null, "¡Selecciona al menos uno!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        //De la fila seleccionada se toma en cuenta solo la primera columna (el ID)
        int id = Integer.parseInt((String)tabla.getModel().getValueAt(tabla.getSelectedRow(), 0));
        
        //Se busca al objeto con ese ID
        Vehicle vehicle;
        try {
            vehicle = vDao.buscarPorId(id);
            dialogoActualizar = new DialogoActualizar(app, true, vehicle);
            dialogoActualizar.setVisible(true);
            cargarTabla();
            notificar();
        } catch (IOException ex) {
            Logger.getLogger(PanelBuscarControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void eliminar() {
        tabla = panelBuscar.getTablaVehiculos();
        
        if (tabla.getSelectedRows().length <= 0) {
            JOptionPane.showMessageDialog(null, "Seleccione al menos un vehículo", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int opcion = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea eliminarlo/s?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (opcion == 1) {
            return;
        }
        
        int[] ids = tabla.getSelectedRows();
        try {
            //Para cada fila selccionada, se toma en cuenta solo la primer columna (el ID)
            for (int i : ids) {
                int id = Integer.parseInt((String)tabla.getValueAt(i, 0));
                Vehicle vehicle = vDao.buscarPorId(id);
                vDao.eliminar(vehicle);
            }
            cargarTabla();
            notificar();
        } catch (IOException e) {
        }
    }

    @Override
    public void notificar() {
        observadores.forEach(o -> {
            o.actualizar();
        });
    }

    @Override
    public void enlazarObservador(Observador o) {
        observadores.add(o);
    }
}
