package cl.lherrera.rc.fechas.principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class FechasUtil {

    private static final Logger log = LoggerFactory.getLogger(FechasUtil.class);

    /**
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
            log.error("la fehcha [{}], es incompatible con el patron ", fechaGregoriana, patronLiteralGregoriano);
            throw new RuntimeException("formato de fecha incompatible");
        }
        log.info("Fin - validaFechaGregoriana([{}]), es válida", fechaGregoriana);
    }

    public static void main(String args[]) {
        validaFechaGregoriana("2021-12-13 04:69:31");
    }
}
