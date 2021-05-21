/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Interfaz.VentanaPrincipal;

/**
 *
 * @author Patricia
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
        Hospital hospital = new Hospital(ventana);
        GenPersonas gen = new GenPersonas(hospital);
        gen.start();
        for (int i = 1; i <= 10; i++) {
            Sanitario s = new Sanitario("S" + i, hospital);
            s.start();
        }
        Auxiliar aux1 = new Auxiliar("A1", hospital);
        aux1.start();
        Auxiliar aux2 = new Auxiliar("A2", hospital);
        aux2.start();
        //hola
    }
}
