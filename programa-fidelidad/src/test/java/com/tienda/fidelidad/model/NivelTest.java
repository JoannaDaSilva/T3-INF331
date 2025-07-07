package com.tienda.fidelidad.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NivelTest {

    @Test
    @DisplayName("El multiplicador de puntos debe ser el correcto para cada nivel")
    void testMultiplicadorPorNivel() {
        assertEquals(2.0, Nivel.PLATINO.getMultiplicador(), 0.001);
        assertEquals(1.5, Nivel.ORO.getMultiplicador(), 0.001);
        assertEquals(1.2, Nivel.PLATA.getMultiplicador(), 0.001);
        assertEquals(1.0, Nivel.BRONCE.getMultiplicador(), 0.001);
    }

    @Test
    @DisplayName("Debe determinar el nivel correcto basado en la cantidad de puntos")
    void testDeterminarNivelSegunPuntos() {
        
        // Límites para BRONCE
        assertEquals(Nivel.BRONCE, Nivel.determinarNivelPorPuntos(0));
        assertEquals(Nivel.BRONCE, Nivel.determinarNivelPorPuntos(499));

        // Límites para PLATA
        assertEquals(Nivel.PLATA, Nivel.determinarNivelPorPuntos(500));
        assertEquals(Nivel.PLATA, Nivel.determinarNivelPorPuntos(1499));

        // Límites para ORO
        assertEquals(Nivel.ORO, Nivel.determinarNivelPorPuntos(1500));
        assertEquals(Nivel.ORO, Nivel.determinarNivelPorPuntos(2999));

        // Límites para PLATINO
        assertEquals(Nivel.PLATINO, Nivel.determinarNivelPorPuntos(3000));
        assertEquals(Nivel.PLATINO, Nivel.determinarNivelPorPuntos(10000));
    }

    @Test
    @DisplayName("Determinar nivel con puntos negativos debe lanzar una excepción")
    void testDeterminarNivelConPuntosNegativos() {
        assertThrows(IllegalArgumentException.class, () -> {
            Nivel.determinarNivelPorPuntos(-1);
        });
    }
}
