package org.example.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.example.model.Nodo;
import org.example.model.RedBayesiana;


public class LectorArchivosTexto implements ILectorArchivos {

    @Override
    public void construirEstructura(String ruta, RedBayesiana red) throws IOException {
        try (BufferedReader br = getReader(ruta)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    System.out.println("DEBUG - Leyendo: [" + linea + "]");

                    String[] partes = linea.split(",");
                    if (partes.length == 2) {
                        red.agregarDependencia(partes[0].trim(), partes[1].trim());
                    } else {
                        System.out.println("DEBUG - Línea ignorada (no detectó la coma): [" + linea + "]");
                    }
                }
            }
        }

    }

    @Override
    public void cargarProbabilidades(String ruta, RedBayesiana red) throws IOException {
        try (BufferedReader br = getReader(ruta)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    String[] partes = linea.split(",");
                    if (partes.length == 3) {
                        Nodo nodo = red.obtenerOCrearNodo(partes[0].trim());
                        // Se guarda en el orden exacto de lectura
                        nodo.agregarProbabilidad(partes[1].trim(), Double.parseDouble(partes[2].trim()));
                    }
                }
            }
        }
    }

    private BufferedReader getReader(String ruta) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(ruta);
        if (is == null) throw new IOException("Archivo no encontrado: " + ruta);
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    }
}