/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.implement;

import java.io.IOException;
import java.util.List;
import uni.dao.VehicleDao;
import uni.flujos.FilesConnection;
import uni.pojo.Vehicle;
import java.io.File;
import com.google.gson.Gson;
import java.util.ArrayList;

/**
 *
 * @author gabri
 */
public class VehicleDaoImplement extends FilesConnection implements VehicleDao {
    
    private static final int SIZE = 398;
    private final Gson gson;
    
    public VehicleDaoImplement() {
        super(new File("vehiclesHead.dat"), new File("vehiclesData.dat"));
        gson = new Gson();
    }

    @Override
    public Vehicle buscarPorId(int id) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Vehicle> buscarPorEstado(String estado) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void crear(Vehicle t) throws IOException {
        /*
        Se obtienen los valores iniciales de n (número de registros) y
        ids (número de id único para cada registro)
        */
        getRandomConnection().getRafHead().seek(0);
        int n = getRandomConnection().getRafHead().readInt();
        int ids = getRandomConnection().getRafHead().readInt();
        
        /*
        Se calculan las posiciones del head y el data para posteriores
        modificaciones en éstos
        */
        long posHead = 8 + (n * 8);
        long posData = ids * SIZE;
        
        /*
        Se escribe en el archivo data el id único de cada vehículo
        y posteriormente el vehículo transformado a formato JSON
        */
        getRandomConnection().getRafData().seek(posData);
        getRandomConnection().getRafData().writeInt(++ids);//Se aumenta en 1 el id actual
        t.setId(ids);
        getRandomConnection().getRafData().writeUTF(gson.toJson(t));
        
        /*
        Se actualiza el head con el número de registros aumentado en 1
        y el número de id actual (anteriormente modificado)
        */
        getRandomConnection().getRafHead().seek(0);
        getRandomConnection().getRafHead().writeInt(++n);
        getRandomConnection().getRafHead().writeInt(ids);
        
        /*
        Se ubica en la posición del nuevo registro, se escribe
        el id del objeto y el stockNumber del vehículo en cuestión
        */
        getRandomConnection().getRafHead().seek(posHead);
        getRandomConnection().getRafHead().writeInt(ids);
        getRandomConnection().getRafHead().writeInt(t.getStockNumber());
        
        cerrar();
    }

    @Override
    public List<Vehicle> leer() throws IOException {
        List<Vehicle> vehiculos = new ArrayList<>();
        Vehicle vehicle;
        
        getRandomConnection().getRafHead().seek(0);
        int n = getRandomConnection().getRafHead().readInt();
        int ids;
        long posHead, posData;
        
        for (int i = 0; i < n; i++) {
            posHead = 8 + (i * 8);
            getRandomConnection().getRafHead().seek(posHead);
            ids = getRandomConnection().getRafHead().readInt();
            posData = (ids - 1) * SIZE + 4;
            getRandomConnection().getRafData().seek(posData);
            vehicle = gson.fromJson(getRandomConnection().getRafData().readUTF(), Vehicle.class);
            vehiculos.add(vehicle);
        }
        
        return vehiculos;
    }

    @Override
    public void actualizar(Vehicle t) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminar(Vehicle t) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
