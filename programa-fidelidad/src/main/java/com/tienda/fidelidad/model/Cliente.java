package com.tienda.fidelidad.model;

public class Cliente {
    private int id;
    private String nombre;
    private String correo;
    private int puntos;
    private Nivel nivel;
    private static int nextId = 1;

    public Cliente(String nombre, String correo) {
        if (nombre == null || nombre.trim().isEmpty() || nombre.matches(".*[@].*")) {
            throw new IllegalArgumentException("El formato del nombre es inválido.");
        }
        if (correo == null || !correo.contains("@")) {
            throw new IllegalArgumentException("El formato del correo es inválido.");
        }
        this.id = nextId++;
        this.nombre = nombre;
        this.correo = correo;
        this.puntos = 0;
        this.nivel = Nivel.BRONCE;
    }

    public Cliente(int id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.puntos = 0;
        this.nivel = Nivel.BRONCE;
    }

    public void agregarPuntos(int puntos) {
        this.puntos += puntos;
        this.nivel = Nivel.determinarNivelPorPuntos(this.puntos);
    }
    
    public static void resetCounter() {
        nextId = 1;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public int getPuntos() { return puntos; }
    public Nivel getNivel() { return nivel; }
    
    // Setters
    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
