package modelo;

public record Libro(String isbn, String titulo, String autor) implements Identificable<String> {
    public Libro {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("El isbn no puede ser nulo");
        }

        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("El titulo no puede ser nulo");
        }

        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("El autor no puede ser nulo");
        }
    }

    @Override
    public String getId() {
        return isbn;
    }
}
