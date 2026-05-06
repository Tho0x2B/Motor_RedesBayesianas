package src.main.java.org.example.app;


import src.main.java.org.example.controller.ControladorRed;
import src.main.java.org.example.data.ILectorArchivos;
import src.main.java.org.example.data.LectorArchivosTexto;
import src.main.java.org.example.model.RedBayesiana;
import src.main.java.org.example.view.VistaRed;

public class Main {
    public static void main(String[] args) {
        RedBayesiana modelo = new RedBayesiana();
        VistaRed vista = new VistaRed();
        ILectorArchivos lector = new LectorArchivosTexto();
        ControladorRed controlador = new ControladorRed(modelo, vista, lector);

        controlador.cargarEstructuraDesdeArchivo("estructura.txt");
        controlador.cargarProbabilidadesDesdeArchivo("probabilidades.txt");

        controlador.ejecutarReporte();
    }
}