package com.tienda.fidelidad.services;

import com.tienda.fidelidad.data.ClienteData;
import com.tienda.fidelidad.modelo.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    private ClienteService clienteService;
    private ClienteData clienteData;

    @BeforeEach
    void setUp() {
        clienteData = new ClienteData();
        clienteService = new ClienteService(clienteData);
    }

    @Test
    @DisplayName("Agregar un cliente con datos válidos debe guardarlo")
    void testAgregarClienteValido() {
        String nombre = "Eddie Valiant";
        String correo = "eddie.valiant@valiantpi.com";

        Cliente clienteGuardado = clienteService.agregarCliente(nombre, correo);

        assertNotNull(clienteGuardado);
        assertNotNull(clienteGuardado.getId());
        assertEquals(nombre, clienteGuardado.getNombre());

        Optional<Cliente> clienteEnData = clienteData.findById(clienteGuardado.getId());
        assertTrue(clienteEnData.isPresent());
    }

    @Test
    @DisplayName("Agregar un cliente con un correo que ya existe debe lanzar una excepción")
    void testAgregarClienteCorreoExistente() {
        clienteService.agregarCliente("Roger Rabbit", "roger.rabbit@toontown.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.agregarCliente("Roger Rabbit Jr", "roger.rabbit@toontown.com");
        });

        assertEquals("El correo 'roger.rabbit@toontown.com' ya está registrado.", exception.getMessage());
    }

    @Test
    @DisplayName("Listar clientes debe devolver todos los clientes guardados")
    void testListarClientes() {
        clienteService.agregarCliente("Jessica Rabbit", "jessica.rabbit@inkandpaint.com");
        clienteService.agregarCliente("Baby Herman", "baby.herman@marooncartoons.com");

        List<Cliente> clientes = clienteService.listarClientes();

        assertNotNull(clientes);
        assertEquals(2, clientes.size());
    }

    @Test
    @DisplayName("Actualizar un cliente existente debe modificar sus datos")
    void testActualizarClienteExistente() {
        Cliente clienteInicial = clienteService.agregarCliente("R. K. Maroon", "rk.maroon@marooncartoons.com");
        int idCliente = clienteInicial.getId();
        
        clienteInicial.setNombre("R. K. Maroon (Retirado)");
        Cliente clienteActualizado = clienteService.actualizarCliente(clienteInicial);

        assertNotNull(clienteActualizado);
        assertEquals(idCliente, clienteActualizado.getId());
        assertEquals("R. K. Maroon (Retirado)", clienteActualizado.getNombre());
    }
    
    @Test
    @DisplayName("Actualizar un cliente que no existe debe lanzar una excepción")
    void testActualizarClienteNoExistente() {
        Cliente clienteFalso = new Cliente(999, "Fantasma", "fantasma@example.com");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteService.actualizarCliente(clienteFalso);
        });

        assertEquals("No se puede actualizar. El cliente con ID 999 no existe.", exception.getMessage());
    }

    @Test
    @DisplayName("Eliminar un cliente existente debe removerlo")
    void testEliminarClienteExistente() {
        Cliente cliente = clienteService.agregarCliente("Judge Doom", "judge.doom@cloverleaf.com");
        int idCliente = cliente.getId();

        clienteService.eliminarCliente(idCliente);

        Optional<Cliente> clienteEnRepo = clienteData.findById(idCliente);
        assertFalse(clienteEnRepo.isPresent());
    }
}
