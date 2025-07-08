package com.tienda.fidelidad.services;

import java.time.LocalDate;

import com.tienda.fidelidad.data.CompraData;
import com.tienda.fidelidad.model.Cliente;

public class PuntosService {

    private final CompraData compraData;

    public PuntosService(CompraData compraData) {
        this.compraData = compraData;
    }

    /**
     * Calcula los puntos que una compra generaría para un cliente,
     * aplicando todas las reglas de negocio (multiplicador y bonos).
     * @param cliente El cliente que realiza la compra.
     * @param montoCompra El monto de la nueva compra.
     * @return El total de puntos generados por la compra.
     */
    public int calcularPuntos(Cliente cliente, double montoCompra) {
        int puntosBase = (int) (montoCompra / 100);

        double multiplicador = cliente.getNivel().getMultiplicador();
        int puntosConMultiplicador = (int) (puntosBase * multiplicador);

        int puntosDeBono = 0;
        LocalDate hoy = LocalDate.now();
        long comprasDeHoy = compraData.findAllByClienteId(cliente.getId()).stream()
                .filter(compra -> compra.getFecha().toLocalDate().isEqual(hoy))
                .count();

        if (comprasDeHoy == 2) {
            puntosDeBono = 10;
        }

        return puntosConMultiplicador + puntosDeBono;
    }
}
