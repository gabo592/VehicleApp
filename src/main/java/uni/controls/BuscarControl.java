/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.controls;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import uni.implement.VehicleDaoImplement;
import uni.panels.Buscar;

/**
 *
 * @author gabri
 */
public class BuscarControl {
    private final Buscar buscar;
    private DefaultTableModel defaultTableModel;
    private VehicleDaoImplement vDao;
    private final static String nombreColumnas[] = new String[]{
        "ID", "Stock Number", "Año", "Marca", "Modelo", "Estilo", "Vin", "Color Interior", "Color Exterior", "Millas", "Precio", "Transmisión", "Motor", "Imagen", "Estado"
    };

    public BuscarControl(Buscar buscar) {
        this.buscar = buscar;
        vDao = new VehicleDaoImplement();
        try {
            init();
        } catch (IOException ex) {
            Logger.getLogger(BuscarControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void init() throws IOException {
        if (vDao.leer().isEmpty()) {
            return;
        }
        
        /*
        Las filas son la cantidad de vehículos existentes en el archivo.
        Las columnas son la cantidad total de atributos de cada objeto.
        */
        int filas = vDao.leer().size();
        int columnas = 15;
        String data[][] = new String[filas][columnas];
        for (int i = 0; i < filas; i++) {
            data[i][0] = Integer.toString(vDao.leer().get(i).getId());
            data[i][1] = Integer.toString(vDao.leer().get(i).getStockNumber());
            data[i][2] = Integer.toString(vDao.leer().get(i).getAño());
            data[i][3] = vDao.leer().get(i).getMarca();
            data[i][4] = vDao.leer().get(i).getModelo();
            data[i][5] = vDao.leer().get(i).getEtilo();
            data[i][6] = vDao.leer().get(i).getVin();
            data[i][7] = vDao.leer().get(i).getColorInterior();
            data[i][8] = vDao.leer().get(i).getColorExterior();
            data[i][9] = vDao.leer().get(i).getMiles();
            data[i][10] = Double.toString(vDao.leer().get(i).getPrecio());
            data[i][11] = vDao.leer().get(i).getTransmision().toString();
            data[i][12] = vDao.leer().get(i).getMotor();
            data[i][13] = vDao.leer().get(i).getImagen();
            data[i][14] = vDao.leer().get(i).getEstado();
        }
        
        defaultTableModel = new DefaultTableModel(data, nombreColumnas);
        buscar.getTablaVehiculos().setModel(defaultTableModel);
    }
}
