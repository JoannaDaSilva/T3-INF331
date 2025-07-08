package com.tienda.fidelidad.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.tienda.fidelidad.data.ClienteData;
import com.tienda.fidelidad.data.CompraData;
import com.tienda.fidelidad.model.Compra;

/**
 * Servicio para gestionar las operaciones relacionadas con las compras.
 */
public final class CompraService {
    private final ClienteData clienteData;
    private final CompraData compraData;
    private final AtomicInteger nextCompraId = new AtomicInteger(1);

    public CompraService(ClienteData clienteData, CompraData compraData) {
        this.clienteData = clienteData;
        this.compraData = compraData;
    }

    /**
     * Registra una nueva compra para un cliente existente.
     *
     * @param clienteId El ID del cliente que realiza la compra.
     * @param monto El monto total de la compra.
     * @return El objeto Compra recién creado y guardado.
     * @throws IllegalArgumentException si el cliente con el ID especificado no existe.
     */
    public Compra registrarCompra(int clienteId, double monto) {
        if (!clienteData.existsById(clienteId)) {
            throw new IllegalArgumentException("No se puede registrar la compra. El cliente con ID " + clienteId + " no existe.");
        }
        
        int nuevoId = nextCompraId.getAndIncrement();
        Compra nuevaCompra = new Compra(nuevoId, clienteId, monto, LocalDateTime.now());
        
        return compraData.save(nuevaCompra);
    }

    /**
     * Obtiene el historial de compras de un cliente específico.
     *
     * @param clienteId El ID del cliente cuyo historial se desea obtener.
     * @return Una lista de objetos Compra. Estará vacía si el cliente no tiene compras.
     */
    public List<Compra> listarComprasPorCliente(int clienteId) {
        return compraData.findAllByClienteId(clienteId);
    }
}