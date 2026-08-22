package biblioteca.Spring.Boot.DTO;


import jakarta.validation.constraints.NotBlank;

public record LibroDTO(
       @NotBlank(message = "El isbn no puede estar vacio") String isbn,
       @NotBlank(message = "El titulo no puede estar vacio") String titulo,
       @NotBlank(message = "El autor no puede estar vacio") String autor) {
}