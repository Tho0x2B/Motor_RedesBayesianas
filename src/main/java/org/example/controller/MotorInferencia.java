package org.example.controller;

import org.example.model.Nodo;
import org.example.model.RedBayesiana;

import java.util.*;

public class MotorInferencia {

    public Map<String, Double> distribucionPosterior(RedBayesiana red, String variableConsulta,
            Map<String, String> evidencia) {
        validarParametros(red, variableConsulta, evidencia);

        List<Nodo> orden = red.getNodosEnOrdenTopologico();
        Set<String> dominioQ = red.extraerDominio(variableConsulta);

        // distribucion de la probabilidad posterior de variable dado la evidencia
        Map<String, Double> distribucion = new LinkedHashMap<>();
        // mantiene las asignaciones de valores a todas las variables, inicialmente a
        // las de la evidencia, y luego se va modificando a medida que se recorre el
        // árbol de recursión
        Map<String, String> estadoGlobal = new HashMap<>(evidencia != null ? evidencia : Collections.emptyMap());
        double sumaTotal = 0.0;

        // 1. Calcular distribuciones no normalizadas
        for (String valorPosible : dominioQ) {
            estadoGlobal.put(variableConsulta, valorPosible);

            // Usamos un único mapa de estado para todo el árbol de recursión (Rápido y
            // eficiente en RAM)
            double probabilidad = backtrack(red, orden, 0, estadoGlobal);

            distribucion.put(valorPosible, probabilidad);
            sumaTotal += probabilidad;

            estadoGlobal.remove(variableConsulta);
        }

        // 2. Normalización (Constante Alfa)
        if (sumaTotal > 0.0) {
            for (Map.Entry<String, Double> entrada : distribucion.entrySet()) {
                distribucion.put(entrada.getKey(), entrada.getValue() / sumaTotal);
            }
        } else {
            // Manejo de evidencia imposible
            dominioQ.forEach(v -> distribucion.put(v, 0.0));
        }

        return distribucion;
    }

    public double probabilidadPosterior(RedBayesiana red, String variable, String valor,
            Map<String, String> evidencia) {
        Map<String, Double> dist = distribucionPosterior(red, variable, evidencia);
        return dist.getOrDefault(valor, 0.0);
    }

    private double backtrack(RedBayesiana red, List<Nodo> orden, int idx, Map<String, String> estado) {
        // Caso base: se llegó al ultimo nodo del orden topológico
        if (idx >= orden.size())
            return 1.0;

        Nodo nodoActual = orden.get(idx);
        String nombre = nodoActual.getNombre();
        // Variable de consulta o evidencia ya asignada
        if (estado.containsKey(nombre)) {
            double pLocal = nodoActual.probabilidadDada(estado);
            if (pLocal == 0.0)
                return 0.0;
            return pLocal * backtrack(red, orden, idx + 1, estado);
        }

        // Variables ocultas
        double sumaProbabilidades = 0.0;
        for (String valor : red.extraerDominio(nodoActual)) {
            // Asignar uno de los valores del dominio, temporalmente
            estado.put(nombre, valor);

            double pLocal = nodoActual.probabilidadDada(estado);
            if (pLocal != 0.0) {
                // Continuar con el subarbol
                sumaProbabilidades += pLocal * backtrack(red, orden, idx + 1, estado);
            }

            estado.remove(nombre);
        }
        return sumaProbabilidades;
    }

    private void validarParametros(RedBayesiana red, String variable, Map<String, String> evidencia) {
        if (variable == null || !red.contieneNodo(variable)) {
            throw new IllegalArgumentException("Variable de consulta inválida o inexistente: " + variable);
        }
        if (evidencia != null) {
            evidencia.keySet().forEach(k -> {
                if (!red.contieneNodo(k))
                    throw new IllegalArgumentException("Evidencia desconocida: " + k);
            });
        }
    }
}