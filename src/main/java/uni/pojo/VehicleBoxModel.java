/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.pojo;

/**
 *
 * @author gabri
 */
public class VehicleBoxModel {
    private String color;
    private String modelo;

    public VehicleBoxModel(String color, String modelo) {
        this.color = color;
        this.modelo = modelo;
    }

    public VehicleBoxModel() {
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    
}
