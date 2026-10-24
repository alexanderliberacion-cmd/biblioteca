package biblioteca.Spring.Boot.Exceptions;

public class OperacionNoPermitida extends RuntimeException {
    public OperacionNoPermitida(String message) {
        super(message);
    }
}
