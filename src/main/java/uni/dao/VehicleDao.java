/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.dao;

import java.io.IOException;
import uni.pojo.Vehicle;
import java.util.List;

/**
 *
 * @author gabri
 */
public interface VehicleDao extends IDao<Vehicle> {
    Vehicle buscarPorId(int id) throws IOException;
    List<Vehicle> buscarPorEstado(String estado) throws IOException;
}
