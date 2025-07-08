package com.tienda.fidelidad.model;

import java.util.Objects;

public class Cliente {
    private int id;
    private String nombre;
    private final String correo;
    private int puntos;
    private Nivel nivel;
    private static int nextId = 1;

    public Cliente(String nombre, String correo) {
        validarNombre(nombre);
        validarCorreo(correo);

        this.id = nextId++;
        this.nombre = nombre;
        this.correo = correo;
        this.puntos = 0;
        this.nivel = Nivel.BRONCE;
    }

    // Constructor alternativo, se mantiene por si es usado en otras capas.
    public Cliente(int id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.puntos = 0;
        this.nivel = Nivel.BRONCE;
    }

    // --- Métodos de validación privados ---

    /**
     * Valida el formato del nombre del cliente.
     * No puede ser nulo, vacío, ni contener el carácter '@'.
     */
    private void validarNombre(String nombre) {
        // Se usa Objects.requireNonNullElse para simplificar la comprobación de nulos y vacíos.
        if (Objects.requireNonNullElse(nombre, "").trim().isEmpty() || nombre.contains("@")) {
            throw new IllegalArgumentException("El formato del nombre es inválido.");
        }
    }

    /**
     * Valida el formato del correo electrónico.
     * No puede ser nulo y debe contener el carácter '@'.
     */
    private void validarCorreo(String correo) {
        if (Objects.requireNonNullElse(correo, "").isEmpty() || !correo.contains("@")) {
            throw new IllegalArgumentException("El formato del correo es inválido.");
        }
    }

    // --- Lógica de negocio ---

    public void agregarPuntos(int puntos) {
        this.puntos += puntos;
        this.nivel = Nivel.determinarNivelPorPuntos(this.puntos);
    }
    
    public static void resetCounter() {
        nextId = 1;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public int getPuntos() { return puntos; }
    public Nivel getNivel() { return nivel; }
    
    // --- Setters ---
    public void setId(int id) { this.id = id; }
    
    public void setNombre(String nombre) {
        // Se añade la validación para asegurar que el nuevo nombre también sea válido.
        validarNombre(nombre);
        this.nombre = nombre;
    }
}