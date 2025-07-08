package com.tienda.fidelidad.model;

import java.time.LocalDateTime;

public class Compra {
    private int idCompra;
    private int idCliente;
    private double monto;
    private LocalDateTime fecha;

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
    public int getId() { return idCompra; }
    public int getIdCompra() { return idCompra; }
    public int getIdCliente() { return idCliente; }
    public double getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
}
