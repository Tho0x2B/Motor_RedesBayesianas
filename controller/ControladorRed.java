package controller;

import model.RedBayesiana;
import model.Nodo;
import view.VistaRed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ControladorRed {
    private RedBayesiana modelo;
    private VistaRed vista;

    public ControladorRed(RedBayesiana modelo, VistaRed vista) {
        this.modelo = modelo;
        this.vista = vista;
    }

    // Procesa el archivo donde cada línea es: Padre,Hijo
    public void cargarEstructuraDesdeArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    modelo.agregarDependencia(partes[0].trim(), partes[1].trim());
                }
            }
        } catch (IOException e) {
            vista.mostrarError("No se pudo leer el archivo de estructura: " + e.getMessage());
        }
    }

    // Procesa el archivo donde cada línea es: NombreNodo,Condiciones,Probabilidad
    // Ej: Appointment,Rain=light;Maintenance=no,0.1296
    public void cargarProbabilidadesDesdeArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    String nombreNodo = partes[0].trim();
                    String condicion = partes[1].trim();
                    Double probabilidad = Double.parseDouble(partes[2].trim());

                    Nodo nodo = modelo.obtenerOCrearNodo(nombreNodo);
                    nodo.agregarProbabilidad(condicion, probabilidad);
                }
            }
        } catch (IOException | NumberFormatException e) {
            vista.mostrarError("No se pudo leer el archivo de probabilidades: " + e.getMessage());
        }
    }

    public void ejecutarReporte() {
        vista.mostrarEstructura(modelo);
        vista.mostrarTablas(modelo);
    }
}