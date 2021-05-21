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
public class Sanitario extends Thread {

    private String id;
    private int puesto, pacientesVacunados;
    private Hospital hospital;
    
    public Sanitario(String id, Hospital hospital) {
        this.id = id;
        this.hospital = hospital;
        this.pacientesVacunados = 0;
    }
    
    public String getIdS() {
        return id;
    }

    public int getPuesto() {
        return puesto;
    }

    public void setPuesto(int puesto) {
        this.puesto = puesto;
    }
    
    @Override
    public void run() {
        try {
            hospital.entrarSalaDescanso(this);
            sleep(new Random().nextInt(2000) + 1000);
            hospital.salirSalaDescanso(this);
            while (true) { //crear funcion o algo que compruebe si quedan pacientes en el hospital
                hospital.entrarTrabajarVac(this);
                while (pacientesVacunados < 15) {
                    if ((hospital.getVacunasDisponibles() > 0) && (hospital.getSalaVacunacion()[puesto].isOcupadoP() == true)) {
                        //pone vacuna
                        sleep(new Random().nextInt(2000) + 3000);
                        hospital.vacunar();
                        pacientesVacunados++;
                        System.out.println("El sanitario " + id + " vacuna al paciente " + hospital.getSalaVacunacion()[puesto].getPaciente().getIdP() + " en el puesto " + puesto);
                        hospital.getSalaVacunacion()[puesto].getPaciente().setVacunado();
                    }
                }
                hospital.salirTrabajarVac(this);
                this.pacientesVacunados = 0;
                //descansan
                hospital.entrarSalaDescanso(this);
                sleep(new Random().nextInt(3000) + 5000);
                hospital.salirSalaDescanso(this);
                //comprobar si hay algun paciente con reaccion
                hospital.trabajarObs(this);
            }
        } catch (Exception e) {
            e.toString();
        }
    }
}
