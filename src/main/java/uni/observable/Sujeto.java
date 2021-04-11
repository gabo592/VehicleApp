/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uni.observable;

/**
 *
 * @author gabri
 */
public interface Sujeto {
    void notificar();
    void enlazarObservador(Observador o);
}
