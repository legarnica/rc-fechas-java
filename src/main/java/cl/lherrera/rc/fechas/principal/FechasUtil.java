package cl.lherrera.rc.fechas.principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static void main(String args[]) {
//        validaFechaGregoriana("2021-12-13 04:69:31"); // error minutos
//        validaFechaGregorianaLocal("13-12-2021 24:59:69"); // error segundos
    }

}
