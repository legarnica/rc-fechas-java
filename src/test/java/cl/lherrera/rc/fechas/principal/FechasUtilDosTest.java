package cl.lherrera.rc.fechas.principal;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}