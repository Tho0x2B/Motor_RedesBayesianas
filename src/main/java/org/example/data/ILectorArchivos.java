package org.example.data;

import org.example.model.RedBayesiana;

import java.io.IOException;

/**
 * Contrato para cargar una red bayesiana desde archivos de entrada.
 */
public interface ILectorArchivos {
    void construirEstructura(String ruta, RedBayesiana red) throws IOException;
    void cargarProbabilidades(String ruta, RedBayesiana red) throws IOException;
}
