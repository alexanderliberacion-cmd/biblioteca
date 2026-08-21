package modelo;

import almacen.Almacen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Prestamo{
    private Socio socio;
    private Ejemplar ejemplar;
    private LocalDate fechaInicio;
    private LocalDate fechaLimite;
    private Optional<LocalDate> fechaDevolucion = Optional.empty();


    private Prestamo(Ejemplar ejemplar, Socio socio, LocalDate fechaInicio, LocalDate fechaLimite) {
        this.ejemplar = ejemplar;
        this.socio = socio;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
    }


    public static Optional<Prestamo> crearPrestamo(Socio socio, LocalDate fechaInicio, LocalDate fechaLimite, Ejemplar ejemplar) {
        if(ejemplar.getEstado().puedePrestarse()) {
           ejemplar.marcarComoPrestado();
           return Optional.of(new Prestamo(ejemplar, socio, fechaInicio, fechaLimite));
        }
        return Optional.empty();
    }

    public Socio getSocio() {
        return socio;
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public Optional<LocalDate> getFechaDevolucion() {
        return fechaDevolucion;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "socio=" + socio +
                ", ejemplar=" + ejemplar +
                ", fechaInicio=" + fechaInicio +
                ", fechaLimite=" + fechaLimite +
                ", fechaDevolucion=" + fechaDevolucion +
                '}';
    }


    //El metodo primero revisa si la fecha de devolucion es despues de la fecha limite, si no hay devolucion se revisa
    //cuanto se ha retrasado comparando la fecha actual con la fecha limite.
    public boolean estaAtrasado() {
        return fechaDevolucion.map(fechaDevolucion-> fechaDevolucion.isAfter(fechaLimite))
                .orElseGet(() -> LocalDate.now().isAfter(fechaLimite));
    }

    //Metodo para ver si esta devuelto
    public boolean estaDevuelto() {
        return fechaDevolucion.isPresent();
    }

    //metodo para obtener el estado
    public EstadoPrestamo obtenerEstado() {
        if(estaDevuelto()) {
            return EstadoPrestamo.DEVUELTO;
        }
        return EstadoPrestamo.ACTIVO;
    }

    //Metodo para devolver, revisa si esta devuelto ya y si no envuelva la fecha en un optional y se la pasa a fechaDevolucion.
    public boolean devolverLibro(LocalDate fecha) {
        if(!estaDevuelto()) {
            fechaDevolucion = Optional.of(fecha);
            return true;
        }
        return false;
    }
}
