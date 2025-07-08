package com.tienda.fidelidad.data;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tienda.fidelidad.model.Cliente;

class ClienteDataTest {

    private ClienteData clienteData;

    @BeforeEach
    void setUp() {
        clienteData = new ClienteData();
    }

    @Test
    @DisplayName("Guardar un cliente nuevo debe asignarle un ID y almacenarlo")
    void testGuardarNuevoCliente() {
        Cliente nuevoCliente = new Cliente(1, "Roger Rabbit", "roger.rabbit@toontown.com");

        Cliente clienteGuardado = clienteData.save(nuevoCliente);

        assertNotNull(clienteGuardado.getId());
        assertEquals(1, clienteGuardado.getId());
        Optional<Cliente> clienteEncontrado = clienteData.findById(1);
        assertTrue(clienteEncontrado.isPresent());
        assertEquals("Roger Rabbit", clienteEncontrado.get().getNombre());
    }

    @Test
    @DisplayName("Guardar un cliente existente debe actualizar sus datos")
    void testGuardarClienteExistente() {
        Cliente clienteInicial = clienteData.save(new Cliente(1, "Jessica Rabbit", "jessica.rabbit@inkandpaint.com"));
        clienteInicial.setNombre("Mrs. Jessica Rabbit");

        clienteData.save(clienteInicial);

        Optional<Cliente> clienteActualizado = clienteData.findById(1);
        assertTrue(clienteActualizado.isPresent());
        assertEquals("Mrs. Jessica Rabbit", clienteActualizado.get().getNombre());
        assertEquals(1, clienteData.findAll().size());
    }

    @Test
    @DisplayName("findById debe devolver un cliente existente")
    void testFindById() {
        Cliente cliente = clienteData.save(new Cliente(1, "Eddie Valiant", "eddie.valiant@valiantpi.com"));

        Optional<Cliente> clienteEncontrado = clienteData.findById(cliente.getId());

        assertTrue(clienteEncontrado.isPresent());
        assertEquals("Eddie Valiant", clienteEncontrado.get().getNombre());
    }

    @Test
    @DisplayName("findById debe devolver un Optional vacío si el cliente no existe")
    void testFindByIdNonExistent() {
        Optional<Cliente> clienteEncontrado = clienteData.findById(999);
        assertFalse(clienteEncontrado.isPresent());
    }

    @Test
    @DisplayName("findByCorreo debe devolver el cliente correcto")
    void testFindByCorreo() {
        clienteData.save(new Cliente(1, "Judge Doom", "judge.doom@cloverleaf.com"));

        Optional<Cliente> clienteEncontrado = clienteData.findByCorreo("judge.doom@cloverleaf.com");
        
        assertTrue(clienteEncontrado.isPresent());
        assertEquals("Judge Doom", clienteEncontrado.get().getNombre());
    }

    @Test
    @DisplayName("findAll debe devolver todos los clientes")
    void testFindAllMultipleClients() {
        List<Cliente> clientes = clienteData.findAll();
        assertEquals(0, clientes.size());

        clienteData.save(new Cliente(1, "Baby Herman", "baby.herman@marooncartoons.com"));
        clienteData.save(new Cliente(2, "Benny the Cab", "benny.cab@toontown.com"));

        clientes = clienteData.findAll();
        assertEquals(2, clientes.size());
    }

    @Test
    @DisplayName("deleteById debe eliminar un cliente existente")
    void testDeleteById() {
        Cliente cliente = clienteData.save(new Cliente(1, "Marvin Acme", "marvin.acme@acme.com"));
        int id = cliente.getId();
        
        clienteData.deleteById(id);

        assertFalse(clienteData.findById(id).isPresent());
    }

}
