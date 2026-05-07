package org.example.controller;

import org.example.model.ConsultaDTO;

/**
 * Abstracción para parsear consultas del usuario (SRP) y permitir inyección de dependencias (DIP).
 */
public interface IConsultaParser {

    ConsultaDTO parse(String input);
}
