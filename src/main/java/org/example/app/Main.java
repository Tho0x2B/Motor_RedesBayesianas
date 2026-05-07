package org.example.app;

import org.example.controller.ControladorRed;
import org.example.data.ILectorArchivos;
import org.example.data.LectorArchivosTexto;
import org.example.model.RedBayesiana;
import org.example.view.VistaRed;

public class Main {
    public static void main(String[] args) {
        RedBayesiana modelo = new RedBayesiana();

        VistaRed vista = new VistaRed();

        ILectorArchivos lector = new LectorArchivosTexto();

        ControladorRed controlador = new ControladorRed(modelo, vista, lector);

        controlador.inicializarRed("estructura.txt", "probabilidades.txt");

        controlador.mostrarSistema();
    }
}