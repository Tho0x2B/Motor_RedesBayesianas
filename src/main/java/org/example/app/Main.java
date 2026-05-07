package org.example.app;

import org.example.controller.ConsultaParser;
import org.example.controller.ControladorRed;
import org.example.controller.IConsultaParser;
import org.example.data.ILectorArchivos;
import org.example.data.LectorArchivosTexto;
import org.example.model.RedBayesiana;
import org.example.view.VistaRed;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RedBayesiana modelo = new RedBayesiana();

        VistaRed vista = new VistaRed();

        ILectorArchivos lector = new LectorArchivosTexto();
        IConsultaParser consultaParser = new ConsultaParser();

        ControladorRed controlador = new ControladorRed(modelo, vista, lector, consultaParser);

        controlador.inicializarRed("estructura.txt", "probabilidades.txt");

        controlador.mostrarSistema();

        System.out.println();
        System.out.println("==============================================");
        System.out.println("Modo consulta (enumeración con backtracking)");
        System.out.println("Escribe consultas como:");
        System.out.println("  P(Train=delayed | Rain=heavy, Maintenance=no)");
        System.out.println("  P(Appointment | Rain=light)");
        System.out.println("Escribe 'salir' para terminar.");
        System.out.println("==============================================");

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String line = sc.nextLine();
                if (line == null) break;
                line = line.trim();
                if (line.equalsIgnoreCase("salir") || line.equalsIgnoreCase("exit")) {
                    break;
                }
                if (line.isEmpty()) continue;
                controlador.resolverConsultaPorEnumeracion(line);
            }
        }
    }
}