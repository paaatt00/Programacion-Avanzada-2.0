/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Patricia e Isabel
 */
public class Paciente extends Thread {

    private String id;
    private int idPuestoVac, idPuestoObs;
    private boolean vacunado, citado, efectosAdversos;
    private Hospital hospital;
    Date date; 
    DateFormat dateFormat; 

    public Paciente(String id, Hospital hospital) {
        this.id = id;
        this.vacunado = false;
        this.hospital = hospital;
        dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"); 
    }
    
    public String getIdP() {
        return id;
    }

    public int getIdPuestoVac() {
        return idPuestoVac;
    }

    public void setIdPuestoVac(int idPuestoVac) {
        this.idPuestoVac = idPuestoVac;
    }

    public int getIdPuestoObs() {
        return idPuestoObs;
    }

    public void setIdPuestoObs(int idPuestoObs) {
        this.idPuestoObs = idPuestoObs;
    }

    public void setVacunado() {
        this.vacunado = true;
    }

    public boolean isCitado() {
        return citado;
    }

    public void setCitado(boolean citado) {
        this.citado = citado;
    }

    public boolean isEfectosAdversos() {
        return efectosAdversos;
    }

    public void setEfectosAdversos(boolean efectosAdversos) {
        this.efectosAdversos = efectosAdversos;
    }

    @Override
    public void run() {
        try {
            hospital.entrarHospital(this);
            hospital.comprobarDatos(this);
            if (citado == true) {
                hospital.entrarSalaVacunacion(this);
                while (vacunado == false) {
                    sleep(1);
                }
                hospital.salirSalaVacunacion(this);
                hospital.entrarSalaObservacion(this);
                sleep(10000);
                int rand = new Random().nextInt(100);
                if (rand < 5) {
                    efectosAdversos = true;
                    System.out.println("Al paciente " + id + " la vacuna le ha producido efectos adversos");
                    date = Calendar.getInstance().getTime();
                    String strDate = dateFormat.format(date);  
                    hospital.escribirTxt(strDate + " Al paciente " + id + " la vacuna le ha producido efectos adversos\n");
                }
                while (efectosAdversos == true) {
                    sleep(1);
                }
                hospital.salirSalaObservacion(this);
            } else {
                System.out.println("El paciente " + id + " ha acudido sin cita");
                date = Calendar.getInstance().getTime();
                String strDate = dateFormat.format(date); 
                hospital.escribirTxt(strDate + " El paciente " + id + " ha acudido sin cita\n");
            }
        } catch (Exception ex) {
            ex.toString();
        }        
    }
}
