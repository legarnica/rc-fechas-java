package cl.lherrera.rc.fechas.principal;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FechasUtilDosTest {

    /**
     * NO hace más que arrojar una exception en caso de fallar
     * no se puede extraer más info de ese método. Se
     * complementará con una prueba cuando lance
     * la exception.
     */
    @Test
    void validaEstructuraFechaEnOK() {
        String fechaLiteral = "2021-12-13 04:59:31";
        FechasUtilDos.validaEstructuraFechaEn(fechaLiteral);
    }

    /**
     * Prueba el manejo de una fecha con formato inválido.
     */
    @Test
    void validaEstructuraFechaEnNOK() {
        // no existe el minuto 69 en la estructura.
        String fechaLiteral = "2021-12-13 04:69:31";
        boolean esInvalida = false;
        try{
            FechasUtilDos.validaEstructuraFechaEn(fechaLiteral);
        }catch (Exception e) {
            esInvalida = true;
        }
        assertTrue(esInvalida);
    }

    /**
     * Prueba validación ok de fecha en formato local chileno.
     */
    @Test
    void validaEstructuraFechaEsOK() {
        List<String> fechas = Arrays.asList(
                "12-12-2020 23:59:31",
                "13-12-2020 00:00:31",
                "31-12-2020 07:37:31");

        for(String fechaLiteral: fechas) {
            FechasUtilDos.validaEstructuraFechaEs(fechaLiteral);
        }
    }

    /**
     * Prueba el reconocimiento y/o evaluación de la estructura
     * de una fecha, con formato inválido.
     *
     * Nota: Es posible adicionar más fechas; pero deben
     * ser inválidas.
     */
    @Test
    void validaEstructuraFechaEsNok() {
        List<String> fechas = Arrays.asList(
                "12-12-2020 24:59:31",
                "13-12-2020 00:00:31 ",
                "13-12-202000:00:31",
                "13-12-2020 00:00:60",
                "13/12/2020 00:00:00",
                "12-12-2020 24:59:31",
                "12-12-2020 24:59:31",
                "13-13-2020 00:00:31");
        int contadorErrores = 0;
        for(String fechaLiteral: fechas) {
            try{
                FechasUtilDos.validaEstructuraFechaEs(fechaLiteral);
            }catch (Exception e) {
                contadorErrores++;
            }
        }

        assertEquals(fechas.size(), contadorErrores);
    }
}