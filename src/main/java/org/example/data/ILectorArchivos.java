package org.example.data;

import java.io.IOException;
import java.util.List;

public interface ILectorArchivos {
    /**
     * Lee un archivo y retorna sus líneas.
     * @param rutaArchivo La ubicación del archivo.
     * @return Lista de cadenas, donde cada cadena es una línea del archivo.
     * @throws IOException Si ocurre un error de lectura.
     */
    List<String> leerLineas(String rutaArchivo) throws IOException;
}