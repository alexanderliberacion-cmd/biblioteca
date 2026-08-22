package biblioteca.Spring.Boot.DTO;


import jakarta.validation.constraints.NotBlank;


public record EjemplarDTO(
        Integer idEjemplar,
       @NotBlank(message = "El estado no puede estar vacio") String estado,
       @NotBlank(message = "El isbn no puede estar vacio") String isbn) {
}