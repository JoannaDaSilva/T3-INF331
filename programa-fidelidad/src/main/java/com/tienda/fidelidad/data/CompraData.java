package com.tienda.fidelidad.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tienda.fidelidad.model.Compra;

public class CompraData {
    private final Map<Integer, Compra> compras = new HashMap<>();

    public Compra save(Compra compra) {
        compras.put(compra.getId(), compra);
        return compra;
    }

    public Optional<Compra> findById(int id) {
        return Optional.ofNullable(compras.get(id));
    }

    public List<Compra> findAllByClienteId(int clienteId) {
        return compras.values().stream()
                .filter(c -> c.getIdCliente() == clienteId)
                .collect(Collectors.toList());
    }
}
