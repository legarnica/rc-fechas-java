package cl.lherrera.rc.fechas.principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

/**
 * Clase utilitaria para el manejo de fechas. Contiene los siguientes servicios:
 *
 * <ul>
 *     <li>void:validaEstructuraFechaEn(String fechaGregoriana), Valida estructura [2021-12-13 04:59:31]</li>
 * </ul>
 */
public class FechasUtilDos {

    /**
     * Logger de la clase.
     */
    private static final Logger log = LoggerFactory.getLogger(FechasUtilDos.class);

    /**
     * Patrón literal expresión regular, que hace match con fechas del tipo: [2021-12-13 15:59:31]
     */
    private final static String YYYYMMDD_HH24MMSS = "^((19|2[0-9])[0-9]{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])(\\s)([0-2])([0-4]):([0-5])([0-9]):([0-5])([0-9]))$";

    /**
     * Valida estructura [2021-12-13 04:59:31]
     *
     * @param fechaAValidar
     */
    public static void validaEstructuraFechaEn(String fechaAValidar) {
        log.info("Inicio - validaEstructuraFechaEn([{}])", fechaAValidar);
        boolean esPatronValido = Pattern.compile(YYYYMMDD_HH24MMSS).matcher(fechaAValidar).matches();

        boolean noEsPatronValido = !esPatronValido;
        if(noEsPatronValido){
            log.error("la fehcha [{}], es incompatible con el patron [{}]", fechaAValidar, YYYYMMDD_HH24MMSS);
            throw new RuntimeException("formato de fecha incompatible");
        }
    }
}
