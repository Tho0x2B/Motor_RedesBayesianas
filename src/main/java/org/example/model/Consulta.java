package org.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Consulta parseada del usuario: variable objetivo, valor opcional y evidencia.
 */
public final class Consulta {
    private final String variable;
    private final String valorOpcional;
    private final List<Asignacion> evidencia;

    public Consulta(String variable, String valorOpcional, List<Asignacion> evidencia) {
        if (variable == null || variable.trim().isEmpty()) {
            throw new IllegalArgumentException("Variable vacía");
        }
        this.variable = variable.trim();
        this.valorOpcional = (valorOpcional == null || valorOpcional.trim().isEmpty()) ? null : valorOpcional.trim();
        this.evidencia = (evidencia == null) ? new ArrayList<>() : new ArrayList<>(evidencia);
    }

    public String getVariable() {
        return variable;
    }

    public String getValorOpcional() {
        return valorOpcional;
    }

    public List<Asignacion> getEvidencia() {
        return evidencia;
    }
}
