package org.example.view;

import org.example.model.Nodo;
import org.example.model.RedBayesiana;

import java.util.Map;

import java.util.*;


/**
 * Vista por consola para inspeccionar la red y mostrar resultados de inferencia.
 */
public class VistaRed {

    public void mostrarEstructura(RedBayesiana red) {
        System.out.println("====================================================");
        System.out.println("   ESTRUCTURA DE LA RED (Dependencias Directas)     ");
        System.out.println("====================================================");

        for (Nodo nodo : red.getNodos()) {
            System.out.print("Nodo: [" + nodo.getNombre() + "]");

            if (nodo.getPadres().isEmpty()) {
                System.out.println(" -> (Nodo Raíz / Causa Primaria)");
            } else {
                System.out.print(" -> Predecesores: ");
                for (int i = 0; i < nodo.getPadres().size(); i++) {
                    String nombrePadre = nodo.getPadres().get(i).getNombre();
                    System.out.print(nombrePadre + (i < nodo.getPadres().size() - 1 ? ", " : ""));
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    public void mostrarTablas(RedBayesiana red) {
        System.out.println("====================================================");
        System.out.println("           TABLAS DE PROBABILIDAD (CPTs)            ");
        System.out.println("====================================================");

        for (Nodo nodo : red.getNodos()) {
            System.out.println("Variable: " + nodo.getNombre());
            Map<String, Double> tabla = nodo.getTablaProbabilidad();

            if (tabla.isEmpty()) {
                System.out.println("  [!] Sin valores de probabilidad cargados.");
            } else {
                tabla.forEach((condicion, valor) ->
                        System.out.printf("  P(%s | %s) = %.4f%n", nodo.getNombre(), condicion, valor)
                );
            }
            System.out.println("----------------------------------------------------");
        }
    }

    public void mostrarDistribucion(String variable, Map<String, String> evidencia, Map<String, Double> distribucion) {
        System.out.println("====================================================");
        System.out.println("INFERENCIA POR ENUMERACIÓN (backtracking)");
        System.out.println("Consulta: P(" + variable + (evidencia.isEmpty() ? ")" : " | " + evidencia + ")"));
        System.out.println("----------------------------------------------------");
        for (Map.Entry<String, Double> e : distribucion.entrySet()) {
            System.out.printf("  P(%s=%s | E) = %.6f%n", variable, e.getKey(), e.getValue());
        }
        System.out.println("====================================================");
    }

    public void mostrarProbabilidadPuntual(String variable, String valor, Map<String, String> evidencia, double p) {
        System.out.println("====================================================");
        System.out.println("INFERENCIA POR ENUMERACIÓN (backtracking)");
        System.out.println("Consulta: P(" + variable + "=" + valor + (evidencia.isEmpty() ? ")" : " | " + evidencia + ")"));
        System.out.println("----------------------------------------------------");
        System.out.printf("  Resultado: %.6f%n", p);
        System.out.println("====================================================");
    }

    public void mostrarError(String mensaje) {
        System.err.println("[ERROR SYSTEM]: " + mensaje);
    }
}
