package com.tienda.fidelidad.model;

import java.time.LocalDateTime;

public class Compra {
    // Se convierten todos los campos a 'final' para hacer el objeto inmutable.
    private final int idCompra;
    private final int idCliente;
    private final double monto;
    private final LocalDateTime fecha;

    public Compra(int idCompra, int idCliente, double monto, LocalDateTime fecha) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto de la compra no puede ser negativo.");
        }
        
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de la compra no puede ser nula.");
        }
        
        if (fecha.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la compra no puede ser en el futuro.");
        }
        
        this.idCompra = idCompra;
        this.idCliente = idCliente;
        this.monto = monto;
        this.fecha = fecha;
    }

    // Getters
    public int getIdCompra() { return idCompra; }
    public int getIdCliente() { return idCliente; }
    public double getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
}