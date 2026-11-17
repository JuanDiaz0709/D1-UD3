# D1-UD3 · Quizz con sesión

Versión mejorada del Quizz de la UD2. El cuestionario sigue teniendo tres pasos y una pantalla de resumen, pero ya no arrastra los datos con campos `hidden`: ahora se guardan en la **sesión HTTP** del servidor.

## Qué cambia respecto al Quizz de la UD2

| UD2 | UD3 |
|---|---|
| Cada formulario escondía los datos de los pasos anteriores con `<input type="hidden">` | No hay ningún campo oculto |
| El controlador recibía hasta 10 parámetros en cada petición | El controlador lee y escribe en `HttpSession` |
| Al volver atrás, los valores se restauraban desde los campos ocultos | Al volver atrás, los valores se leen de la sesión |

## Estructura

- `src/main/java/com/example/demo/EncuestaController.java`: controlador y validación.
- `src/main/resources/templates/`: `paso1.html`, `paso2.html`, `paso3.html` y `resultado.html`.
- `build.gradle`: dependencias de Spring Boot (Thymeleaf y WebMVC).

## Ejecutar

Necesitas JDK 21. Desde esta carpeta:

```bat
gradlew.bat bootRun
```

Abre `http://localhost:8080`.

## Recorrido

1. **Paso 1**: nombre, posición (desplegable) y nivel (radio).
2. **Paso 2**: montura (radio) y resolución (desplegable). Botón *Atrás*.
3. **Paso 3**: accesorios (casillas de verificación, selección múltiple). Botón *Atrás*.
4. **Resultado**: lista con todos los datos, generada con `th:each` dentro de un `<ol>`.

La validación está solo en el controlador. En `/` se invalida la sesión y empieza una encuesta nueva.
