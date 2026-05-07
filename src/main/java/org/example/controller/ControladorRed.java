package org.example.controller;


import org.example.model.ConsultaDTO;
import org.example.data.ILectorArchivos;
import org.example.model.RedBayesiana;
import org.example.view.VistaRed;

import java.io.IOException;
import java.util.Map;


public class ControladorRed {
    private final RedBayesiana modelo;
    private final VistaRed vista;
    private final ILectorArchivos lector;
    private final IConsultaParser consultaParser;

    public ControladorRed(RedBayesiana modelo, VistaRed vista, ILectorArchivos lector, IConsultaParser consultaParser) {
        this.modelo = modelo;
        this.vista = vista;
        this.lector = lector;
        this.consultaParser = consultaParser;
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
        vista.mostrarEstructura(modelo);
        vista.mostrarTablas(modelo);
    }

    public void resolverConsultaPorEnumeracion(String consultaCruda) {
        try {
            ConsultaDTO c = consultaParser.parse(consultaCruda);
            MotorInferencia motor = new MotorInferencia();

            if (c.getValorOpcional() != null) {
                double p = motor.probabilidadPosterior(modelo, c.getVariable(), c.getValorOpcional(), c.getEvidencia());
                vista.mostrarProbabilidadPuntual(c.getVariable(), c.getValorOpcional(), c.getEvidencia(), p);
            } else {
                Map<String, Double> dist = motor.distribucionPosterior(modelo, c.getVariable(), c.getEvidencia());
                vista.mostrarDistribucion(c.getVariable(), c.getEvidencia(), dist);
            }
        } catch (RuntimeException ex) {
            vista.mostrarError(ex.getMessage());
        }
    }
}