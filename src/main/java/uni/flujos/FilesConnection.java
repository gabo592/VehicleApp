/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.flujos;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author gabri
 */

/*
Esta clase regula la clase RandomConnection, que ésta a su vez regula
los RandomAccesFile. A esta clase se le pasan los archivos a los cuales
se van a acceder (el head y el data)
*/
public class FilesConnection {
    private RandomConnection randomConnection;
    private final File head;
    private final File data;

    public FilesConnection(File head, File data) {
        this.head = head;
        this.data = data;
    }
    
    /*
    Patrón de diseño singleton para que solo exista 1 flujo de datos para
    los ficheros que se pasaron como parámetro en el constructor
    */
    public RandomConnection getRandomConnection() throws IOException {
        if (!head.exists()) {
            head.createNewFile();
        }
        if (!data.exists()) {
            data.createNewFile();
        }
        if (randomConnection == null) {
            randomConnection = new RandomConnection(head, data);
        }
        
        return randomConnection;
    }
    
    /*
    A través de la clase RandomConnection se cierran los flujos
    */
    public void cerrar() throws IOException {
        randomConnection.cerrar();
        randomConnection = null;
    }
}
