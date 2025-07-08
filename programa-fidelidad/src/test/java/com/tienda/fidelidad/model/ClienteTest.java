package com.tienda.fidelidad.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @BeforeEach
    void setUp() {
        Cliente.resetCounter();
    }

    @Test
    @DisplayName("Crear cliente nuevo debe inicializarse con 0 puntos y nivel Bronce")
    void testCrearClienteNuevo() {
        Cliente cliente = new Cliente("Jessica Rabbit", "jessica.rabbit@example.com");

        assertNotNull(cliente);
        assertEquals(1, cliente.getId()); // Ahora siempre será 1
        assertEquals("Jessica Rabbit", cliente.getNombre());
        assertEquals("jessica.rabbit@example.com", cliente.getCorreo());
        assertEquals(0, cliente.getPuntos());
        assertEquals(Nivel.BRONCE, cliente.getNivel());
    }

    @Test
    @DisplayName("Crear un cliente con un email sin '@' debe lanzar una excepción")
    void testCrearClienteCorreoInvalido() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("Jessica Rabbit", "jessica.rabbit.invalido.com");
        });
        assertTrue(exception.getMessage().contains("El formato del correo es inválido."));
    }

    @Test
    @DisplayName("Crear un cliente con un email vacío debe lanzar una excepción")
    void testCrearClienteCorreoVacio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("Jessica Rabbit", "");
        });
        assertTrue(exception.getMessage().contains("El formato del correo es inválido."));
    }

    @Test
    @DisplayName("Crear un cliente con un nombre vacío debe lanzar una excepción")
    void testCrearClienteNombreVacio() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("", "jessica.rabbit@example.com");
        });
        assertTrue(exception.getMessage().contains("El formato del nombre es inválido."));
    }

    @Test
    @DisplayName("Crear un cliente con nombre con caracteres especiales debe lanzar una excepción")
    void testCrearClienteNombreConCaracteresEspeciales() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Cliente("Jessica@Rabbit", "jessica.rabbit@example.com");
        });
        assertTrue(exception.getMessage().contains("El formato del nombre es inválido."));
    }

    @Test
    @DisplayName("Agregar puntos debe actualizar el total de puntos y el nivel del cliente")
    void testAgregarPuntos() {
        Cliente cliente = new Cliente("Jessica Rabbit", "jessica.rabbit@example.com");
        
        cliente.agregarPuntos(100);
        assertEquals(100, cliente.getPuntos());
        assertEquals(Nivel.BRONCE, cliente.getNivel());

        cliente.agregarPuntos(150);
        assertEquals(250, cliente.getPuntos());
        assertEquals(Nivel.BRONCE, cliente.getNivel());

        cliente.agregarPuntos(250);
        assertEquals(500, cliente.getPuntos());
        assertEquals(Nivel.PLATA, cliente.getNivel());

        cliente.agregarPuntos(1000);
        assertEquals(1500, cliente.getPuntos());
        assertEquals(Nivel.ORO, cliente.getNivel());
        
        cliente.agregarPuntos(1500);
        assertEquals(3000, cliente.getPuntos());
        assertEquals(Nivel.PLATINO, cliente.getNivel());
    }
}
