package src.main.java.org.example.data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LectorArchivosTexto implements ILectorArchivos {

    @Override
    public List<String> leerLineas(String rutaArchivo) throws IOException {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Ignorar líneas vacías para evitar errores de parseo
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea.trim());
                }
            }
        }
        return lineas;
    }
}