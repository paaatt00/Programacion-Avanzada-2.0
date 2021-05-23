/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logica;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import Interfaz.VentanaPrincipal;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private ReentrantLock lockTxt = new ReentrantLock();
    private Semaphore semaforo = new Semaphore(1);
    private Semaphore semaforoLleno = new Semaphore(10);
    private Semaphore semaforoVacio = new Semaphore(0);
    private Semaphore semaforoSalaObs = new Semaphore(1);
    private Semaphore em = new Semaphore(1);
    private int pacientesVacunados;
    private FileWriter archivo;
    private BufferedWriter bw;
    private Date date;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    public Hospital(VentanaPrincipal ventana) throws IOException {
        this.ventana = ventana;
        this.pacientesVacunados = 0;
        for (int i = 0; i < 10; i++) {
            salaVacunacion[i] = new Puesto(i);
        }
        for (int i = 0; i < 20; i++) {
            salaObservacion[i] = new Puesto(i);
        }
        GenPersonas gen = new GenPersonas(this);
        gen.start();
        for (int i = 1; i <= 10; i++) {
            Sanitario s = new Sanitario("S" + i, this);
            s.start();
        }
        Auxiliar aux1 = new Auxiliar("A1", this);
        aux1.start();
        Auxiliar aux2 = new Auxiliar("A2", this);
        aux2.start();
        String ruta = "archivo.txt";
        archivo = new FileWriter(ruta);
        bw = new BufferedWriter(archivo);        
        
    }

    public ArrayList<Paciente> getColaEspera() {
        return colaEspera;
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

    public Auxiliar getAuxiliar1() {
        return auxiliar1.get(0);
    }

    public int getPacientesVacunados() {
        return pacientesVacunados;
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

    public void quitarAuxiliar(Auxiliar a) {
        if ("A1".equals(a.getIdA())) {
            auxiliar1.add(a);
            ventana.actualizarAux(a);
        } else if ("A2".equals(a.getIdA())) {
            auxiliar2.add(a);
            ventana.actualizarAux(a);
        }
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
        lockSalaObservacion.lock();
        try {
            for (int i = 0; i < salaVacunacion.length; i++) {
                if (salaVacunacion[i].isOcupadoP() == true) {
                    n++;
                }
            }
            for (int i = 0; i < salaObservacion.length; i++) {
                if (salaObservacion[i].isOcupadoP() == true) {
                    n++;
                }
            }
        } catch (Exception ex) {
            ex.toString();
        } finally {
            lockSalaVacunacion.unlock();
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
            date = Calendar.getInstance().getTime();
            String strDate = dateFormat.format(date);
            bw.write(strDate + " El paciente " + p.getIdP() + " ha entrado al hospital\n");
            colaEspera.add(p);
            ventana.actualizarColaEspera(colaEspera);
        } catch (Exception ex) {
            ex.toString();
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
            date = Calendar.getInstance().getTime();
            String strDate = dateFormat.format(date);
            bw.write(strDate + " El sanitario " + s.getIdS() + " entra a la sala de descanso\n");
            salaDescanso.add(s);
            ventana.actualizarSalaDesc(salaDescanso);
        } catch (Exception ex) {
            ex.toString();
        } finally {
            lockSalaDescanso.unlock();
        }
    }

    public void salirSalaDescanso(Sanitario s) {
        lockSalaDescanso.lock();
        try {
            System.out.println("El sanitario " + s.getIdS() + " sale de la sala de descanso");
            date = Calendar.getInstance().getTime();
            String strDate = dateFormat.format(date);
            escribirTxt(strDate + " El sanitario " + s.getIdS() + " sale de la sala de descanso\n");
            salaDescanso.remove(s);
            ventana.actualizarSalaDesc(salaDescanso);
        } catch (Exception ex) {
            ex.toString();
        } finally {
            lockSalaDescanso.unlock();
        }
    }

    public void entrarSalaVacunacion(Paciente p) {
        try {
            while (auxiliar1.get(0).haySitioDisponible() == false) {
                sleep(1);
            }
            semaforo.release();
            semaforoLleno.acquire();
            em.acquire();
            p.setIdPuestoVac(auxiliar1.get(0).quePuestoVacunacionDisponible());
            salaVacunacion[p.getIdPuestoVac()].entrarPuesto(p);
            pacientesVacunados++;
            ventana.actualizarSalaVac(p.getIdPuestoVac(), salaVacunacion);
            System.out.println("El paciente " + p.getIdP() + " entra a vacunarse");
            date = Calendar.getInstance().getTime();
            String strDate = dateFormat.format(date);
            bw.write(strDate + " El paciente " + p.getIdP() + " entra a vacunarse\n");
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
            date = Calendar.getInstance().getTime();
            String strDate = dateFormat.format(date);
            escribirTxt(strDate + " El paciente " + p.getIdP() + " sale de vacunarse\n");
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
                if (salaVacunacion[i].isOcupadoS() == false) {
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
        lockSalaObservacion.lock();
        try {
            semaforoSalaObs.acquire();
            int i = 0;
            while (i < salaObservacion.length) {
                if ((salaObservacion[i].getListPaciente().isEmpty() == false) && (salaObservacion[i].getPaciente().isEfectosAdversos() == true)) {
                    System.out.println("El sanitario " + s.getIdS() + " entra a dar el visto bueno al paciente " + salaObservacion[i].getPaciente().getIdP() + " en el puesto " + (i + 1));
                    date = Calendar.getInstance().getTime();
                    String strDate = dateFormat.format(date);
                    bw.write(strDate + " El sanitario " + s.getIdS() + " entra a dar el visto bueno al paciente " + salaObservacion[i].getPaciente().getIdP() + " en el puesto " + (i + 1) + "\n");
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
            semaforoSalaObs.release();
        } catch (Exception ex) {
            ex.toString();
        } finally {
            lockSalaObservacion.unlock();
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
            date = Calendar.getInstance().getTime();
            String strDate = dateFormat.format(date);
            escribirTxt(strDate + " El paciente " + p.getIdP() + " entra a observacion\n");
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
            semaforoSalaObs.acquire();
            salaObservacion[p.getIdPuestoObs()].salirPuesto(p);
            ventana.actualizarSalaObs(p.getIdPuestoObs(), salaObservacion);
            System.out.println("El paciente " + p.getIdP() + " sale de observacion");
            date = Calendar.getInstance().getTime();
            String strDate = dateFormat.format(date);
            escribirTxt(strDate + " El paciente " + p.getIdP() + " sale de observacion\n");
            semaforoSalaObs.release();
        } catch (Exception e) {
            e.toString();
        } finally {
            lockSalaObservacion.unlock();
        }
    }

    public boolean comprobarObservacion() {
        boolean finalizar1 = true;
        boolean finalizar2 = true;
        boolean finalizar3 = true;
        boolean finalizar = false;

        lockEntrada.lock();
        lockSalaVacunacion.lock();
        lockSalaObservacion.lock();
        try {
            if (colaEspera.isEmpty() == false) {
                finalizar1 = false;
            }
            for (int i = 0; i < salaVacunacion.length; i++) {
                if (salaVacunacion[i].isOcupadoP() == true) {
                    finalizar2 = false;
                }
            }
            for (int i = 0; i < salaObservacion.length; i++) {
                if (salaObservacion[i].isOcupadoP() == true && salaObservacion[i].getPaciente().isEfectosAdversos() == false) {
                    finalizar3 = false;
                }
            }
        } finally {
            lockEntrada.unlock();
            lockSalaVacunacion.unlock();
            lockSalaObservacion.unlock();
        }
        if ((finalizar1 == true) && (finalizar2 == true) && (finalizar3 == true)) {
            finalizar = true;
        }
        return finalizar;

    }

    public boolean terminar() {
        boolean finalizar1 = true;
        boolean finalizar2 = true;
        boolean finalizar3 = true;
        boolean finalizar = false;

        lockEntrada.lock();
        lockSalaVacunacion.lock();
        lockSalaObservacion.lock();
        try {
            if (colaEspera.isEmpty() == false) {
                finalizar1 = false;
            }
            for (int i = 0; i < salaVacunacion.length; i++) {
                if (salaVacunacion[i].isOcupadoP() == true) {
                    finalizar2 = false;
                }
            }
            for (int i = 0; i < salaObservacion.length; i++) {
                if (salaObservacion[i].isOcupadoP() == true) {
                    finalizar3 = false;
                }
            }
        } finally {
            lockEntrada.unlock();
            lockSalaVacunacion.unlock();
            lockSalaObservacion.unlock();
        }
        if ((finalizar1 == true) && (finalizar2 == true) && (finalizar3 == true)) {
            finalizar = true;
        }
        return finalizar;
    }

    public boolean cerrarHospital() {
        boolean finalizar1 = true;
        boolean finalizar2 = true;
        boolean finalizar3 = true;
        boolean finalizar4 = true;
        boolean finalizar = false;

        lockEntrada.lock();
        lockSalaVacunacion.lock();
        lockSalaObservacion.lock();
        lockSalaDescanso.lock();

        try {
            if (colaEspera.isEmpty() == false) {
                finalizar1 = false;
            }
            for (int i = 0; i < salaVacunacion.length; i++) {
                if (salaVacunacion[i].isOcupadoP() == true || salaVacunacion[i].isOcupadoS() == true) {
                    finalizar2 = false;
                }
            }
            for (int i = 0; i < salaObservacion.length; i++) {
                if (salaObservacion[i].isOcupadoP() == true || salaObservacion[i].isOcupadoS() == true) {
                    finalizar3 = false;
                }
            }
            if (salaDescanso.isEmpty() == false) {
                finalizar4 = false;
            }
        } catch (Exception ex) {
            ex.toString();
        } finally {
            lockEntrada.unlock();
            lockSalaVacunacion.unlock();
            lockSalaObservacion.unlock();
            lockSalaDescanso.unlock();
        }
        if ((finalizar1 == true) && (finalizar2 == true) && (finalizar3 == true) && (finalizar4 == true)) {
            finalizar = true;
        }
        return finalizar;
    }

    public void cerrarVentana() throws IOException {
        bw.close();
        System.out.println("---------------------------");
        ventana.terminarProgramaVentana();

    }

    public void escribirTxt(String s) throws IOException {
        lockTxt.lock();
        try {
            bw.write(s);
        } catch (Exception ex) {
            ex.toString();
        } finally {
            lockTxt.unlock();
        }
    }

}
