package org.example.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LectorArchivosTexto implements ILectorArchivos {

    @Override
    public List<String> leerLineas(String rutaArchivo) throws IOException {
        List<String> lineas = new ArrayList<>();

        InputStream is = getClass().getClassLoader().getResourceAsStream(rutaArchivo);

        if (is == null) {
            throw new IOException("Archivo no encontrado en resources: " + rutaArchivo);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea.trim());
                }
            }
        }
        return lineas;
    }
}