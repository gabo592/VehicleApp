/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.dao;

import java.util.List;
import java.io.IOException;

/**
 *
 * @author gabri
 */
public interface IDao<T> {
    void crear(T t) throws IOException;
    List<T> leer() throws IOException;
    void actualizar(T t) throws IOException;
    void eliminar(T t) throws IOException;
}
