/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import static java.lang.Thread.sleep;
import java.util.Random;

/**
 *
 * @author Patricia
 */
public class GenPersonas extends Thread {

    private Hospital hospital;

    public GenPersonas(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 2000; i++) {
                try {
                    Paciente p = new Paciente("P" + i, hospital);
                    p.start();
                    sleep(new Random().nextInt(2000) + 1000);
                } catch (Exception ex) {
                    ex.toString();
                }
            }

        } catch (Exception ex) {
            ex.toString();
        }        
    }
}
