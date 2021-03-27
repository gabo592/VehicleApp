/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.controls;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import uni.panels.Crear;
import javax.swing.DefaultComboBoxModel;
import java.util.Set;
import java.util.stream.Collectors;
import uni.pojo.VehicleBoxModel;

/**
 *
 * @author gabri
 */
public class CrearControl {
    
    private final Gson gson;
    private final Crear panelCrear;
    private DefaultComboBoxModel color;
    private DefaultComboBoxModel modelo;
    private Set<VehicleBoxModel> vSubModelos;
    private Set<String> colores;
    private Set<String> modelos;

    public CrearControl(Crear crear) {
        gson = new Gson();
        this.panelCrear = crear;
        init();
    }
    
    private void init() {
        //Para leer el JSON se necesita de este objeto
        JsonReader jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/recursos/datosVehiculo.json"))));
        
        /*
        Antes de leer el archivo JSON hay que especificarle a qué tipo va a convertir,
        si a una lista, a un set, etc.
        */
        Type tipo = new TypeToken<Set<VehicleBoxModel>>(){}.getType();
        
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
        color = new DefaultComboBoxModel(colores.toArray());
        modelo = new DefaultComboBoxModel(modelos.toArray());
        
        /*
        Se les pasan los modelos a sus respectivos combo box
        */
        panelCrear.getComboModelo().setModel(modelo);
        panelCrear.getComboColorE().setModel(color);
        panelCrear.getComboColorI().setModel(color);
    }
    
}
