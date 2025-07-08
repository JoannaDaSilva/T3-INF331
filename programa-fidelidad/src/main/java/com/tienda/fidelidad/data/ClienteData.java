package com.tienda.fidelidad.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tienda.fidelidad.model.Cliente;

public final class ClienteData {
    private final Map<Integer, Cliente> clientes = new HashMap<>();

    /**
     * Guarda o actualiza un cliente. Si el ID del cliente ya existe, se actualiza.
     * Si no, se añade como nuevo.
     * @param cliente El cliente a guardar.
     * @return El cliente guardado.
     */
    public Cliente save(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    /**
     * Busca un cliente por su ID.
     * @param id El ID del cliente a buscar.
     * @return Un Optional con el cliente si se encuentra, o un Optional vacío.
     */
    public Optional<Cliente> findById(int id) {
        return Optional.ofNullable(clientes.get(id));
    }

    /**
     * Busca un cliente por su correo electrónico, ignorando mayúsculas y minúsculas.
     * @param correo El correo del cliente a buscar.
     * @return Un Optional con el primer cliente que coincida, o un Optional vacío.
     */
    public Optional<Cliente> findByCorreo(String correo) {
        return clientes.values().stream()
                .filter(c -> c.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }

    /**
     * Devuelve una lista con todos los clientes.
     * @return Una nueva lista conteniendo todos los clientes.
     */
    public List<Cliente> findAll() {
        // Se devuelve una nueva instancia de ArrayList para proteger la colección interna.
        return new ArrayList<>(clientes.values());
    }

    /**
     * Elimina un cliente por su ID.
     * @param id El ID del cliente a eliminar.
     */
    public void deleteById(int id) {
        clientes.remove(id);
    }
    
    /**
     * Verifica si un cliente existe por su ID.
     * @param id El ID del cliente.
     * @return true si el cliente existe, false en caso contrario.
     */
    public boolean existsById(int id) {
        return clientes.containsKey(id); 
    }
}