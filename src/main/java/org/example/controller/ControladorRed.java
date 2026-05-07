package org.example.controller;


import org.example.data.ILectorArchivos;
import org.example.model.RedBayesiana;
import org.example.view.VistaRed;

import java.io.IOException;


public class ControladorRed {
    private final RedBayesiana modelo;
    private final VistaRed vista;
    private final ILectorArchivos lector;

    public ControladorRed(RedBayesiana modelo, VistaRed vista, ILectorArchivos lector) {
        this.modelo = modelo;
        this.vista = vista;
        this.lector = lector;
    }

    public void inicializarRed(String rutaEstructura, String rutaProbabilidades) {
        try {
            // El lector inyecta los datos directamente en el modelo (eficiencia máxima)
            lector.construirEstructura(rutaEstructura, modelo);
            lector.cargarProbabilidades(rutaProbabilidades, modelo);
        } catch (IOException e) {
            vista.mostrarError("Error crítico en la carga de datos: " + e.getMessage());
        }
    }

    public void mostrarSistema() {
        // Muestra la estructura jerárquica y las tablas cargadas
        vista.mostrarEstructura(modelo);
        vista.mostrarTablas(modelo);
    }
}