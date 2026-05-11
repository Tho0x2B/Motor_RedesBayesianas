package org.example.app;

import org.example.controller.ConsultaParser;
import org.example.controller.ControladorRed;
import org.example.controller.MotorInferencia;
import org.example.data.ILectorArchivos;
import org.example.data.LectorArchivosTexto;
import org.example.model.RedBayesiana;
import org.example.view.VistaRed;

import java.util.Scanner;

/**
 * Punto de entrada de la aplicación de consola para el motor bayesiano.
 */
public class Main {
    public static void main(String[] args) {
        // 1. Instanciación de componentes base (Grafo, UI, Lógica)
        Scanner scanner = new Scanner(System.in);
        RedBayesiana modelo = new RedBayesiana();
        VistaRed vista = new VistaRed();

        // 2. Instanciación de Servicios
        ILectorArchivos lector = new LectorArchivosTexto();
        ConsultaParser parser = new ConsultaParser();
        MotorInferencia motor = new MotorInferencia();

        // 3. Inyección de Dependencias en el Controlador
        ControladorRed controlador = new ControladorRed(modelo, vista, lector, parser, motor);

        // 4. Inicialización
        Pair<String, String> rutas = ingresarRutaArchivos(scanner);
        controlador.inicializar(rutas.key(), rutas.value());

        // 5. Bucle de aplicación
        ejecutarTerminal(controlador, scanner);

        scanner.close();
    }

    private static void ejecutarTerminal(ControladorRed controlador, Scanner scanner) {
        System.out.println("\n==============================================");
        System.out.println("Motor de Inferencia Bayesiana Activo");
        System.out.println("Formato: P(Variable=valor | Evidencia1=v1, Evidencia2=v2)");
        System.out.println("Escribe 'salir' para terminar.");
        System.out.println("==============================================");

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (input == null || input.trim().equalsIgnoreCase("salir")) {
                break;
            }
            if (!input.trim().isEmpty()) {
                controlador.procesarConsulta(input);
            }
        }
    }

    /**
     * Par genérico usado para retornar dos rutas en una sola llamada.
     */
    public record Pair<K, V>(K key, V value) {
    }

    private static Pair<String, String> ingresarRutaArchivos(Scanner scanner) {
        System.out.print("Ruta del archivo de estructura: ");
        String estructura = scanner.nextLine().trim();
        System.out.print("Ruta del archivo de probabilidades: ");
        String probabilidades = scanner.nextLine().trim();
        return new Pair<>(estructura, probabilidades);

    }
}
