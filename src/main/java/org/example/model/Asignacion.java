package org.example.model;

/**
 * Par variable=valor para evidencia o asignaciones puntuales.
 */
public final class Asignacion {
    private final String variable;
    private final String valor;

    public Asignacion(String variable, String valor) {
        if (variable == null || variable.trim().isEmpty()) {
            throw new IllegalArgumentException("Variable vacía");
        }
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("Valor vacío");
        }
        this.variable = variable.trim();
        this.valor = valor.trim();
    }

    public String getVariable() {
        return variable;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return variable + "=" + valor;
    }
}

