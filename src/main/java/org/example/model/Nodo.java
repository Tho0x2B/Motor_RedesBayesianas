package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nodo {
    private String nombre;
    private List<Nodo> padres;
    private Map<String, Double> tablaProbabilidad;

    public Nodo(String nombre) {
        this.nombre = nombre;
        this.padres = new ArrayList<>();
        this.tablaProbabilidad = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void agregarPadre(Nodo padre) {
        if (!this.padres.contains(padre)) {
            this.padres.add(padre);
        }
    }

    public List<Nodo> getPadres() {
        return padres;
    }

    public void agregarProbabilidad(String condicion, Double probabilidad) {
        this.tablaProbabilidad.put(condicion, probabilidad);
    }

    public Map<String, Double> getTablaProbabilidad() {
        return tablaProbabilidad;
    }
}