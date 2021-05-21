/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.util.Random;

/**
 *
 * @author Patricia
 */
public class Paciente extends Thread {

    private String id;
    private int idPuestoVac, idPuestoObs;
    private boolean vacunado, citado, efectosAdversos;
    private Hospital hospital;

    public Paciente(String id, Hospital hospital) {
        this.id = id;
        this.vacunado = false;
        this.hospital = hospital;
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
                }
                while (efectosAdversos == true) {
                    sleep(1);
                }
                hospital.salirSalaObservacion(this);
            } else {
                System.out.println("El paciente " + id + " ha acudido sin cita");
            }
        } catch (InterruptedException ex) {
            ex.toString();
        }
    }
}
