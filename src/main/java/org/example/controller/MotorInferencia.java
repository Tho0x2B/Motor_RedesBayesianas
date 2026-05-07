package org.example.controller;

import org.example.model.Nodo;
import org.example.model.RedBayesiana;

import java.util.*;

public class MotorInferencia {

    /**
     * Calcula la distribución P(X | E) por enumeración con backtracking.
     */
    public Map<String, Double> distribucionPosterior(RedBayesiana red, String variableConsulta, Map<String, String> evidencia) {
        if (variableConsulta == null || variableConsulta.trim().isEmpty()) {
            throw new IllegalArgumentException("Variable de consulta vacía");
        }
        if (!red.contieneNodo(variableConsulta)) {
            throw new IllegalArgumentException("Variable de consulta no existe en la red: " + variableConsulta);
        }

        evidencia = (evidencia == null) ? new LinkedHashMap<>() : new LinkedHashMap<>(evidencia);

        // Validación básica: evidencia con variables que existan
        for (String k : evidencia.keySet()) {
            if (!red.contieneNodo(k)) {
                throw new IllegalArgumentException("La evidencia contiene una variable desconocida: " + k);
            }
        }

        List<Nodo> orden = red.getNodosEnOrdenTopologico();
        Nodo q = red.obtenerNodo(variableConsulta);
        Set<String> dominioQ = red.extraerDominio(q);
        if (dominioQ.isEmpty()) {
            throw new IllegalStateException("No se pudo inferir el dominio de la variable: " + variableConsulta);
        }

        Map<String, Double> distNoNorm = new LinkedHashMap<>();
        double total = 0.0;
        for (String vq : dominioQ) {
            Map<String, String> asg = new LinkedHashMap<>(evidencia);
            asg.put(variableConsulta, vq);
            double p = probabilidadConjuntaPorBacktracking(red, orden, asg);
            distNoNorm.put(vq, p);
            total += p;
        }

        Map<String, Double> dist = new LinkedHashMap<>();
        if (total == 0.0) {
            // evidencia imposible o tabla incompleta
            for (String vq : dominioQ) dist.put(vq, 0.0);
            return dist;
        }
        for (Map.Entry<String, Double> e : distNoNorm.entrySet()) {
            dist.put(e.getKey(), e.getValue() / total);
        }
        return dist;
    }

    /**
     * Calcula P(X=valor | E) usando la distribución posterior.
     */
    public double probabilidadPosterior(RedBayesiana red, String variable, String valor, Map<String, String> evidencia) {
        Map<String, Double> dist = distribucionPosterior(red, variable, evidencia);
        Double p = dist.get(valor);
        if (p == null) {
            throw new IllegalArgumentException("El valor '" + valor + "' no pertenece al dominio observado de '" + variable + "'");
        }
        return p;
    }

    /**
     * Backtracking sobre variables NO asignadas. Multiplica factores locales P(Y | Padres(Y)).
     *
     * Si una variable binaria no asignada, esto efectivamente recorre 2^n combinaciones.
     */
    private double probabilidadConjuntaPorBacktracking(RedBayesiana red, List<Nodo> ordenTopologico, Map<String, String> asignacionParcial) {
        Map<String, String> asg = new LinkedHashMap<>(asignacionParcial);
        Map<String, Double> memo = new HashMap<>();
        return backtrack(red, ordenTopologico, 0, asg, memo);
    }

    private double backtrack(RedBayesiana red, List<Nodo> orden, int idx, Map<String, String> asg, Map<String, Double> memo) {
        if (idx >= orden.size()) return 1.0;

        String key = claveMemo(orden, idx, asg);
        Double cached = memo.get(key);
        if (cached != null) return cached;

        Nodo nodo = orden.get(idx);
        String nombre = nodo.getNombre();

        double res;
        if (asg.containsKey(nombre)) {
            res = ramaAsignada(red, orden, idx, asg, nodo, memo);
        } else {
            res = ramaNoAsignada(red, orden, idx, asg, nodo, memo);
        }

        memo.put(key, res);
        return res;
    }

    private double ramaAsignada(RedBayesiana red, List<Nodo> orden, int idx, Map<String, String> asg, Nodo nodo, Map<String, Double> memo) {
        double pLocal = nodo.probabilidadDada(asg);
        if (pLocal == 0.0) return 0.0;
        return pLocal * backtrack(red, orden, idx + 1, asg, memo);
    }

    private double ramaNoAsignada(RedBayesiana red, List<Nodo> orden, int idx, Map<String, String> asg, Nodo nodo, Map<String, Double> memo) {
        Set<String> dominio = dominioDe(red, nodo);
        double suma = 0.0;

        String nombre = nodo.getNombre();
        for (String valor : dominio) {
            asg.put(nombre, valor);
            double pLocal = nodo.probabilidadDada(asg);
            if (pLocal != 0.0) {
                suma += pLocal * backtrack(red, orden, idx + 1, asg, memo);
            }
            asg.remove(nombre);
        }
        return suma;
    }

    /**
     * Crea una clave compacta y estable para memoización.
     * Solo incluye variables hasta idx-1 (ya decididas en este punto), porque las futuras aún no existen.
     */
    private String claveMemo(List<Nodo> orden, int idx, Map<String, String> asg) {
        StringBuilder sb = new StringBuilder(64);
        sb.append(idx).append('|');
        for (int i = 0; i < idx; i++) {
            String var = orden.get(i).getNombre();
            String val = asg.get(var);
            // En topológico, todas las variables previas deberían estar asignadas
            sb.append(var).append('=').append(val).append(';');
        }
        return sb.toString();
    }

    private Set<String> dominioDe(RedBayesiana red, Nodo nodo) {
        Set<String> dominio = red.extraerDominio(nodo);
        if (dominio == null || dominio.isEmpty()) {
            throw new IllegalStateException("No se pudo inferir dominio de: " + nodo.getNombre());
        }
        return dominio;
    }
}