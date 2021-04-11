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
public class Vehicle {
    private int id;
    private int stockNumber;//20
    private int año;//4
    private String marca;//30
    private String modelo;//30
    private String estilo;//30
    private String vin;//20
    private String colorExterior;//30
    private String colorInterior;//30
    private String miles;//7
    private float precio;//7
    private Transmission transmision;//20
    private String motor;//50
    private String imagen;//100
    private String estado;//20
    //Tamaño total = 398
    
    public enum Transmission{
        AUTOMATICA, MANUAL
    }

    public Vehicle() {
    }

    public Vehicle(int stockNumber, int año, String marca, String modelo, String estilo, String vin, String colorExterior, String colorInterior, String miles, float precio, Transmission transmision, String motor, String imagen, String estado) {
        this.stockNumber = stockNumber;
        this.año = año;
        this.marca = marca;
        this.modelo = modelo;
        this.estilo = estilo;
        this.vin = vin;
        this.colorExterior = colorExterior;
        this.colorInterior = colorInterior;
        this.miles = miles;
        this.precio = precio;
        this.transmision = transmision;
        this.motor = motor;
        this.imagen = imagen;
        this.estado = estado;
    }

    public Vehicle(int id, int stockNumber, int año, String marca, String modelo, String estilo, String vin, String colorExterior, String colorInterior, String miles, float precio, Transmission transmision, String motor, String imagen, String estado) {
        this.id = id;
        this.stockNumber = stockNumber;
        this.año = año;
        this.marca = marca;
        this.modelo = modelo;
        this.estilo = estilo;
        this.vin = vin;
        this.colorExterior = colorExterior;
        this.colorInterior = colorInterior;
        this.miles = miles;
        this.precio = precio;
        this.transmision = transmision;
        this.motor = motor;
        this.imagen = imagen;
        this.estado = estado;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getStockNumber() {
        return stockNumber;
    }

    public void setStockNumber(int stockNumber) {
        this.stockNumber = stockNumber;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEtilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getColorExterior() {
        return colorExterior;
    }

    public void setColorExterior(String colorExterior) {
        this.colorExterior = colorExterior;
    }

    public String getColorInterior() {
        return colorInterior;
    }

    public void setColorInterior(String colorInterior) {
        this.colorInterior = colorInterior;
    }

    public String getMiles() {
        return miles;
    }

    public void setMiles(String miles) {
        this.miles = miles;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public Transmission getTransmision() {
        return transmision;
    }

    public void setTransmision(Transmission transmision) {
        this.transmision = transmision;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
