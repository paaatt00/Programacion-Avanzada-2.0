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
public class Auxiliar extends Thread {

    private String id;
    private int pacientesRegistrados = 0, vacunasPreparadas = 0;
    private Hospital hospital;

    public Auxiliar(String id, Hospital hospital) {
        this.id = id;
        this.hospital = hospital;
    }

    public String getIdA() {
        return id;
    }

    public void pacienteCitado(Paciente p) {
        pacientesRegistrados++;
        System.out.println("He registrao " + pacientesRegistrados);
        System.out.println("El auxiliar " + id + " ha comprobado los datos del paciente " + p.getIdP());
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
                while (true) { //crear funcion o algo que compruebe si quedan pacientes en el hospital
                    //hospital.comprobarDatos();
                    System.out.println("voy a descansar jiijijiijijijijiijjijiijjiji");
                    sleep(1);
                    if (pacientesRegistrados % 10 == 0) {
                        System.out.println("El auxiliar " + id + " se va a descansar");
                        sleep(new Random().nextInt(2000) + 3000);
                    }
                }
            } else if (this.id == "A2") {
                hospital.anadirAuxiliar(this);
                while (true) { //crear funcion o algo que compruebe si quedan pacientes en el hospital
                    hospital.anadirVacunas();
                    vacunasPreparadas++;
                    sleep(new Random().nextInt(500) + 500);
                    if (vacunasPreparadas % 20 == 0) {
                        System.out.println("El auxiliar " + id + " se va a descansar");
                        sleep(new Random().nextInt(3000) + 1000);
                    }
                }
            }
        } catch (Exception ex) {
            ex.toString();
        }
    }

}
