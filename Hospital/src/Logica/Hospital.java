/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import Interfaz.VentanaPrincipal;
import static java.lang.Thread.sleep;
import java.util.Random;

/**
 *
 * @author Patricia
 */
public class Hospital {

    private VentanaPrincipal ventana;
    private ArrayList<Paciente> colaEspera = new ArrayList<>();

    private ArrayList<Auxiliar> auxiliar1 = new ArrayList<>();
    private ArrayList<Auxiliar> auxiliar2 = new ArrayList<>();
    private Puesto salaVacunacion[] = new Puesto[10];
    private Puesto salaObservacion[] = new Puesto[20];
    private ArrayList<Sanitario> salaDescanso = new ArrayList<>();
    private int vacunasDisponibles;
    private ReentrantLock lockVacunas = new ReentrantLock();
    private ReentrantLock lockEntrada = new ReentrantLock();
    private ReentrantLock lockSalaDescanso = new ReentrantLock();
    private ReentrantLock lockSalaVacunacion = new ReentrantLock();
    private ReentrantLock lockSalaObservacion = new ReentrantLock();
    private Condition salaVacunacionLlena = lockSalaVacunacion.newCondition();
    private Semaphore semaforo = new Semaphore(1);
    private Semaphore semaforoLleno = new Semaphore(10);
    private Semaphore semaforoVacio = new Semaphore(0);
    private Semaphore em = new Semaphore(1);

    public Hospital(VentanaPrincipal ventana) {
        this.ventana = ventana;
        for (int i = 0; i < 10; i++) {
            salaVacunacion[i] = new Puesto(i);
        }
        for (int i = 0; i < 20; i++) {
            salaObservacion[i] = new Puesto(i);
        }
    }

    public Puesto[] getSalaVacunacion() {
        return salaVacunacion;
    }

    public Puesto[] getSalaObservacion() {
        return salaObservacion;
    }

    public int getVacunasDisponibles() {
        int n = 0;
        lockVacunas.lock();
        try {
            n = vacunasDisponibles;
        } catch (Exception ex) {
            ex.toString();
        } finally {
            lockVacunas.unlock();
        }
        return n;
    }

    public void anadirAuxiliar(Auxiliar a) {
        if ("A1".equals(a.getIdA())) {
            auxiliar1.add(a);
            ventana.actualizarAux(a);
        } else if ("A2".equals(a.getIdA())) {
            auxiliar2.add(a);
            ventana.actualizarAux(a);
        }
    }

    public Auxiliar getAuxiliar1() {
        return auxiliar1.get(0);
    }

    public void vacunar() {
        lockVacunas.lock();
        try {
            vacunasDisponibles--;
            ventana.actualizarVacunas(vacunasDisponibles);
        } catch (Exception e) {
            e.toString();
        } finally {
            lockVacunas.unlock();
        }
    }

    public void anadirVacunas() {
        lockVacunas.lock();
        try {
            vacunasDisponibles++;
            ventana.actualizarVacunas(vacunasDisponibles);
        } catch (Exception e) {
            e.toString();
        } finally {
            lockVacunas.unlock();
        }
    }
    

    public boolean tamano() {
        int n = 0;
        boolean sitio = false;
        lockSalaVacunacion.lock();
        try {
            for (int i = 0; i < salaVacunacion.length; i++) {
                if (salaVacunacion[i].isOcupadoP() == true) {
                    n++;
                }
            }
        } catch (Exception ex) {
            ex.toString();
        } finally {
            lockSalaVacunacion.unlock();
        }
        lockSalaObservacion.lock();
        try {
            for (int i = 0; i < salaObservacion.length; i++) {
                if (salaObservacion[i].isOcupadoP() == true) {
                    n++;
                }
            }
        } catch (Exception ex) {
            ex.toString();
        } finally {
            lockSalaObservacion.unlock();
        }
        if (n < salaObservacion.length) {
            sitio = true;
        }
        return sitio;
    }

    public void entrarHospital(Paciente p) {
        lockEntrada.lock();
        try {
            System.out.println("El paciente " + p.getIdP() + " ha entrado al hospital");
            colaEspera.add(p);
            ventana.actualizarColaEspera(colaEspera);
        } finally {
            lockEntrada.unlock();
        }
    }

    public void sacarColaEspera(Paciente p) {
        lockEntrada.lock();
        try {
            colaEspera.remove(p);
            ventana.actualizarColaEspera(colaEspera);
        } finally {
            lockEntrada.unlock();
        }
    }

    public void comprobarDatos(Paciente p) {
        try {
            semaforo.acquire();
            sacarColaEspera(p);
            ventana.actualizarPacienteEspera(p);
            auxiliar1.get(0).pacienteCitado(p);
            if (p.isCitado() == false) {
                semaforo.release();
            }
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public void entrarSalaDescanso(Sanitario s) {
        lockSalaDescanso.lock();
        try {
            System.out.println("El sanitario " + s.getIdS() + " entra a la sala de descanso");
            salaDescanso.add(s);
            ventana.actualizarSalaDesc(salaDescanso);
        } finally {
            lockSalaDescanso.unlock();
        }
    }

    public void salirSalaDescanso(Sanitario s) {
        lockSalaDescanso.lock();
        try {
            System.out.println("El sanitario " + s.getIdS() + " sale de la sala de descanso");
            salaDescanso.remove(s);
            ventana.actualizarSalaDesc(salaDescanso);
        } finally {
            lockSalaDescanso.unlock();
        }
    }

    public void entrarSalaVacunacion(Paciente p) {
        System.out.println("Hola quiero entrar a la sala de vacunacion " + p.getIdP());
        try {
            while (auxiliar1.get(0).haySitioDisponible() == false) {
                //System.out.println("Estoy esperando para vacunarme " + p.getIdP());
            }
            semaforo.release();
            semaforoLleno.acquire();
            em.acquire();
            p.setIdPuestoVac(auxiliar1.get(0).quePuestoVacunacionDisponible());
            salaVacunacion[p.getIdPuestoVac()].entrarPuesto(p);
            ventana.actualizarSalaVac(p.getIdPuestoVac(), salaVacunacion);
            System.out.println("El paciente " + p.getIdP() + " entra a vacunarse");
            em.release();
            semaforoVacio.release();
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public void salirSalaVacunacion(Paciente p) {
        try {
            semaforoVacio.acquire();
            em.acquire();
            salaVacunacion[p.getIdPuestoVac()].salirPuesto(p);
            ventana.actualizarSalaVac(p.getIdPuestoVac(), salaVacunacion);
            System.out.println("El paciente " + p.getIdP() + " sale de vacunarse");
            em.release();
            semaforoLleno.release();
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public void entrarTrabajarVac(Sanitario s) {
        int i = 0;
        try {
            while ((i < salaVacunacion.length)) {
                if (!salaVacunacion[i].isOcupadoS()) {
                    s.setPuesto(i);
                    salaVacunacion[i].trabajarPuesto(s);
                    ventana.actualizarSalaVac(s.getPuesto(), salaVacunacion);
                    i = salaVacunacion.length + 1;
                }
                i++;
            }
        } catch (Exception ex) {
            ex.toString();
        } 
    }

    public void salirTrabajarVac(Sanitario s) {
        salaVacunacion[s.getPuesto()].terminarTrabajar(s);
        ventana.actualizarSalaVac(s.getPuesto(), salaVacunacion);
    }

    public void trabajarObs(Sanitario s) {
        try {
            int i = 0;
            while (i < salaObservacion.length) {
                System.out.println("hola2");
                if ((salaObservacion[i].getListPaciente().isEmpty() == false) && (salaObservacion[i].getPaciente().isEfectosAdversos() == true)) {
                    System.out.println("El sanitario " + s.getIdS() + " entra a dar el visto bueno al paciente " + salaObservacion[i].getPaciente().getIdP() + " en el puesto " + i);
                    salaObservacion[i].trabajarPuesto(s);
                    ventana.actualizarSalaObs(i, salaObservacion);
                    sleep(new Random().nextInt(3000) + 2000);
                    salaObservacion[i].getPaciente().setEfectosAdversos(false);
                    salaObservacion[i].terminarTrabajar(s);
                    ventana.actualizarSalaObs(i, salaObservacion);
                    i = salaObservacion.length + 1;
                }
                i++;
            }
        } catch (Exception ex) {
            ex.toString();
        }
    }

    public void entrarSalaObservacion(Paciente p) {
        boolean aux = false;
        int posicion = 0, i = 0;
        lockSalaObservacion.lock();
        try {
            while ((i < salaObservacion.length) && (aux == false)) {
                if (salaObservacion[i].isOcupadoP() == false) {
                    posicion = i;
                    aux = true;
                }
                i++;
            }
            p.setIdPuestoObs(posicion);
            salaObservacion[p.getIdPuestoObs()].entrarPuesto(p);
            System.out.println("El paciente " + p.getIdP() + " entra a observacion");
            ventana.actualizarSalaObs(p.getIdPuestoObs(), salaObservacion);
        } catch (Exception ex) {
            ex.toString();
        } finally {
            lockSalaObservacion.unlock();
        }
    }

    public void salirSalaObservacion(Paciente p) {
        lockSalaObservacion.lock();
        try {
            salaObservacion[p.getIdPuestoObs()].salirPuesto(p);
            ventana.actualizarSalaObs(p.getIdPuestoObs(), salaObservacion);
            System.out.println("El paciente " + p.getIdP() + " sale de observacion");
        } catch (Exception e) {
            e.toString();
        } finally {
            lockSalaObservacion.unlock();
        }
    }

}
