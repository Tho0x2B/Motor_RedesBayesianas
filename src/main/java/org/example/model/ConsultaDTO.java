package org.example.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * DTO para transportar una consulta parseada.
 * Evita usar record (y evita declararlo dentro de una interface).
 */
public final class ConsultaDTO {
    private final String variable;
    private final String valorOpcional;
    private final Map<String, String> evidencia;

    public ConsultaDTO(String variable, String valorOpcional, Map<String, String> evidencia) {
        if (variable == null || variable.trim().isEmpty()) {
            throw new IllegalArgumentException("Variable vacía");
        }
        this.variable = variable.trim();
        this.valorOpcional = (valorOpcional == null || valorOpcional.trim().isEmpty()) ? null : valorOpcional.trim();
        Map<String, String> ev = (evidencia == null) ? new LinkedHashMap<>() : new LinkedHashMap<>(evidencia);
        this.evidencia = Collections.unmodifiableMap(ev);
    }

    public String getVariable() {
        return variable;
    }

    public String getValorOpcional() {
        return valorOpcional;
    }

    public Map<String, String> getEvidencia() {
        return evidencia;
    }
}

