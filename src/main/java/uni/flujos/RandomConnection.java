/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.flujos;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author gabri
 */

/*
Esta clase solo crea y maneja los RandomAccesFile para
los archivos que se le pasen como parámetro
*/
public class RandomConnection {
    private RandomAccessFile rafHead;
    private RandomAccessFile rafData;

    protected RandomConnection(File head, File data) throws FileNotFoundException, IOException {
        rafHead = new RandomAccessFile(head, "rw");
        rafData = new RandomAccessFile(data, "rw");
        
        if (rafHead.length() == 0) {
            rafHead.writeInt(0); //n
            rafHead.writeInt(0); //ids
        }
    }

    public RandomAccessFile getRafHead() {
        return rafHead;
    }

    public RandomAccessFile getRafData() {
        return rafData;
    }
    
    protected void cerrar() throws IOException {
        if (rafData != null) {
            rafData.close();
            rafData = null;
        }
        if (rafHead != null) {
            rafHead.close();
            rafHead = null;
        }
    }
}
