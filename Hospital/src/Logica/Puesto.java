/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.util.ArrayList;
import Interfaz.VentanaPrincipal;

/**
 *
 * @author Patricia
 */
public class Puesto {
    
    private int id;
    private ArrayList<Paciente> paciente = new ArrayList<>();
    private ArrayList<Sanitario> sanitario = new ArrayList<>();
    private boolean ocupadoP = false;
    private boolean ocupadoS = false;
    
    public Puesto(int id) {
        this.id = id;
        this.ocupadoP = false;
        this.ocupadoS = false;
    }

    public int getIdPuesto() {
        return id;
    }

    public Paciente getPaciente() {
        return paciente.get(0);
    }
    
    public ArrayList<Paciente> getListPaciente() {
        return paciente;
    }
    
    public ArrayList<Sanitario> getListSanitario() {
        return sanitario;
    }

    public boolean isOcupadoS() {
        return ocupadoS;
    }

    public boolean isOcupadoP() {
        return ocupadoP;
    }
    
    public void entrarPuesto(Paciente p) { //para cuando un paciente quiere entrar en un puesto
        ocupadoP = true;
        paciente.add(p);
    }
    
    public void trabajarPuesto(Sanitario s) { //para cuando un sanitario entra a trabajar
        ocupadoS = true;
        sanitario.add(s);
    }
    
    public void salirPuesto(Paciente p) {
        paciente.remove(p);
        ocupadoP = false;
    }
    
    public void terminarTrabajar(Sanitario s) {
        sanitario.remove(s);
        ocupadoS = false;
    }
}
