package com.tienda.fidelidad.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.tienda.fidelidad.model.Cliente;

public class ClienteData {
    private final Map<Integer, Cliente> clientes = new HashMap<>();

    public Cliente save(Cliente cliente) {
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    public Optional<Cliente> findById(int id) {
        return Optional.ofNullable(clientes.get(id));
    }

    public Optional<Cliente> findByCorreo(String correo) {
        return clientes.values().stream()
                .filter(c -> c.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }

    public List<Cliente> findAll() {
        return new ArrayList<>(clientes.values());
    }

    public void deleteById(int id) {
        clientes.remove(id);
    }
    
    public boolean existsById(int id) {
        return clientes.containsKey(id);
    }
}
