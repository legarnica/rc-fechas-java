package cl.lherrera.rc.fechas.principal;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    /**
     * Se utiliza el método obtenerFechaLocalLiteral, para generar la fecha,
     * luego esta se constrasta con el validador que ya tenemos para
     * determinar si el literal generado posee una estructura válida.
     *
     * No podemos simplemente constrastarlo con una fecha generada,
     * ya que esta fecha es denámica.
     */
    @Test
    void obtenerFechaLocalLiteralOK() {
        String fechaGeneradaComoCadena = FechasUtilDos.obtenerFechaLocalLiteral();
        boolean esValida = true;
        try {
            FechasUtilDos.validaEstructuraFechaEs(fechaGeneradaComoCadena);
        } catch (Exception e) {
            esValida = false;
        }
        assertTrue(esValida);
    }

    /**
     * Prueba la transformación de una fecha a palabras.
     *
     * @throws ParseException por el parse que se hace
     *         en el mismo test, no implica funcionamiento
     *         del método que se está probando.
     */
    @Test
    void parseaAFechaPalabras() throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        Date mifecha = formato.parse("13-12-2020");

        String esperado = "domingo 13 de diciembre de 2020";
        String generado = FechasUtilDos.parseaAFechaPalabras(mifecha);
        assertEquals(esperado, generado);
    }

    /**
     * prueba la diferencia en DIAS, entre DOS FECHAS.
     *
     * @throws ParseException por el parse que se hace
     *         en el mismo test, no implica funcionamiento
     *         del método que se está probando.
     */
    @Test
    void diferenciaDiasFechaLocal() throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date primera = formato.parse("01-01-2021 00:00:00");
        Date segunda = formato.parse("01-02-2021 00:00:00");

        int diasDeDiferencia = FechasUtilDos.diferenciaDiasFechaLocal(primera, segunda);
        int diferenciaEsperada = 31;

        assertEquals(diferenciaEsperada, diasDeDiferencia);
    }

    /**
     * prueba la diferencia en SEGUNDOS, entre DOS FECHAS.
     *
     * @throws ParseException por el parse que se hace
     *         en el mismo test, no implica funcionamiento
     *         del método que se está probando.
     */
    @Test
    void diferenciaFechasEnSegundosOK() throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String fechaLitUno = "01-01-2020 23:59:59";
        String fechaLitDos = "01-01-2020 23:58:59";

        int diferencia = FechasUtilDos.diferenciaFechasEnSegundos(
                formato.parse(fechaLitUno), formato.parse(fechaLitDos));
        int diferenciaEsperada = 60;
        assertEquals(diferenciaEsperada, diferencia);
    }
}