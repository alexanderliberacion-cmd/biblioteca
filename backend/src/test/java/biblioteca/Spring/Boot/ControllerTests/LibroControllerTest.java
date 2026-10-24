package biblioteca.Spring.Boot.ControllerTests;

import biblioteca.Spring.Boot.Controllers.LibroController;
import biblioteca.Spring.Boot.DTO.LibroDTO;
import biblioteca.Spring.Boot.Repositories.LibroRepository;
import biblioteca.Spring.Boot.Security.JwtUtil;
import biblioteca.Spring.Boot.Security.UsuarioDetailsService;
import biblioteca.Spring.Boot.Service.LibroService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.springframework.security.test.context.support.WithMockUser;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LibroController.class)
public class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    @MockitoBean
    private LibroService libroService;

    @MockitoBean
    private LibroRepository libroRepository;

    @Test
    void listarLibros() throws Exception {
        var libro1 = new LibroDTO("12324334", "prueba", "yo");
        Mockito.when(libroService.listarLibros()).thenReturn(List.of(libro1));

        mockMvc.perform(MockMvcRequestBuilders.get("/libro/listar")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].isbn").value("12324334"))
                .andExpect(jsonPath("$[0].titulo").value("prueba"))
                .andExpect(jsonPath("$[0].autor").value("yo"));
        Mockito.verify(libroService).listarLibros();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearLibro() throws Exception {
        var libro1 = new LibroDTO("12324334", "prueba", "yo");
        Mockito.when(libroService.crearLibro(Mockito.any(LibroDTO.class))).thenReturn(Optional.of(libro1));

        mockMvc.perform(MockMvcRequestBuilders.post("/libro/libro")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(libro1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("12324334"))
                .andExpect(jsonPath("$.titulo").value("prueba"))
                .andExpect(jsonPath("$.autor").value("yo"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearFallido() throws Exception {
        var libro1 = new LibroDTO("12324334", "", "yo");
        Mockito.when(libroService.crearLibro(Mockito.any(LibroDTO.class))).thenThrow(new RuntimeException("El libro no se ha podido crear"));

        mockMvc.perform(MockMvcRequestBuilders.post("/libro/libro")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(libro1)))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizarExitoso() throws Exception {
        var isbn = "12324335";
        var libroActualizado = new LibroDTO(isbn, "Prueba2", "yo mismo");
        Mockito.when(libroService.actualizarLibro(isbn, libroActualizado)).thenReturn(Optional.of(libroActualizado));

        mockMvc.perform(MockMvcRequestBuilders.put("/libro/actualizar/{isbn}", isbn)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(libroActualizado))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andExpect(jsonPath("$.titulo").value("Prueba2"))
                .andExpect(jsonPath("$.autor").value("yo mismo"));
        Mockito.verify(libroService).actualizarLibro(isbn, libroActualizado);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizaFallido() throws Exception {
        var isbn = "12324";
        var libroActualizado = new LibroDTO(isbn, "", "yo mismo");
        Mockito.when(libroService.actualizarLibro(isbn, libroActualizado)).thenThrow(new RuntimeException("El libro no se ha podido modificar"));

        mockMvc.perform(MockMvcRequestBuilders.put("/libro/actualizar/{isbn}", isbn)
                        .with(csrf())
                .content(objectMapper.writeValueAsString(libroActualizado))
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarLibro() throws Exception {
        var isbn = "12324334";
        Mockito.doNothing().when(libroService).eliminarLibro(isbn);

        mockMvc.perform(MockMvcRequestBuilders.delete("/libro/borrar/{isbn}", isbn)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
        ).andExpect(status().isOk());
        Mockito.verify(libroService).eliminarLibro(isbn);

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarFallido() throws Exception {
        var isbnFallido = "12324334";
        Mockito.doThrow(new RuntimeException("Error al eliminar")).when(libroService).eliminarLibro(isbnFallido);
        ServletException exception = Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.delete("/libro/borrar/{isbn}", isbnFallido)
                    .contentType(String.valueOf(MediaType.APPLICATION_JSON)));
        });
        Assertions.assertInstanceOf(RuntimeException.class, exception.getCause());
        Assertions.assertEquals("Error al eliminar", exception.getCause().getMessage());
    }


}
