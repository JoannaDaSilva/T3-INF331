package com.tienda.fidelidad.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.tienda.fidelidad.data.ClienteData;
import com.tienda.fidelidad.data.CompraData;
import com.tienda.fidelidad.model.Compra;

public class CompraService {
    private final ClienteData clienteData;
    private final CompraData compraData;
    private final AtomicInteger nextCompraId = new AtomicInteger(1);

    public CompraService(ClienteData clienteData, CompraData compraData) {
        this.clienteData = clienteData;
        this.compraData = compraData;
    }

    public Compra registrarCompra(int clienteId, double monto) {
        if (clienteData.findById(clienteId).isEmpty()) {
            throw new IllegalArgumentException("No se puede registrar la compra. El cliente con ID " + clienteId + " no existe.");
        }
        int nuevoId = nextCompraId.getAndIncrement();
        Compra nuevaCompra = new Compra(nuevoId, clienteId, monto, LocalDateTime.now());
        return compraData.save(nuevaCompra);
    }

    public List<Compra> listarComprasPorCliente(int clienteId) {
        return compraData.findAllByClienteId(clienteId);
    }
}
