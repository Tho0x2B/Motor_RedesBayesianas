package org.example.controller;

import org.example.model.Consulta;

/**
 * Abstracción para parsear consultas del usuario (SRP) y permitir inyección de dependencias (DIP).
 */
public interface IConsultaParser {

    Consulta parse(String input);
}
