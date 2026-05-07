package org.example.app;

import org.example.controller.ConsultaParser;
import org.example.controller.ControladorRed;
import org.example.controller.MotorInferencia;
import org.example.data.ILectorArchivos;
import org.example.data.LectorArchivosTexto;
import org.example.model.RedBayesiana;
import org.example.view.VistaRed;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Instanciación de componentes base (Grafo, UI, Lógica)
        RedBayesiana modelo = new RedBayesiana();
        VistaRed vista = new VistaRed();

        // 2. Instanciación de Servicios
        ILectorArchivos lector = new LectorArchivosTexto();
        ConsultaParser parser = new ConsultaParser();
        MotorInferencia motor = new MotorInferencia();

        // 3. Inyección de Dependencias en el Controlador
        ControladorRed controlador = new ControladorRed(modelo, vista, lector, parser, motor);

        // 4. Inicialización
        controlador.inicializar("estructura.txt", "probabilidades.txt");

        // 5. Bucle de aplicación
        ejecutarTerminal(controlador);
    }

    private static void ejecutarTerminal(ControladorRed controlador) {
        System.out.println("\n==============================================");
        System.out.println("Motor de Inferencia Bayesiana Activo");
        System.out.println("Formato: P(Variable=valor | Evidencia1=v1, Evidencia2=v2)");
        System.out.println("Escribe 'salir' para terminar.");
        System.out.println("==============================================");

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String input = sc.nextLine();
                if (input == null || input.trim().equalsIgnoreCase("salir")) {
                    break;
                }
                if (!input.trim().isEmpty()) {
                    controlador.procesarConsulta(input);
                }
            }
        }
    }
}