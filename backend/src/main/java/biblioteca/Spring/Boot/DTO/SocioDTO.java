package biblioteca.Spring.Boot.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SocioDTO(
        Integer idSocio,
       @NotBlank(message = "El nombre no puede estar vacio") String nombre,
       @NotBlank(message = "El email no puede estar vacio")
       @Email(message = "Formato no valido")
        String email) {
}