# Motor_RedesBayesianas

Implementación de un Motor de Inferencia por Enumeración.

## Requisitos

- Java 17 o superior
- Maven 3.6 o superior
- Terminal o consola

## Estructura de archivos de entrada

La aplicacion requiere dos archivos de texto:

1.  **Estructura**: Define las relaciones entre variables. Formato: `Variable: Padre1, Padre2`
2.  **Probabilidades**: Define las tablas de probabilidad condicional. Formato: `Variable(valor|Padre1=valor,Padre2=valor)=probabilidad`

## Construccion del proyecto con Maven

### Windows

```cmd
cd ruta/del/proyecto
mvn clean compile
mvn exec:java -Dexec.mainClass="org.example.app.Main"
```

### Linux / macOS
```bash
cd ruta/del/proyecto
mvn clean compile
mvn exec:java -Dexec.mainClass="org.example.app.Main"
```