package modelo;

public enum EstadoEjemplar {
    DISPONIBLE,PRESTADO;


    public boolean puedePrestarse() {
        return this == DISPONIBLE;
    }
}
