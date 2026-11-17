package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class EncuestaController {

    // Nombres con los que guardamos cada dato en la sesión.
    private static final String JUGADOR = "jugador";
    private static final String POSICION = "posicion";
    private static final String NIVEL = "nivel";
    private static final String MONTURA = "montura";
    private static final String RESOLUCION = "resolucion";
    private static final String ACCESORIOS = "accesorios";

    // Al entrar en la web empezamos una encuesta nueva: borramos la sesión anterior.
    @GetMapping("/")
    public String inicio(HttpSession session) {
        session.invalidate();
        return "redirect:/paso1";
    }

    // Cada pantalla se dibuja leyendo lo que ya hay en la sesión.
    // Así funciona el botón "Atrás": los formularios aparecen con los valores guardados.
    @GetMapping("/paso1")
    public String verPaso1(HttpSession session, Model model) {
        cargar(session, model);
        return "paso1";
    }

    @GetMapping("/paso2")
    public String verPaso2(HttpSession session, Model model) {
        cargar(session, model);
        return "paso2";
    }

    @GetMapping("/paso3")
    public String verPaso3(HttpSession session, Model model) {
        cargar(session, model);
        return "paso3";
    }

    @GetMapping("/resultado")
    public String verResultado(HttpSession session, Model model) {
        List<String> resumen = new ArrayList<>();
        resumen.add("Jugador: " + dato(session, JUGADOR));
        resumen.add("Posición: " + dato(session, POSICION));
        resumen.add("Nivel: " + dato(session, NIVEL));
        resumen.add("Montura de cámara: " + dato(session, MONTURA));
        resumen.add("Calidad de grabación: " + dato(session, RESOLUCION));

        @SuppressWarnings("unchecked")
        List<String> accesorios = (List<String>) session.getAttribute(ACCESORIOS);
        resumen.add("Accesorios extra: "
                + (accesorios == null || accesorios.isEmpty() ? "Ninguno" : String.join(", ", accesorios)));

        model.addAttribute("datos", resumen);
        return "resultado";
    }

    // --- GUARDADO DE CADA PASO (la validación siempre está aquí, nunca en el HTML) ---

    @PostMapping("/paso1")
    public String guardarPaso1(
            @RequestParam(value = "jugador", defaultValue = "") String jugador,
            @RequestParam(value = "posicion", defaultValue = "") String posicion,
            @RequestParam(value = "nivel", defaultValue = "") String nivel,
            HttpSession session, Model model) {

        if (jugador.isBlank() || posicion.isBlank() || nivel.isBlank()) {
            model.addAttribute("error", "Por favor, rellena todos los campos del paso 1.");
            model.addAttribute(JUGADOR, jugador);
            model.addAttribute(POSICION, posicion);
            model.addAttribute(NIVEL, nivel);
            return "paso1";
        }

        session.setAttribute(JUGADOR, jugador.trim());
        session.setAttribute(POSICION, posicion);
        session.setAttribute(NIVEL, nivel);
        return "redirect:/paso2";
    }

    @PostMapping("/paso2")
    public String guardarPaso2(
            @RequestParam(value = "montura", defaultValue = "") String montura,
            @RequestParam(value = "resolucion", defaultValue = "") String resolucion,
            HttpSession session, Model model) {

        if (montura.isBlank() || resolucion.isBlank()) {
            model.addAttribute("error", "Por favor, selecciona montura y resolución.");
            model.addAttribute(MONTURA, montura);
            model.addAttribute(RESOLUCION, resolucion);
            return "paso2";
        }

        session.setAttribute(MONTURA, montura);
        session.setAttribute(RESOLUCION, resolucion);
        return "redirect:/paso3";
    }

    @PostMapping("/paso3")
    public String guardarPaso3(
            @RequestParam(value = "accesorios", required = false) List<String> accesorios,
            HttpSession session) {

        session.setAttribute(ACCESORIOS, accesorios == null ? List.of() : accesorios);
        return "redirect:/resultado";
    }

    // --- AYUDAS ---

    // Copia los datos de la sesión al modelo para que Thymeleaf pueda mostrarlos.
    private void cargar(HttpSession session, Model model) {
        model.addAttribute(JUGADOR, dato(session, JUGADOR));
        model.addAttribute(POSICION, dato(session, POSICION));
        model.addAttribute(NIVEL, dato(session, NIVEL));
        model.addAttribute(MONTURA, dato(session, MONTURA));
        model.addAttribute(RESOLUCION, dato(session, RESOLUCION));

        @SuppressWarnings("unchecked")
        List<String> accesorios = (List<String>) session.getAttribute(ACCESORIOS);
        model.addAttribute(ACCESORIOS, accesorios == null ? List.of() : accesorios);
    }

    private String dato(HttpSession session, String nombre) {
        Object valor = session.getAttribute(nombre);
        return valor == null ? "" : valor.toString();
    }
}
