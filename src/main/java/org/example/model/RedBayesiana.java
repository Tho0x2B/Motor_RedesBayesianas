package org.example.model;

import java.util.*;

/**
 * Modelo de red bayesiana: grafo dirigido acíclico con nodos y CPTs.
 */
public class RedBayesiana {
    // LinkedHashMap el String es el nombre de la variable
    private Map<String, Nodo> nodos;

    public RedBayesiana() {
        this.nodos = new LinkedHashMap<>();
    }

    public Nodo obtenerOCrearNodo(String nombre) {
        nodos.putIfAbsent(nombre, new Nodo(nombre));
        return nodos.get(nombre);
    }

    public Nodo obtenerNodo(String nombre) {
        return nodos.get(nombre);
    }

    public boolean contieneNodo(String nombre) {
        return nodos.containsKey(nombre);
    }

    public void agregarDependencia(String nombrePadre, String nombreHijo) {
        Nodo padre = obtenerOCrearNodo(nombrePadre);
        Nodo hijo = obtenerOCrearNodo(nombreHijo);

        hijo.vincularPredecesor(padre);
    }

    public Collection<Nodo> getNodos() {
        return nodos.values();
    }

    /**
     * Devuelve los nodos en un orden topológico (padres antes que hijos).
     * Lanza excepción si detecta un ciclo.
     */
    public List<Nodo> getNodosEnOrdenTopologico() {
        Map<Nodo, Integer> indegree = new LinkedHashMap<>();
        for (Nodo n : nodos.values()) {
            indegree.put(n, n.getPadres().size());
        }

        ArrayDeque<Nodo> q = new ArrayDeque<>();
        for (Map.Entry<Nodo, Integer> e : indegree.entrySet()) {
            if (e.getValue() == 0)
                q.add(e.getKey());
        }

        List<Nodo> orden = new ArrayList<>(nodos.size());
        while (!q.isEmpty()) {
            Nodo u = q.removeFirst();
            orden.add(u);
            for (Nodo v : u.getHijos()) {
                int d = indegree.get(v) - 1;
                indegree.put(v, d);
                if (d == 0)
                    q.addLast(v);
            }
        }

        if (orden.size() != nodos.size()) {
            throw new IllegalStateException("La red contiene un ciclo o dependencias inconsistentes");
        }
        return orden;
    }

    /**
     * Extrae el dominio observado de una variable a partir de su CPT.
     * Se asume que cada clave de la CPT es una lista de asignaciones 'Var=valor'
     * separadas por ';'.
     */
    public Set<String> extraerDominio(String nombreVariable) {
        Nodo n = obtenerNodo(nombreVariable);
        if (n == null)
            return Collections.emptySet();
        return extraerDominio(n);
    }

    public Set<String> extraerDominio(Nodo nodo) {
        Set<String> dom = new LinkedHashSet<>();
        for (String condicion : nodo.getTablaProbabilidad().keySet()) {
            Map<String, String> asg = Nodo.parseAsignaciones(condicion);
            String v = asg.get(nodo.getNombre());
            if (v != null)
                dom.add(v);
        }
        return dom;
    }
}
