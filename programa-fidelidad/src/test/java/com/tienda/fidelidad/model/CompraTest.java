package com.tienda.fidelidad.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CompraTest {
    @Test
    @DisplayName("Crear una compra debe asignar los valores correctamente")
    void testCrearCompra() {
        LocalDateTime fechaCompra = LocalDateTime.now();

        Compra compra = new Compra(1, 101, 15000.0, fechaCompra);

        assertNotNull(compra);
        assertEquals(1, compra.getIdCompra());
        assertEquals(101, compra.getIdCliente());
        assertEquals(15000.0, compra.getMonto());
        assertEquals(fechaCompra, compra.getFecha());
    }

    @Test
    @DisplayName("Crear una compra con monto negativo debe lanzar una excepción")
    void testCrearCompraConMontoNegativoLanzaExcepcion() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Compra(2, 102, -100.0, LocalDateTime.now());
        });

        String expectedMessage = "El monto de la compra no puede ser negativo.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Agregué este test suponiendo casos que los clientes pueden canjear sus puntos y tendrian una compra de monto cero
    @Test
    @DisplayName("Crear una compra con monto cero debe ser válido")
    void testCrearCompraConMontoCeroEsValido() {
        LocalDateTime fechaCompra = LocalDateTime.now();
        
        assertDoesNotThrow(() -> {
            new Compra(3, 103, 0.0, fechaCompra);
        });
    }

    @Test
    @DisplayName("Crear una compra con fecha nula debe lanzar una excepción")
    void testCrearCompraConFechaNulaLanzaExcepcion() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Compra(4, 104, 5000.0, null);
        });
        String expectedMessage = "La fecha de la compra no puede ser nula.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Crear una compra con fecha en el futuro debe lanzar una excepción")
    void testCrearCompraConFechaFuturaLanzaExcepcion() {
        LocalDateTime fechaFutura = LocalDateTime.now().plusDays(1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Compra(5, 105, 6000.0, fechaFutura);
        });
        String expectedMessage = "La fecha de la compra no puede ser en el futuro.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}