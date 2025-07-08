package com.tienda.fidelidad.services;

import com.tienda.fidelidad.data.ClienteData;
import com.tienda.fidelidad.data.CompraData;
import com.tienda.fidelidad.model.Cliente;
import com.tienda.fidelidad.model.Compra;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CompraServiceTest {

    private CompraService compraService;
    private ClienteService clienteService;
    private CompraData compraData;
    private ClienteData clienteData;

    @BeforeEach
    void setUp() {
        clienteData = new ClienteData();
        compraData = new CompraData();
        clienteService = new ClienteService(clienteData);
        compraService = new CompraService(clienteData, compraData);
    }

    @Test
    @DisplayName("Registrar una compra para un cliente existente debe ser exitoso")
    void testRegistrarCompraValida() {
        Cliente eddie = clienteService.agregarCliente("Eddie Valiant", "eddie@valiantpi.com");
        double monto = 25000.0;

        Compra compraRegistrada = compraService.registrarCompra(eddie.getId(), monto);

        assertNotNull(compraRegistrada);
        assertNotNull(compraRegistrada.getId());
        assertEquals(eddie.getId(), compraRegistrada.getIdCliente());
        assertEquals(monto, compraRegistrada.getMonto());
    }

    @Test
    @DisplayName("Registrar una compra para un cliente que no existe debe lanzar una excepción")
    void testRegistrarCompraClienteNoExistente() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            compraService.registrarCompra(999, 10000.0);
        });

        assertEquals("No se puede registrar la compra. El cliente con ID 999 no existe.", exception.getMessage());
    }

    @Test
    @DisplayName("Listar compras por cliente debe devolver su historial")
    void testListarComprasPorCliente() {
        Cliente roger = clienteService.agregarCliente("Roger Rabbit", "roger@toontown.com");
        compraService.registrarCompra(roger.getId(), 5000.0);
        compraService.registrarCompra(roger.getId(), 15000.0);

        List<Compra> historial = compraService.listarComprasPorCliente(roger.getId());

        assertNotNull(historial);
        assertEquals(2, historial.size());
    }

    @Test
    @DisplayName("Listar compras de un cliente sin historial debe devolver una lista vacía")
    void testListarComprasClienteSinCompras() {
        Cliente jessica = clienteService.agregarCliente("Jessica Rabbit", "jessica@inkandpaint.com");

        List<Compra> historial = compraService.listarComprasPorCliente(jessica.getId());

        assertNotNull(historial);
        assertTrue(historial.isEmpty());
    }
}
