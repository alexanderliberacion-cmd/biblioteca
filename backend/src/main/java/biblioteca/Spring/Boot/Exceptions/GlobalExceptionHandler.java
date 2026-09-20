package biblioteca.Spring.Boot.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{


    @ExceptionHandler(OperacionNoPermitidaException.class)
    public ResponseEntity<ResponseError> operacionNoPermitida(OperacionNoPermitidaException ex) {
        ResponseError error = new ResponseError(403, ex.getMessage());
        ResponseEntity<ResponseError> response = ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
        return response;
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ResponseError>  recursoNoEncontrado(RecursoNoEncontradoException ex) {
        ResponseError error = new ResponseError(404, ex.getMessage());
        ResponseEntity<ResponseError> response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        return response;
    }

    @ExceptionHandler(RecursoYaExistente.class)
    public ResponseEntity<ResponseError> recursoYaExistente(RecursoYaExistente ex) {
        ResponseError error = new ResponseError(409, ex.getMessage());
        ResponseEntity<ResponseError> response = ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        return response;
    }
}
