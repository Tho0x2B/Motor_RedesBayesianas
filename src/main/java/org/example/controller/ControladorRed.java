package src.main.java.org.example.controller;


import src.main.java.org.example.data.ILectorArchivos;
import src.main.java.org.example.model.Nodo;
import src.main.java.org.example.model.RedBayesiana;
import src.main.java.org.example.view.VistaRed;

import java.io.IOException;
import java.util.List;

public class ControladorRed {
    private RedBayesiana modelo;
    private VistaRed vista;
    private ILectorArchivos lector;


    public ControladorRed(RedBayesiana modelo, VistaRed vista, ILectorArchivos lector) {
        this.modelo = modelo;
        this.vista = vista;
        this.lector = lector;
    }

    public void cargarEstructuraDesdeArchivo(String rutaArchivo) {
        try {
            List<String> lineas = lector.leerLineas(rutaArchivo);
            for (String linea : lineas) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    modelo.agregarDependencia(partes[0].trim(), partes[1].trim());
                }
            }
        } catch (IOException e) {
            vista.mostrarError("No se pudo leer la estructura: " + e.getMessage());
        }
    }

    public void cargarProbabilidadesDesdeArchivo(String rutaArchivo) {
        try {
            List<String> lineas = lector.leerLineas(rutaArchivo);
            for (String linea : lineas) {
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
            vista.mostrarError("Error procesando probabilidades: " + e.getMessage());
        }
    }

    public void ejecutarReporte() {
        vista.mostrarEstructura(modelo);
        vista.mostrarTablas(modelo);
    }
}