package biblioteca.Spring.Boot.Exceptions;

public class RecursoYaExistente extends RuntimeException {
    public RecursoYaExistente(String message) {
        super(message);
    }
}
