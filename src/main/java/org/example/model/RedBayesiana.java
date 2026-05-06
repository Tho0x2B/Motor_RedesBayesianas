package src.main.java.org.example.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RedBayesiana {
    private Map<String, Nodo> nodos;

    public RedBayesiana() {
        this.nodos = new HashMap<>();
    }

    public Nodo obtenerOCrearNodo(String nombre) {
        nodos.putIfAbsent(nombre, new Nodo(nombre));
        return nodos.get(nombre);
    }

    public void agregarDependencia(String nombrePadre, String nombreHijo) {
        Nodo padre = obtenerOCrearNodo(nombrePadre);
        Nodo hijo = obtenerOCrearNodo(nombreHijo);
        hijo.agregarPadre(padre);
    }

    public Collection<Nodo> getNodos() {
        return nodos.values();
    }
}