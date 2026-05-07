package org.example.data;

import org.example.model.RedBayesiana;

import java.io.IOException;

public interface ILectorArchivos {
    void construirEstructura(String ruta, RedBayesiana red) throws IOException;
    void cargarProbabilidades(String ruta, RedBayesiana red) throws IOException;
}