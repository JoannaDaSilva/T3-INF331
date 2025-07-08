package com.tienda.fidelidad.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tienda.fidelidad.model.Compra;

public final class CompraData {
    private final Map<Integer, Compra> compras = new HashMap<>();

    /**
     * Guarda una compra en el mapa de datos.
     * @param compra La compra a guardar.
     * @return La compra guardada.
     */
    public Compra save(Compra compra) {
        compras.put(compra.getIdCompra(), compra);
        return compra;
    }

    /**
     * Busca una compra por su ID.
     * @param id El ID de la compra a buscar.
     * @return Un Optional con la compra si se encuentra, o un Optional vacío.
     */
    public Optional<Compra> findById(int id) {
        return Optional.ofNullable(compras.get(id));
    }

    /**
     * Devuelve todas las compras asociadas a un ID de cliente específico.
     * @param clienteId El ID del cliente.
     * @return Una lista de compras del cliente. Puede estar vacía.
     */
    public List<Compra> findAllByClienteId(int clienteId) {
        return compras.values().stream()
                .filter(c -> c.getIdCliente() == clienteId)
                .collect(Collectors.toList());
    }
}