package com.tienda.fidelidad.services;

import java.util.List;

import com.tienda.fidelidad.data.ClienteData;
import com.tienda.fidelidad.model.Cliente;

public class ClienteService {
    private final ClienteData clienteData;

    public ClienteService(ClienteData clienteData) {
        this.clienteData = clienteData;
    }

    public Cliente agregarCliente(String nombre, String correo) {
        if (clienteData.findByCorreo(correo).isPresent()) {
            throw new IllegalArgumentException("El correo '" + correo + "' ya está registrado.");
        }
        Cliente nuevoCliente = new Cliente(nombre, correo);
        return clienteData.save(nuevoCliente);
    }

    public List<Cliente> listarClientes() {
        return clienteData.findAll();
    }

    public Cliente actualizarCliente(Cliente cliente) {
        if (!clienteData.existsById(cliente.getId())) {
            throw new IllegalArgumentException("No se puede actualizar. El cliente con ID " + cliente.getId() + " no existe.");
        }
        return clienteData.save(cliente);
    }

    public void eliminarCliente(int id) {
        clienteData.deleteById(id);
    }
}
