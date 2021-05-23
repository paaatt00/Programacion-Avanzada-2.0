/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import Interfaz.VentanaPrincipal;
import java.io.IOException;

/**
 *
 * @author Patricia e Isabel
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        VentanaPrincipal ventana = new VentanaPrincipal();
        ventana.setVisible(true);
        Hospital hospital = new Hospital(ventana);

        boolean aux = true;
        while (aux) {
            if (hospital.cerrarHospital() == true) {
                hospital.cerrarVentana();
                aux = false;
            }
        }
    }
}
