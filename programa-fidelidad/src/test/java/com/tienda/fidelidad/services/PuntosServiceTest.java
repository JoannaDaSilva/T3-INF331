package com.tienda.fidelidad.services;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tienda.fidelidad.data.CompraData;
import com.tienda.fidelidad.model.Cliente;
import com.tienda.fidelidad.model.Compra;
import com.tienda.fidelidad.model.Nivel;

class PuntosServiceTest {

    private PuntosService puntosService;
    private CompraData compraData;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        compraData = new CompraData();
        puntosService = new PuntosService(compraData);
        Cliente.resetCounter();
        cliente = new Cliente("Roger Rabbit", "roger@toontown.com");
    }

    @Test
    @DisplayName("Cálculo de puntos base debe ser 1 por cada $100, redondeado hacia abajo")
    void testCalcularPuntosBase() {
        assertEquals(0, puntosService.calcularPuntos(cliente, 99.9));
        assertEquals(1, puntosService.calcularPuntos(cliente, 100.0));
        assertEquals(1, puntosService.calcularPuntos(cliente, 199.0));
        assertEquals(10, puntosService.calcularPuntos(cliente, 1000.0));
    }

    @Test
    @DisplayName("Cálculo de puntos debe aplicar multiplicador de nivel")
    void testCalcularPuntosConMultiplicador() {
        // Nivel PLATA (1.2x)
        cliente.agregarPuntos(500);
        assertEquals(Nivel.PLATA, cliente.getNivel());
        // 100 puntos base * 1.2 = 120 puntos
        assertEquals(120, puntosService.calcularPuntos(cliente, 10000.0));

        // Nivel ORO (1.5x)
        cliente.agregarPuntos(1000); // Total 1500
        assertEquals(Nivel.ORO, cliente.getNivel());
        // 100 puntos base * 1.5 = 150 puntos
        assertEquals(150, puntosService.calcularPuntos(cliente, 10000.0));

        // Nivel PLATINO (2.0x)
        cliente.agregarPuntos(1500); // Total 3000
        assertEquals(Nivel.PLATINO, cliente.getNivel());
        // 100 puntos base * 2.0 = 200 puntos
        assertEquals(200, puntosService.calcularPuntos(cliente, 10000.0));
    }

    @Test
    @DisplayName("Debe agregar +10 puntos de bono por la tercera compra en el mismo día")
    void testBonoPorTerceraCompraMismoDia() {
        LocalDateTime hoy = LocalDateTime.now();
        // Simulamos 2 compras previas hoy
        compraData.save(new Compra(1, cliente.getId(), 100.0, hoy.minusHours(2)));
        compraData.save(new Compra(2, cliente.getId(), 100.0, hoy.minusHours(1)));

        // La tercera compra ($1000 = 10 puntos) debe recibir el bono
        // Total = 10 puntos base + 10 de bono = 20 puntos
        assertEquals(20, puntosService.calcularPuntos(cliente, 1000.0));
    }

    @Test
    @DisplayName("La cuarta compra en el mismo día no debe recibir bono")
    void testCuartaCompraNoRecibeBono() {
        LocalDateTime hoy = LocalDateTime.now();
        // Simulamos 3 compras previas hoy
        compraData.save(new Compra(1, cliente.getId(), 100.0, hoy.minusHours(3)));
        compraData.save(new Compra(2, cliente.getId(), 100.0, hoy.minusHours(2)));
        compraData.save(new Compra(3, cliente.getId(), 100.0, hoy.minusHours(1)));

        // La cuarta compra ($1000 = 10 puntos) no debe recibir bono
        assertEquals(10, puntosService.calcularPuntos(cliente, 1000.0));
    }

    @Test
    @DisplayName("El bono solo se aplica a la tercera compra, no a la sexta")
    void testBonoNoSeAplicaASextaCompra() {
        LocalDateTime hoy = LocalDateTime.now();
        // Simulamos 5 compras previas hoy
        compraData.save(new Compra(1, cliente.getId(), 100.0, hoy.minusHours(5)));
        compraData.save(new Compra(2, cliente.getId(), 100.0, hoy.minusHours(4)));
        compraData.save(new Compra(3, cliente.getId(), 100.0, hoy.minusHours(3))); // Esta recibió bono
        compraData.save(new Compra(4, cliente.getId(), 100.0, hoy.minusHours(2)));
        compraData.save(new Compra(5, cliente.getId(), 100.0, hoy.minusHours(1)));

        // La sexta compra ($1000 = 10 puntos) no debe recibir bono
        assertEquals(10, puntosService.calcularPuntos(cliente, 1000.0));
    }

    @Test
    @DisplayName("El contador de bono debe reiniciarse al día siguiente")
    void testBonoSeReiniciaAlDiaSiguiente() {
        LocalDateTime ayer = LocalDateTime.now().minusDays(1);
        // Simulamos 2 compras previas ayer
        compraData.save(new Compra(1, cliente.getId(), 100.0, ayer.minusHours(2)));
        compraData.save(new Compra(2, cliente.getId(), 100.0, ayer.minusHours(1)));

        // La primera compra de hoy no debe recibir bono
        assertEquals(10, puntosService.calcularPuntos(cliente, 1000.0));
    }
}
