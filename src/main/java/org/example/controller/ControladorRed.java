package org.example.controller;

import org.example.data.ILectorArchivos;
import org.example.model.Asignacion;
import org.example.model.Consulta;
import org.example.model.RedBayesiana;
import org.example.view.VistaRed;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ControladorRed {
    private final RedBayesiana modelo;
    private final VistaRed vista;
    private final ILectorArchivos lector;
    private final ConsultaParser parser;
    private final MotorInferencia motor; // Dependencia inyectada

    public ControladorRed(RedBayesiana modelo, VistaRed vista, ILectorArchivos lector,
            ConsultaParser parser, MotorInferencia motor) {
        this.modelo = modelo;
        this.vista = vista;
        this.lector = lector;
        this.parser = parser;
        this.motor = motor;
    }

    public void inicializar(String archivoEstructura, String archivoProbabilidades) {
        try {
            lector.construirEstructura(archivoEstructura, modelo);
            lector.cargarProbabilidades(archivoProbabilidades, modelo);
            vista.mostrarEstructura(modelo);
            vista.mostrarTablas(modelo);
        } catch (IOException e) {
            vista.mostrarError("Error crítico de I/O: " + e.getMessage());
        }
    }

    public void procesarConsulta(String comandoUsuario) {
        try {
            Consulta consulta = parser.parse(comandoUsuario);
            Map<String, String> evidencia = toMap(consulta.getEvidencia());

            String variable = consulta.getVariable();
            String valor = consulta.getValorOpcional();

            if (valor != null) {
                double p = motor.probabilidadPosterior(modelo, variable, valor, evidencia);
                vista.mostrarProbabilidadPuntual(variable, valor, evidencia, p);
            } else {
                Map<String, Double> distribucion = motor.distribucionPosterior(modelo, variable, evidencia);
                vista.mostrarDistribucion(variable, evidencia, distribucion);
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            vista.mostrarError("Comando inválido: " + e.getMessage());
        }
    }

    private Map<String, String> toMap(List<Asignacion> evidencia) {
        Map<String, String> m = new LinkedHashMap<>();
        if (evidencia == null)
            return m;
        for (Asignacion a : evidencia) {
            m.put(a.getVariable(), a.getValor());
        }
        return m;
    }
}