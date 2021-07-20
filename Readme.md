# Recetario de fechas

Conjunto de métodos utilitarios para el manejo de fechas. Los servicios implementados son los siguientes:

- Validar estructura de una fecha en formatos: `yyyy-MM-dd HH:mm:ss` y `dd-MM-yyyy HH:mm:ss`.
  
- Obtiener la fecha `Date` con zona horaria `America/Santiago`, localidad `Locale` en español Chile desde un `String` con el formato `dd-MM-yyyy HH:mm:ss`.

- Transforma o hace un parse desde un `Date` a por ejemplo: `sábado 17 de julio de 2021`.

- Calcula diferencias entre dos fechas, en unidades de tiempo: años, meses, días, segundos, milisegundos. Un método para cada uno, algunos privados que podrían ser públicos.

- Toma una fecha y aplica el formáto local indicado anteriormente con zona horaria y localidad.

- Trasforma un `Date` en una salida de cadena como la siguiente: `366 días, 0 horas, 1 minutos y 16 segundos` que representa la diferencia entre el `Date` entregado como argumento y la fecha actual.

> Dudas se pueden resolver en los Test o comentarios extras. Al utilizar en productivo, convendría realizar una refactorización, ya que el objeto en ese contexto será más funcional y menos explicativo.