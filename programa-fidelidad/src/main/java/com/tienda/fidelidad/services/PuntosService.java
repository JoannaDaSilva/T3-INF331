package com.tienda.fidelidad.services;

import java.time.LocalDate;

import com.tienda.fidelidad.data.CompraData;
import com.tienda.fidelidad.model.Cliente;

/**
 * Servicio para calcular los puntos de fidelidad generados por las compras.
 */
public final class PuntosService {

    private static final int MONTO_POR_PUNTO = 100;
    private static final int PUNTOS_BONO_TERCERA_COMPRA = 10;
    private static final int NUM_COMPRA_PARA_BONO = 2;

    private final CompraData compraData;

    public PuntosService(CompraData compraData) {
        this.compraData = compraData;
    }

    /**
     * Calcula los puntos que una compra generaría para un cliente,
     * aplicando todas las reglas de negocio (multiplicador de nivel y bonos por frecuencia).
     *
     * @param cliente El cliente que realiza la compra.
     * @param montoCompra El monto de la nueva compra.
     * @return El total de puntos generados por la compra.
     */
    public int calcularPuntos(Cliente cliente, double montoCompra) {
        int puntosBase = calcularPuntosBase(montoCompra);
        int puntosConMultiplicador = aplicarMultiplicadorDeNivel(cliente, puntosBase);
        int puntosDeBono = calcularBonoPorFrecuencia(cliente);

        return puntosConMultiplicador + puntosDeBono;
    }
    
    /**
     * Calcula los puntos base a partir del monto de la compra.
     *
     * @param montoCompra El monto de la compra.
     * @return Los puntos base generados.
     */
    private int calcularPuntosBase(double montoCompra) {
        return (int) (montoCompra / MONTO_POR_PUNTO);
    }
    
    /**
     * Aplica el multiplicador de nivel del cliente a los puntos base.
     *
     * @param cliente El cliente que realiza la compra.
     * @param puntosBase Los puntos base calculados a partir del monto.
     * @return Los puntos con el multiplicador ya aplicado.
     */
    private int aplicarMultiplicadorDeNivel(Cliente cliente, int puntosBase) {
        double multiplicador = cliente.getNivel().getMultiplicador();
        return (int) (puntosBase * multiplicador);
    }

    /**
     * Calcula si corresponde un bono por frecuencia de compra en el mismo día.
     *
     * @param cliente El cliente que realiza la compra.
     * @return Los puntos de bono (si aplican) o 0.
     */
    private int calcularBonoPorFrecuencia(Cliente cliente) {
        long comprasDeHoy = compraData.findAllByClienteId(cliente.getId()).stream()
                .filter(compra -> compra.getFecha().toLocalDate().isEqual(LocalDate.now()))
                .count();

        if (comprasDeHoy == NUM_COMPRA_PARA_BONO) {
            return PUNTOS_BONO_TERCERA_COMPRA;
        }

        return 0;
    }
}