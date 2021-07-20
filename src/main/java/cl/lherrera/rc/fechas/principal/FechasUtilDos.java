package cl.lherrera.rc.fechas.principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
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
     * Localidad: lenguaje español de chile.
     *
     * Nota: Ojo con inicializar el SimpleDateFormat con el Locale.
     *       si se hace eso, se queda en inglés. Debe ser en
     *       GregorianCalendar.getInstance(localidad).getTime();
     *       esto retornará un tipo Date.
     *
     */
    private static final Locale LOCALIDAD = new Locale("es", "CH");

    /**
     * Zona horaria de Santiago de Chile. Preferir setearlo en el GregorianCalendar por sobre
     * SimpleDateFormat.
     *
     * Nota: Date fecha = new GregorianCalendar(zonaHoraria, localidad).getTime();
     *       de esta forma nos aseguramos que la zona horaría sea realmente
     *       seteada y no tratada en el formateador ya que el cambio de zona
     *       si el servidor no es de esa zona no está garantizado.
     */
    private static final TimeZone ZONA_HORARIA = TimeZone.getTimeZone("America/Santiago");

    /**
     * Herramienta la gestión de la representación de la fecha con Patrón literal,
     * "dd-MM-yyyy HH:mm:ss" que representa el formato de fecha usado
     * típicamente en español. Como [13-12-2020 00:00:31]
     */
    private final static SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    /**
     * Patrón literal expresión regular, que hace match con fechas del tipo: [2021-12-13 15:59:31]
     *
     */
    private final static String YYYYMMDD_HH24MMSS_RGX = "^((19|2[0-9])[0-9]{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])(\\s)((([0-1])([0-9]))|(([2])([0-3]))):(([0-5])([0-9])):(([0-5])([0-9])))$";

    /**
     * Patrón literal expresión regular, que hace match con fechas del tipo: [21-12-2020 15:59:31]
     *
     * Nota: ((([0-1])([0-9]))|(([2])([0-4]))), verifica que no sea mayor a 24.
     */
    private final static String DDMMYYYY_HH24MMSS_RGX = "^((0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-((19|2[0-9])[0-9]{2})(\\s)((([0-1])([0-9]))|(([2])([0-3]))):(([0-5])([0-9])):(([0-5])([0-9])))$";

    /**
     * Valida estructura [2021-12-13 04:59:31]
     *
     * @param fechaAValidar Fecha a validar.
     */
    public static void validaEstructuraFechaEn(String fechaAValidar) {
        log.info("Inicio - validaEstructuraFechaEn([{}])", fechaAValidar);
        boolean esPatronValido = Pattern.compile(YYYYMMDD_HH24MMSS_RGX).matcher(fechaAValidar).matches();

        boolean noEsPatronValido = !esPatronValido;
        if(noEsPatronValido){
            log.error("la fehcha [{}], es incompatible con el patron [{}]", fechaAValidar, YYYYMMDD_HH24MMSS_RGX);
            throw new RuntimeException("formato de fecha incompatible");
        }
    }

    /**
     * Valida estructura [21-12-2020 15:59:31]
     *
     * @param fechaAValidar Fecha a validar.
     */
    public static void validaEstructuraFechaEs(String fechaAValidar) {
        log.info("Inicio - validaEstructuraFechaEs([{}])", fechaAValidar);
        boolean esPatronValido = Pattern.compile(DDMMYYYY_HH24MMSS_RGX).matcher(fechaAValidar).matches();
        boolean noEsPatronValido = !esPatronValido;
        if(noEsPatronValido){
            log.error("la fecha [{}], es incompatible con el patron [{}]", fechaAValidar, DDMMYYYY_HH24MMSS_RGX);
            throw new RuntimeException("formato de fecha incompatible");
        }
        log.info("Fin - validaEstructuraFechaEs([{}]), es válida", fechaAValidar);
    }

    /**
     * FECHA ACTUAL.
     *
     * Obtiene la fecha actual formateada a la forma en que se ve en Chile,
     * adicionalmente se ajusta al horario de santiago en caso que
     * el servidor esté en otro horario.
     */
    public static String obtenerFechaLocalLiteral(){
        log.info("[obtenerFechaLocalLiteral] - Inicio");
        String retorno = "";
        // formato.setTimeZone(ZONA_HORARIA); // mejor esto NO.
        retorno = formato.format(GregorianCalendar.getInstance(ZONA_HORARIA, LOCALIDAD).getTime());

        log.info("[obtenerFechaLocalLiteral]: [{}] - Fin", retorno);
        return retorno;
    }
}
