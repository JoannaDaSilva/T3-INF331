package com.tienda.fidelidad.services;

import java.util.List;
import java.util.Optional;

import com.tienda.fidelidad.data.ClienteData;
import com.tienda.fidelidad.model.Cliente;

/**
 * Servicio para gestionar la lógica de negocio de los clientes.
 * Provee operaciones como agregar, listar, actualizar y eliminar clientes.
 */
public final class ClienteService {
    private final ClienteData clienteData;

    public ClienteService(ClienteData clienteData) {
        this.clienteData = clienteData;
    }

    /**
     * Crea y guarda un nuevo cliente si el correo no está registrado.
     *
     * @param nombre El nombre del nuevo cliente.
     * @param correo El correo electrónico del nuevo cliente (debe ser único).
     * @return El objeto Cliente recién creado y guardado.
     * @throws IllegalArgumentException si el correo ya está en uso.
     */
    public Cliente agregarCliente(String nombre, String correo) {
        Optional<Cliente> clienteExistente = clienteData.findByCorreo(correo);
        if (clienteExistente.isPresent()) {
            throw new IllegalArgumentException("El correo '" + correo + "' ya está registrado.");
        }
        
        Cliente nuevoCliente = new Cliente(nombre, correo);
        return clienteData.save(nuevoCliente);
    }

    /**
     * Busca y devuelve un cliente por su ID.
     *
     * @param id El ID del cliente a buscar.
     * @return Un Optional que contiene al cliente si se encuentra, o un Optional vacío.
     */
    public Optional<Cliente> buscarClientePorId(int id) {
        return clienteData.findById(id);
    }

    /**
     * Devuelve una lista con todos los clientes registrados.
     *
     * @return Una lista de objetos Cliente. La lista estará vacía si no hay clientes.
     */
    public List<Cliente> listarClientes() {
        return clienteData.findAll();
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param cliente El objeto Cliente con los datos actualizados. El ID del cliente debe existir.
     * @return El objeto Cliente actualizado.
     * @throws IllegalArgumentException si el cliente con el ID especificado no existe.
     */
    public Cliente actualizarCliente(Cliente cliente) {
        verificarClienteExistente(cliente.getId());
        return clienteData.save(cliente);
    }

    /**
     * Elimina un cliente del sistema utilizando su ID.
     *
     * @param id El ID del cliente a eliminar.
     * @throws IllegalArgumentException si el cliente con el ID especificado no existe.
     */
    public void eliminarCliente(int id) {
        verificarClienteExistente(id);
        clienteData.deleteById(id);
    }

    /**
     * Método de ayuda para verificar si un cliente existe y lanzar una excepción si no.
     *
     * @param id El ID del cliente a verificar.
     * @throws IllegalArgumentException si el cliente no se encuentra.
     */
    private void verificarClienteExistente(int id) {
        if (!clienteData.existsById(id)) {
            throw new IllegalArgumentException("No se puede continuar. El cliente con ID " + id + " no existe.");
        }
    }
}