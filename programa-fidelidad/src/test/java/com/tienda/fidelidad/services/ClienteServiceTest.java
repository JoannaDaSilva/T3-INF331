package com.tienda.fidelidad.services;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tienda.fidelidad.data.ClienteData;
import com.tienda.fidelidad.model.Cliente;

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
    @DisplayName("buscarClientePorId debe devolver el cliente si existe")
    void testBuscarClientePorIdExistente() {
        Cliente clienteGuardado = clienteService.agregarCliente("Benny the Cab", "benny.cab@toontown.com");
        int idCliente = clienteGuardado.getId();

        Optional<Cliente> clienteEncontrado = clienteService.buscarClientePorId(idCliente);

        assertTrue(clienteEncontrado.isPresent(), "El cliente debería haber sido encontrado.");
        assertEquals(idCliente, clienteEncontrado.get().getId());
        assertEquals("Benny the Cab", clienteEncontrado.get().getNombre());
    }

    @Test
    @DisplayName("buscarClientePorId debe devolver un Optional vacío si el cliente no existe")
    void testBuscarClientePorIdNoExistente() {
        int idInexistente = 999;

        Optional<Cliente> clienteEncontrado = clienteService.buscarClientePorId(idInexistente);

        assertFalse(clienteEncontrado.isPresent(), "No debería encontrarse un cliente con un ID inexistente.");
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

        assertEquals("No se puede continuar. El cliente con ID 999 no existe.", exception.getMessage());
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
