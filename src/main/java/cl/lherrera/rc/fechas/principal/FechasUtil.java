package cl.lherrera.rc.fechas.principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


public class FechasUtil {

    private static final Logger log = LoggerFactory.getLogger(FechasUtil.class);

    /**
     * Estructura gregoriana: WIKI
     * El rango válido para el Calendario Gregoriano es de 4714 A.C. a 9999 D.C.
     * Aunque esta función puede manejar fechas que se remontan hasta 4714 A.C.,
     * tal uso puede no ser significativo. El calendario Gregoriano no fue
     * establecido hasta el 15 de octubre de 1582
     * (o 5 de octubre de 1582 el el calendario Juliano).
     *
     *
     * Valida: AAAA-MM-DD HH:MM:SS
     *
     * valida estructura, valida estructura 2021-12-13 04:59:31, con límites por ejemplo 24 horas y no 25
     * el mecanismo es usar cada validación en un paréntesis, por ejemplo (19|2[0-9])[0-9]{2} y las veces que se
     * repite. Por ejemplo ([0-2])([0-4]):, indica hora desde las 00 hasta las 24. No admite 25
     */
    public static void validaFechaGregoriana(String fechaGregoriana) {
        log.info("Inicio - validaFechaGregoriana([{}])", fechaGregoriana);
        String patronLiteralGregoriano = "^((19|2[0-9])[0-9]{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])(\\s)([0-2])([0-4]):([0-5])([0-9]):([0-5])([0-9]))$";
        Pattern patronGregoriano = Pattern.compile(patronLiteralGregoriano);
        boolean esPatronValido = patronGregoriano.matcher(fechaGregoriana).matches();
        boolean noEsPatronValido = !esPatronValido;
        if(noEsPatronValido){
            log.error("la fehcha [{}], es incompatible con el patron [{}]", fechaGregoriana, patronLiteralGregoriano);
            throw new RuntimeException("formato de fecha incompatible");
        }
        log.info("Fin - validaFechaGregoriana([{}]), es válida", fechaGregoriana);
    }


    /**
     * Valida: DD-MM-AAAA HH:MM:SS, esto se resuelve cambiando el rx de posición, para que se adapte
     * al formato local donde el día está primero que el mes y que el año.
     */
    public static void validaFechaGregorianaLocal(String fechaGregoriana) {
        log.info("Inicio - validaFechaGregorianaLocal([{}])", fechaGregoriana);
        String patronLiteralGregoriano = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-((19|2[0-9])[0-9]{2}(\\s)([0-2])([0-4]):([0-5])([0-9]):([0-5])([0-9]))$";
        Pattern patronGregoriano = Pattern.compile(patronLiteralGregoriano);
        boolean esPatronValido = patronGregoriano.matcher(fechaGregoriana).matches();
        boolean noEsPatronValido = !esPatronValido;
        if(noEsPatronValido){
            log.error("la fehcha [{}], es incompatible con el patron [{}]", fechaGregoriana, patronLiteralGregoriano);
            throw new RuntimeException("formato de fecha incompatible");
        }
        log.info("Fin - validaFechaGregorianaLocal([{}]), es válida", fechaGregoriana);
    }

    /**
     * Obtiene la fecha actual formateada a la forma en que se ve en Chile,
     * adicionalmente se ajusta al horario de santiago en caso que
     * el servidor esté en otro horario.
     */
    public static String obtenerFechaLocalLiteral(){
        String retorno = "";

        String lenguaje = "es";
        String pais = "CH";
        Locale localidad = new Locale(lenguaje, pais);

        Date fecha = GregorianCalendar.getInstance(localidad).getTime();

        // HH es 24 horas, con minúsculas es de 0 a 12.
        String patronLiteral = "dd-MM-yyyy HH:mm:ss";
        SimpleDateFormat formato = new SimpleDateFormat(patronLiteral);
        TimeZone zonaHoraria = TimeZone.getTimeZone("America/Santiago");
        formato.setTimeZone(zonaHoraria);

        retorno = formato.format(fecha);

        return retorno;
    }

    /**
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
        String retorno = null;
        Locale localidad = new Locale("es-CH");
        TimeZone zonaHoraria = TimeZone.getTimeZone("America/Santiago");
        // es mejor un tipo Date con la zona y localidad que setearlos en el
        // SimpleDateFormat.
        Date fechaLocal = GregorianCalendar.getInstance(zonaHoraria, localidad).getTime();
        fechaLocal.setTime(fecha.getTime());

        String patronAFechaPalabras = "EEEE d 'de' MMMM 'de' yyyy";
        DateFormat formato = new SimpleDateFormat(patronAFechaPalabras);
        formato.setTimeZone(zonaHoraria);

        retorno = formato.format(fecha);
        return retorno;
    }

    /**
     * Transforma un literal a una fecha, con formato
     *
     *
     * @param fechaLiteral transformada desde un string
     * @return
     */
    public static Date parseaStringAFecha(String fechaLiteral) {
        validaFechaGregorianaLocal(fechaLiteral); // "dd-MM-yyyy HH:mm:ss"

        // creando una fecha con formato local
        TimeZone zonaHoraria = TimeZone.getTimeZone("America/Santiago");
        Locale localidad = new Locale("es", "CH");
        Date fecha = new GregorianCalendar(zonaHoraria, localidad).getTime();

        String formatoLiteral = "dd-MM-yyyy HH:mm:ss";
        // no le pasamos el locale ni el timezone al formato, ya se los pasamos
        // en la creación del Date. Si los pasamos en el SimpleDateFormat, no
        // funciona pasarlo a palabras, se queda en ingles.
//        DateFormat formato = new SimpleDateFormat(formatoLiteral, localidad);
//        formato.setTimeZone(zonaHoraria);
        DateFormat formato = new SimpleDateFormat(formatoLiteral, localidad);

        try{
            Date fechaPorParametroParceada = formato.parse(fechaLiteral);
            // una vez parseada o transformada, se aplican los formatos locales.
            fecha.setTime(fechaPorParametroParceada.getTime());
        }catch (ParseException e){
            // algo... con el e
        }


        return fecha;
    }

    /**
     * toma una fecha y retorna un objeto mapeado con atributos útiles para
     * operar con esta información.
     * @param fecha
     * @return
     */
    public static Map<String, Integer> separaFechaEnArreglo(Date fecha){
        Map retorno = new HashMap<String, Integer>();
        Calendar calendario = new GregorianCalendar();
        calendario.setTime(fecha);

        retorno.put("DAY_OF_YEAR", calendario.get(Calendar.DAY_OF_YEAR));
        retorno.put("DAY_OF_MONTH", calendario.get(Calendar.DAY_OF_MONTH));
        retorno.put("DAY_OF_WEEK", calendario.get(Calendar.DAY_OF_WEEK));
        retorno.put("HOUR", calendario.get(Calendar.HOUR));
        retorno.put("HOUR_OF_DAY", calendario.get(Calendar.HOUR_OF_DAY));
        retorno.put("MONTH", calendario.get(Calendar.MONTH));
        retorno.put("YEAR", calendario.get(Calendar.YEAR));



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
        long primeraEnMilisegundos = fechaUno.getTime();
        long segundaEnMilisegundos = fechaDos.getTime();
        long diferenciaEnMilisegundos = primeraEnMilisegundos - segundaEnMilisegundos;
        // para sacar los negativos
        long diferenciaAbsoluta = Math.abs(diferenciaEnMilisegundos);
        // convierte a días, esta diferencia en milisegundos, desde milisegundos a días.
        long diasDeDiferencia = TimeUnit.DAYS.convert(diferenciaAbsoluta, TimeUnit.MILLISECONDS);
        return (int) diasDeDiferencia;
    }

    /**
     * Entrega la direfencia de cantidad enteras de segundos entre dos fechas.
     */
    public static int diferenciaFechasEnSegundos(Date primeraFecha, Date segundaFecha) {
        long diferenciaEnMilisegundos = Math.abs(primeraFecha.getTime() - segundaFecha.getTime());
        long diferenciaEnSegundos = TimeUnit.SECONDS.convert(diferenciaEnMilisegundos, TimeUnit.MILLISECONDS);
        return (int) diferenciaEnSegundos;
    }
    /**
     * Se usa solamente para probar diferenciaFechasEnSegundos()
     */
    private static void pruebaDiferenciaFechasEnSegundos() {
        String fechaLitUno = "01-01-2020 23:59:59";
        String fechaLitDos = "01-01-2020 23:59:57";

        int diff = diferenciaFechasEnSegundos(
                parseaStringAFecha(fechaLitUno),
                parseaStringAFecha(fechaLitDos));
        System.out.println(diff);

    }

    /**
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
        TimeZone zonaHoraria = TimeZone.getTimeZone("America/Santiago");
        Locale localidad = new Locale("es", "CH");
        Date fechaActual = GregorianCalendar.getInstance(zonaHoraria, localidad).getTime();
        Date copiaLocalDeFechaNoActual = GregorianCalendar.getInstance(zonaHoraria, localidad).getTime();
        copiaLocalDeFechaNoActual.setTime(fechaNoActual.getTime());

        long diffMilisegundos = Math.abs(fechaActual.getTime() - copiaLocalDeFechaNoActual.getTime());
        long dias = TimeUnit.DAYS.convert(diffMilisegundos, TimeUnit.MILLISECONDS);
        long remanenteDias = diffMilisegundos - TimeUnit.MILLISECONDS.convert(dias, TimeUnit.DAYS);
        long horas = TimeUnit.HOURS.convert(remanenteDias, TimeUnit.MILLISECONDS);
        long remanenteHoras = remanenteDias - TimeUnit.MILLISECONDS.convert(horas, TimeUnit.HOURS);
        long minutos = TimeUnit.MINUTES.convert(remanenteHoras, TimeUnit.MILLISECONDS);
        long remanenteMinutos = remanenteHoras - TimeUnit.MILLISECONDS.convert(minutos, TimeUnit.MINUTES);
        long segundos = TimeUnit.SECONDS.convert(remanenteMinutos, TimeUnit.MILLISECONDS);

        return String.format(diferenciaLiteral, dias, horas, minutos, segundos);
    }
    /**
     * Se usa solamente para probar obtenerDiferenciaLiteralConFechaActual()
     */
    private static void pruebaObtenerDiferenciaLiteralConFechaActual() {
        Date fechaAEnviar = parseaStringAFecha("18-07-2020 11:34:00");
        String diff = obtenerDiferenciaLiteralConFechaActual(fechaAEnviar);
        System.out.println(diff);
    }

    /**
     * Se usa solamente para probar diferenciaDiasFechaLocal()
     */
    private static void pruebaDiferenciaFechaLocal() {
        String fechaLitUno = "01-01-2020 23:59:59";
        String fechaLitDos = "31-12-2020 23:59:59";

        int diff = diferenciaDiasFechaLocal(
                parseaStringAFecha(fechaLitUno),
                parseaStringAFecha(fechaLitDos));
        System.out.println(diff);

    }

    public static void main(String args[]) {


//        validaFechaGregoriana("2021-12-13 04:69:31"); // error minutos
//        validaFechaGregorianaLocal("13-12-2021 24:59:69"); // error segundos
//        log.info("Salida: [{}]", obtenerFechaLocalLiteral());
//        log.info("Salida: [{}]", parseaAFechaPalabras(new Date()));

//        /**
//         * se puede ver que acá solamente se le da el formato para que lo
//         * escriba en palabras, pero no le paso el locale ni el timezone
//         * al SimpleDateFormat, solamente el formato, para demostrar que
//         * dentro del método, es una solución crear una fecha paralela
//         * con los formatos locales y pasar esta fecha parseada a la
//         * fecha con formáto, para que realmente los asuma como "nativo".
//         *
//         */
//        Date fecha = parseaStringAFecha("13-12-2021 23:59:59");
//        System.out.println(new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy").format(fecha));

//        pruebaDiferenciaFechaLocal();
//        pruebaDiferenciaFechasEnSegundos();
        pruebaObtenerDiferenciaLiteralConFechaActual();


    }

}
