package modelo;

public class Ejemplar implements Identificable<Integer> {
    private String isbn;
    private EstadoEjemplar estado;
    private Integer id;

    public Ejemplar(String isbn, EstadoEjemplar estado, Integer id) {
        this.isbn = isbn;
        this.estado = estado;
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public EstadoEjemplar getEstado() {
        return estado;
    }

    public void setEstado(EstadoEjemplar estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Ejemplar{" +
                "Isbn=" + isbn +
                ", estado=" + estado +
                ", id=" + id +
                '}';
    }

    public void marcarComoPrestado() {
        estado = EstadoEjemplar.PRESTADO;
    }

    public void marcarComoDisponible() {
        estado = EstadoEjemplar.DISPONIBLE;
    }

    @Override
    public Integer getId() {
        return id;
    }
}
