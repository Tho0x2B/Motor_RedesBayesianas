package org.example.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Nodo {
    private String nombre;
    private List<Nodo> padres;
    private List<Nodo> hijos;
    private Map<String, Double> tablaProbabilidad;

    public Nodo(String nombre) {
        this.nombre = nombre;
        this.padres = new ArrayList<>();
        this.hijos = new ArrayList<>();
        // LinkedHashMap preserva el orden exacto en el que se leen y guardan las probabilidades
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

    public String getNombre() { return nombre; }
    public List<Nodo> getPadres() { return padres; }
    public List<Nodo> getHijos() { return hijos; }
    public Map<String, Double> getTablaProbabilidad() { return tablaProbabilidad; }
}