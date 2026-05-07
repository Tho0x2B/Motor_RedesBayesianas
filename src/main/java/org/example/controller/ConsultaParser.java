package org.example.controller;

import org.example.model.ConsultaDTO;

import java.util.*;

public class ConsultaParser implements IConsultaParser {

    @Override
    public ConsultaDTO parse(String input) {
        if (input == null) throw new IllegalArgumentException("Consulta nula");
        String s = input.trim();
        if (s.isEmpty()) throw new IllegalArgumentException("Consulta vacía");

        int open = s.indexOf('(');
        int close = s.lastIndexOf(')');
        if (open < 0 || close < 0 || close <= open) {
            throw new IllegalArgumentException("Formato inválido. Usa P(...)");
        }

        String prefix = s.substring(0, open).trim();
        if (!prefix.equalsIgnoreCase("P")) {
            throw new IllegalArgumentException("Formato inválido. Usa P(...)");
        }

        String inside = s.substring(open + 1, close).trim();
        if (inside.isEmpty()) throw new IllegalArgumentException("Consulta vacía dentro de P(...)");

        String[] parts = inside.split("\\|", 2);
        String left = parts[0].trim();
        String evidencePart = parts.length > 1 ? parts[1].trim() : "";

        String variable;
        String valor = null;
        if (left.contains("=")) {
            String[] kv = left.split("=", 2);
            variable = kv[0].trim();
            valor = kv[1].trim();
        } else {
            variable = left.trim();
        }

        if (variable.isEmpty()) throw new IllegalArgumentException("Variable de consulta vacía");

        Map<String, String> evidencia = parseEvidencia(evidencePart);
        return new ConsultaDTO(variable, (valor != null && !valor.isEmpty()) ? valor : null, evidencia);
    }

    public static Map<String, String> parseEvidencia(String evidencePart) {
        Map<String, String> e = new LinkedHashMap<>();
        if (evidencePart == null) return e;
        String s = evidencePart.trim();
        if (s.isEmpty()) return e;

        // separadores ',' o ';'
        String[] items = s.split("[;,]");
        for (String raw : items) {
            String it = raw.trim();
            if (it.isEmpty()) continue;
            if (!it.contains("=")) {
                throw new IllegalArgumentException("Evidencia inválida: '" + it + "'. Usa Var=valor");
            }
            String[] kv = it.split("=", 2);
            String k = kv[0].trim();
            String v = kv[1].trim();
            if (k.isEmpty() || v.isEmpty()) {
                throw new IllegalArgumentException("Evidencia inválida: '" + it + "'. Usa Var=valor");
            }
            e.put(k, v);
        }
        return e;
    }
}
