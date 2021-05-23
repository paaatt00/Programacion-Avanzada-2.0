/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Patricia e Isabel
 */
public class Auxiliar extends Thread {

    private String id;
    private int pacientesRegistrados = 0, vacunasPreparadas = 0;
    private Hospital hospital;
    Date date; 
    DateFormat dateFormat;

    public Auxiliar(String id, Hospital hospital) {
        this.id = id;
        this.hospital = hospital;
        dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");  
    }

    public String getIdA() {
        return id;
    }

    public void pacienteCitado(Paciente p) throws IOException {
        pacientesRegistrados++;
        System.out.println("El auxiliar " + id + " ha comprobado los datos del paciente " + p.getIdP());        
        date = Calendar.getInstance().getTime();
        String strDate = dateFormat.format(date);          
        hospital.escribirTxt(strDate + " El auxiliar " + id + " ha comprobado los datos del paciente " + p.getIdP() + "\n");
        int rand = new Random().nextInt(100);
        if (rand == 1) {
            p.setCitado(false);
        } else {
            p.setCitado(true);
        }
        
    }

    public boolean haySitioDisponible() {
        boolean puesto = false;
        int i = 0;
        while ((puesto == false) && (i < hospital.getSalaVacunacion().length)) { //
            if ((hospital.getSalaVacunacion()[i].isOcupadoP() == false) && (hospital.getSalaVacunacion()[i].isOcupadoS() == true) && (hospital.tamano() == true)) {
                puesto = true;
            }
            i++;
        }
        return puesto;
    }

    public int quePuestoVacunacionDisponible() {
        boolean puesto = false;
        int posicion = 0, i = 0;
        while ((i < hospital.getSalaVacunacion().length) && (puesto == false)) {
            if (!hospital.getSalaVacunacion()[i].isOcupadoP() && hospital.getSalaVacunacion()[i].isOcupadoS()) {
                puesto = true;
                posicion = i;
            }
            i++;
        }
        return posicion;
    }

    @Override
    public void run() {
        try {
            if (this.id == "A1") {
                hospital.anadirAuxiliar(this);
                while (hospital.terminar() == false || hospital.getPacientesVacunados() == 0) {                   
                    sleep(1);
                    if (pacientesRegistrados % 10 == 0) {
                        System.out.println("El auxiliar " + id + " se va a descansar");
                        date = Calendar.getInstance().getTime();
                        String strDate = dateFormat.format(date);  
                        hospital.escribirTxt(strDate + " El auxiliar " + id + " se va a descansar\n");
                        sleep(new Random().nextInt(2000) + 3000);
                        pacientesRegistrados = 0;
                    }
                }
              
            } else if (this.id == "A2") {
                hospital.anadirAuxiliar(this);
                while (hospital.terminar() == false || hospital.getPacientesVacunados() == 0) {
                    hospital.anadirVacunas();
                    vacunasPreparadas++;
                    sleep(new Random().nextInt(500) + 500);
                    if (vacunasPreparadas % 20 == 0) {
                        System.out.println("El auxiliar " + id + " se va a descansar");
                        date = Calendar.getInstance().getTime();
                        String strDate = dateFormat.format(date);  
                        hospital.escribirTxt(strDate + " El auxiliar " + id + " se va a descansar\n");
                        sleep(new Random().nextInt(3000) + 1000);
                        vacunasPreparadas = 0;
                    }
                }               
            }            
        } catch (Exception ex) {
            ex.toString();
        } 
    }
}
