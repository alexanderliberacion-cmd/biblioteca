package modelo;

public record Socio(int id, String nombre, String email) implements Identificable<Integer> {
    public Socio{
        if (id < 0){
            throw new IllegalArgumentException("El id no puede ser negativo");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser nulo");
        }
    }

    @Override
    public Integer getId() {
        return id;
    }
}
