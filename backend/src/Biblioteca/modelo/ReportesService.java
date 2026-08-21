package modelo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportesService {
    //Reporte para socio con prestamos atrasados
    public static List<Socio> socioConPrestamosAtrasados(List<Prestamo> prestamos) {
        //Filtra los que estan atrasados, los mapea convirtiendolos a socio, busca los distintos y los convierte a lista
        return prestamos.stream()
                .filter(Prestamo::estaAtrasado)
                .map(Prestamo::getSocio)
                .distinct()
                .toList();
    }

    //Top libros mas prestados
    public static List<String> librosMasPrestados(List<Prestamo> prestamos) {
        //Los agrupa segun su isbn y los cuenta
        Map<String, Long> conteoPorIsbn = prestamos.stream()
                .collect(Collectors.groupingBy(prestamo -> prestamo.getEjemplar().getIsbn(), Collectors.counting()));

        //Los ordena de mayor a menor, limita el numero a 5, los mapea segun su clave y los convierte en lista.
        return conteoPorIsbn.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

    }

    //Meses con mas prestamos. Los agrupa segun la fecha de inicio, uniendo año y mes en una string, y los cuenta.
    public static Map<String, Long> mesesConMasPrestamos(List<Prestamo> prestamos) {
        return prestamos.stream()
                .collect(Collectors.groupingBy(prestamo -> prestamo.getFechaInicio().getYear() + "-" + prestamo.getFechaInicio().getMonth().getValue(), Collectors.counting()));
    }
}
