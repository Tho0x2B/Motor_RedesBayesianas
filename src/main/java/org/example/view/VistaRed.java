package src.main.java.org.example.view;

import src.main.java.org.example.model.Nodo;
import src.main.java.org.example.model.RedBayesiana;

import java.util.List;
import java.util.Map;

public class VistaRed {

    public void mostrarEstructura(RedBayesiana red) {
        System.out.println("--- ESTRUCTURA DE LA RED BAYESIANA ---");
        for (Nodo nodo : red.getNodos()) {
            System.out.print("Nodo: " + nodo.getNombre());
            List<Nodo> padres = nodo.getPadres();

            if (padres.isEmpty()) {
                System.out.println(" | Predecesores: (Raíz - Ninguno)");
            } else {
                System.out.print(" | Predecesores: ");
                for (int i = 0; i < padres.size(); i++) {
                    System.out.print(padres.get(i).getNombre() + (i < padres.size() - 1 ? ", " : ""));
                }
                System.out.println();
            }
        }
        System.out.println();
    }

    public void mostrarTablas(RedBayesiana red) {
        System.out.println("--- TABLAS DE PROBABILIDAD ---");
        for (Nodo nodo : red.getNodos()) {
            System.out.println("Tabla para el nodo: [" + nodo.getNombre() + "]");
            Map<String, Double> tabla = nodo.getTablaProbabilidad();

            if (tabla.isEmpty()) {
                System.out.println("  (Tabla vacía o no cargada)");
            } else {
                for (Map.Entry<String, Double> entrada : tabla.entrySet()) {
                    System.out.println("  Condición: " + entrada.getKey() + " -> P = " + entrada.getValue());
                }
            }
            System.out.println("----------------------------------");
        }
    }

    public void mostrarError(String mensaje) {
        System.err.println("Error: " + mensaje);
    }
}