package cl.lherrera.rc.fechas.principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private final static SimpleDateFormat DATEFORMAT_LOCAL = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

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
        retorno = DATEFORMAT_LOCAL.format(GregorianCalendar.getInstance(ZONA_HORARIA, LOCALIDAD).getTime());

        log.info("[obtenerFechaLocalLiteral]: [{}] - Fin", retorno);
        return retorno;
    }

    /**
     * PARSEO DE FECHA A LITERALES EN ESPAÑOL.
     *
     * Transforma una fecha Date a un String con el nombre literal
     * como: [sábado 17 de julio de 2021]
     *
     * Nota: Ojo con inicializar el SimpleDateFormat con el Locale.
     *       si se hace eso, se queda en inglés. Debe ser en
     *       GregorianCalendar.getInstance(localidad).getTime();
     *       esto retornará un tipo Date.
     *
     * @param fecha Date.
     * @return fecha en palabras.
     */
    public static String parseaAFechaPalabras(Date fecha) {
        log.info("[parseaAFechaPalabras]: fecha [{}] - Inicio", fecha);
        String retorno = null;
        // es mejor un tipo Date con la zona y localidad que setearlos en el
        // SimpleDateFormat.
        Calendar calendarioQuePasaAEspaniolChile = GregorianCalendar.getInstance(ZONA_HORARIA, LOCALIDAD);
        calendarioQuePasaAEspaniolChile.setTime(fecha);
        Date fechaLocal = calendarioQuePasaAEspaniolChile.getTime();

        DateFormat formato = new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy");
        formato.setTimeZone(ZONA_HORARIA);

        retorno = formato.format(fechaLocal);
        log.info("[parseaAFechaPalabras]: [{}] - Fin", retorno);
        return retorno;
    }

    /**
     * Retorna la diferencia en días entre dos fechas tipo Date.
     *
     * Transforma las fechas a milisegundos, y luego se operan en
     * milisegundos, donde el resultado se transforma a días
     * desde los milisegundos que resultaron de la resta.
     *
     * Nota: los milisegundos de una fecha, son medidos desde 1900.
     * Es por este motivo que se puede transformar una fecha Date
     * a milisegundos, desde ese momento.
     *
     * Nota 2: Hay que cuidar el resultado cuando se especifica la hora
     * no contará otro día si no hay al menos 24 horas de diferencia
     * aunque sean distintos días.
     */
    public static int diferenciaDiasFechaLocal(Date fechaUno, Date fechaDos) {
        int retorno = 0;
        log.info("[diferenciaDiasFechaLocal] - inicio: [fechaUno, fechaDos][{}, {}] - Fin", fechaUno, fechaDos);
        long diferenciaEnMilisegundos = Math.abs(fechaUno.getTime() - fechaDos.getTime());
        // convierte a días, esta diferencia en milisegundos, desde milisegundos a días.
        long diasDeDiferencia = TimeUnit.DAYS.convert(diferenciaEnMilisegundos, TimeUnit.MILLISECONDS);
        retorno = (int) diasDeDiferencia;
        log.info("[diferenciaDiasFechaLocal] - fin: [retorno][{}] - Fin", retorno);
        return retorno;
    }

    /**
     * DIFERENCIA DE FECHAS NOTADAS EN SEGUNDOS
     *
     * Obtiene la direfencia esteraa de segundos, entre dos fechas.
     */
    public static int diferenciaFechasEnSegundos(Date primeraFecha, Date segundaFecha) {
        log.info("[diferenciaFechasEnSegundos - inicio] - [primeraFecha, segundaFecha] [{}, {}]",
                primeraFecha, segundaFecha);
        int retorno = 0;

        long diferenciaEnMilisegundos = Math.abs(primeraFecha.getTime() - segundaFecha.getTime());
        long diferenciaEnSegundos = TimeUnit.SECONDS.convert(diferenciaEnMilisegundos, TimeUnit.MILLISECONDS);

        retorno = (int) diferenciaEnSegundos;
        log.info("[diferenciaFechasEnSegundos - fin] [retorno] [{}]", retorno);

        return retorno;
    }
    /**
     * PALABRAS CON EL TIEMPO TRASCURRIDO.
     *
     * Obtiene un literal con la difererencia desde una fecha a la actual,
     * como las que se entrega en los post de redes sociales para indicar
     * la antiguedad de una noticia. Una salida de ejemplo sería:
     * "366 días, 0 horas, 1 minutos y 16 segundos".
     *
     * Nota: La forma en que se entrega el literal puede variar, se
     * podría ir concatenando una cadena si es que los valores
     * son superiores a cero, si se quiere mostrar solamente
     * aquellos que poseen valor, entre otros.
     *
     * Nota 2: Esta forma utiliza el cálculo de milisegundos, también es una alternativa el
     * obtener los milisegundos y cada 3600000 calcular una hora por ejemplo, aunque
     * el approach utilizado me parece más correcto debido a que se utiliza
     * la API oficial de java.
     *
     * Nota 3: Se definen las localidades en ambas fechas, la actual que se calcula por sistema
     * y la entregada como parámetro. Esto para que tenga sentido realizar operaciones entre
     * fechas homogeneas. Por ejemplo puede llegar una fecha en UTC, entonces se asume que se quiere
     * constrastar con una fecha del equipo (local), esta estará con el TimeZone local, por eso
     * la fecha entregada por parámetro se reconfigura a local en este método.
     */
    public static String obtenerDiferenciaLiteralConFechaActual(Date fechaNoActual) {
        String diferenciaLiteral = "%d días, %d horas, %d minutos y %d segundos";

        Date fechaActual = formateaZonaHorariaALaFecha(new Date());
        Date copiaLocalDeFechaNoActual = formateaZonaHorariaALaFecha(fechaNoActual);

        long diffMilisegundos = FechasUtilDos.obtenerDiferenciaEnMiliSegundos(fechaActual, copiaLocalDeFechaNoActual);
        long dias = FechasUtilDos.diferenciaDiasFechaLocal(fechaActual, copiaLocalDeFechaNoActual);

        long remanenteDias = diffMilisegundos - TimeUnit.MILLISECONDS.convert(dias, TimeUnit.DAYS);
        long horas = TimeUnit.HOURS.convert(remanenteDias, TimeUnit.MILLISECONDS);
        long remanenteHoras = remanenteDias - TimeUnit.MILLISECONDS.convert(horas, TimeUnit.HOURS);
        long minutos = TimeUnit.MINUTES.convert(remanenteHoras, TimeUnit.MILLISECONDS);
        long remanenteMinutos = remanenteHoras - TimeUnit.MILLISECONDS.convert(minutos, TimeUnit.MINUTES);
        long segundos = TimeUnit.SECONDS.convert(remanenteMinutos, TimeUnit.MILLISECONDS);

        return String.format(diferenciaLiteral, dias, horas, minutos, segundos);
    }

    /**
     * Obtiene la representación en milisegundos, de la diferencia entre dos fechas
     *
     * @param fechaUno
     * @param fechaDos
     * @return Long diferencia en milisegundos.
     */
    private static Long obtenerDiferenciaEnMiliSegundos(Date fechaUno, Date fechaDos) {
        return Math.abs(fechaUno.getTime() - fechaDos.getTime());
    }

    /**
     * Aplica el formáto local, a una fecha, para realizar operaciones con la hora local y en
     * español. Adicionalmente, la zona horaria permite retornar una fecha con esa zona
     * y no dependerá si el servidor está con otra zona.
     *
     * @param fecha fecha a aplicar el formato local.
     * @return Date fecha con formato local.
     */
    private static Date formateaZonaHorariaALaFecha(Date fecha) {
        Calendar calendarioLocal = GregorianCalendar.getInstance(ZONA_HORARIA, LOCALIDAD);
        calendarioLocal.setTime(fecha);

        return calendarioLocal.getTime();
    }
}
