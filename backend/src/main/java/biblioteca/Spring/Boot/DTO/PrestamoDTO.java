package biblioteca.Spring.Boot.DTO;



import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PrestamoDTO(
       @NotNull(message = "La fecha de inicio no puede ser nula") LocalDate fechaInicio,
       @NotNull(message = "La fecha limite no puede ser nula") LocalDate fechaLimite,
        LocalDate fechaDevolucion,
       @NotNull(message = "El id del socio no puede ser nulo") Integer idSocio,
       @NotNull(message = "El id del ejemplar no puede ser nulo") Integer idEjemplar) {
}