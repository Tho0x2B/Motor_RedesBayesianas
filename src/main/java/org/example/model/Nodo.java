package org.example.model;

import java.util.*;

/**
 * Nodo de una red bayesiana con relaciones y tabla de probabilidad condicional (CPT).
 */
public class Nodo {
    private String nombre;
    private List<Nodo> padres;
    private List<Nodo> hijos;
    // la clave es la condición completa (ej:
    // "Rain=heavy;Maintenance=no;Train=delayed") y el valor es la probabilidad
    // P(this|padres)
    private Map<String, Double> tablaProbabilidad;

    public Nodo(String nombre) {
        this.nombre = nombre;
        this.padres = new ArrayList<>();
        this.hijos = new ArrayList<>();
        // LinkedHashMap preserva el orden exacto en el que se leen y guardan las
        // probabilidades
        this.tablaProbabilidad = new LinkedHashMap<>();
    }

    public void vincularPredecesor(Nodo padre) {
        if (!this.padres.contains(padre)) {
            this.padres.add(padre);
            if (!padre.getHijos().contains(this)) {
                padre.getHijos().add(this);
            }
        }
    }

    public void agregarProbabilidad(String condicion, Double probabilidad) {
        this.tablaProbabilidad.put(condicion, probabilidad);
    }

    public String getNombre() {
        return nombre;
    }

    public List<Nodo> getPadres() {
        return padres;
    }

    public List<Nodo> getHijos() {
        return hijos;
    }

    public Map<String, Double> getTablaProbabilidad() {
        return tablaProbabilidad;
    }

    /**
     * Parsea una condición del archivo (ej:
     * "Rain=heavy;Maintenance=no;Train=delayed") a un mapa.
     */
    public static Map<String, String> parseAsignaciones(String condicion) {
        Map<String, String> map = new LinkedHashMap<>();
        if (condicion == null)
            return map;
        String s = condicion.trim();
        if (s.isEmpty())
            return map;

        String[] parts = s.split(";");
        for (String raw : parts) {
            String t = raw.trim();
            if (t.isEmpty())
                continue;
            if (!t.contains("=")) {
                // Si alguna vez llega sin '=', lo ignoramos para evitar falsos matches.
                continue;
            }
            String[] kv = t.split("=", 2);
            String k = kv[0].trim();
            String v = kv[1].trim();
            if (!k.isEmpty() && !v.isEmpty()) {
                map.put(k, v);
            }
        }
        return map;
    }

    /**
     * Retorna P(this = valor | asignación de padres) desde la CPT.
     * La asignación debe contener el valor de este nodo y de todos los padres.
     */
    public double probabilidadDada(Map<String, String> asignacion) {
        String myVal = asignacion.get(nombre);
        if (myVal == null) {
            throw new IllegalArgumentException("Falta valor para nodo '" + nombre + "' en la asignación");
        }
        for (Nodo p : padres) {
            if (!asignacion.containsKey(p.getNombre())) {
                throw new IllegalArgumentException(
                        "Falta valor para padre '" + p.getNombre() + "' al evaluar '" + nombre + "'");
            }
        }

        // Matching exacto: buscamos la fila cuya lista de asignaciones coincide para
        // este nodo y sus padres.
        for (Map.Entry<String, Double> entry : tablaProbabilidad.entrySet()) {
            Map<String, String> fila = parseAsignaciones(entry.getKey());
            if (!myVal.equals(fila.get(nombre)))
                continue;

            boolean ok = true;
            for (Nodo p : padres) {
                String pv = asignacion.get(p.getNombre());
                if (!Objects.equals(pv, fila.get(p.getNombre()))) {
                    ok = false;
                    break;
                }
            }
            if (ok)
                return entry.getValue();
        }
        return 0.0; // si no hay fila coincidente, asumimos 0
    }
}
