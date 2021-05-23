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
public class Sanitario extends Thread {

    private String id;
    private int puesto, pacientesVacunados;
    private Hospital hospital;
    private Date date;
    DateFormat dateFormat;
    
    public Sanitario(String id, Hospital hospital) {
        this.id = id;
        this.hospital = hospital;
        this.pacientesVacunados = 0;
        dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
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
            while (hospital.terminar() == false || hospital.getPacientesVacunados() == 0) {
                hospital.entrarTrabajarVac(this);
                while (pacientesVacunados < 15 && hospital.comprobarObservacion() == false && (hospital.terminar() == false)) {
                    sleep(1);
                    if ((hospital.getVacunasDisponibles() > 0)) {
                        sleep(1);
                        if ((hospital.getSalaVacunacion()[puesto].isOcupadoP() == true)) {
                            //pone vacuna
                            sleep(new Random().nextInt(2000) + 3000);
                            pacientesVacunados++;
                            System.out.println("El sanitario " + id + " vacuna al paciente " + hospital.getSalaVacunacion()[puesto].getPaciente().getIdP() + " en el puesto " + (puesto + 1));
                            date = Calendar.getInstance().getTime();  
                            String strDate = dateFormat.format(date);
                            hospital.escribirTxt(strDate + " El sanitario " + id + " vacuna al paciente " + hospital.getSalaVacunacion()[puesto].getPaciente().getIdP() + " en el puesto " + (puesto + 1) + "\n");
                            hospital.vacunar();
                            hospital.getSalaVacunacion()[puesto].getPaciente().setVacunado();
                        }
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
