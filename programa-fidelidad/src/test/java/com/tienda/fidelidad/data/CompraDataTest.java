package com.tienda.fidelidad.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.tienda.fidelidad.model.Compra;

class CompraDataTest {

    private CompraData compraData;
    private final int ROGER_RABBIT_ID = 1;
    private final int JESSICA_RABBIT_ID = 2;

    @BeforeEach
    void setUp() {
        compraData = new CompraData();
    }

    @Test
    @DisplayName("Guardar una nueva compra debe asignarle un ID y almacenarla")
    void testGuardarNuevaCompra() {
        Compra nuevaCompra = new Compra(1, ROGER_RABBIT_ID, 5000.0, LocalDateTime.now());

        Compra compraGuardada = compraData.save(nuevaCompra);

        assertEquals(1, compraGuardada.getIdCompra());
        assertEquals(ROGER_RABBIT_ID, compraGuardada.getIdCliente());
        assertEquals(5000.0, compraGuardada.getMonto());
        Optional<Compra> compraEncontrada = compraData.findById(1);
        assertTrue(compraEncontrada.isPresent());
    }

    @Test
    @DisplayName("findById debe devolver una compra existente")
    void testFindById() {
        Compra compra = compraData.save(new Compra(1, JESSICA_RABBIT_ID, 1200.0, LocalDateTime.now()));

        Optional<Compra> compraEncontrada = compraData.findById(compra.getIdCompra());

        assertTrue(compraEncontrada.isPresent());
        assertEquals(1200.0, compraEncontrada.get().getMonto());
    }

    @Test
    @DisplayName("findById debe devolver un Optional vacío si la compra no existe")
    void testFindByIdNonExistent() {
        Optional<Compra> compraEncontrada = compraData.findById(999);

        assertFalse(compraEncontrada.isPresent());
    }

    @Test
    @DisplayName("findAllByClienteId debe devolver todas las compras de un cliente")
    void testFindAllByClienteId_ConCompras_DebeDevolverLista() {
        LocalDateTime ahora = LocalDateTime.now();
        compraData.save(new Compra(1, ROGER_RABBIT_ID, 25.0, ahora.minusDays(1)));
        compraData.save(new Compra(2, JESSICA_RABBIT_ID, 300.0, ahora.minusHours(5)));
        compraData.save(new Compra(3, ROGER_RABBIT_ID, 75.0, ahora));

        List<Compra> comprasRoger = compraData.findAllByClienteId(ROGER_RABBIT_ID);

        assertEquals(2, comprasRoger.size());
    }

    @Test
    @DisplayName("findAllByClienteId debe devolver una lista vacía si el cliente no tiene compras")
    void testFindAllByClienteIdNoPurchase() {
        compraData.save(new Compra(4, JESSICA_RABBIT_ID, 100.0, LocalDateTime.now()));
        
        List<Compra> comprasRoger = compraData.findAllByClienteId(ROGER_RABBIT_ID);

        assertNotNull(comprasRoger);
        assertTrue(comprasRoger.isEmpty());
    }
}
