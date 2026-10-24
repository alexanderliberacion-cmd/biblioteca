package biblioteca.Spring.Boot.ControllerTests;

import biblioteca.Spring.Boot.Controllers.EjemplarController;
import biblioteca.Spring.Boot.DTO.EjemplarDTO;
import biblioteca.Spring.Boot.Security.JwtUtil;
import biblioteca.Spring.Boot.Security.UsuarioDetailsService;
import biblioteca.Spring.Boot.Service.EjemplarService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(EjemplarController.class)
public class EjemplarControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    @MockitoBean
    private EjemplarService ejemplarService;

    @Test
    void listarEjemplar() throws Exception {
        var ejemplar1 = new EjemplarDTO(1, "DISPONIBLE", "232372372372");
        Mockito.when(ejemplarService.listarTodos()).thenReturn(List.of(ejemplar1));

        mockMvc.perform(MockMvcRequestBuilders.get("/ejemplar/listar")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idEjemplar").value(1))
                .andExpect(jsonPath("$[0].estado").value("DISPONIBLE"))
                .andExpect(jsonPath("$[0].isbn").value("232372372372"));
        Mockito.verify(ejemplarService).listarTodos();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearExitoso() throws Exception {
        var ejemplar1 = new EjemplarDTO(1, "DISPONIBLE", "232372372372");
        Mockito.when(ejemplarService.crearEjemplar(Mockito.any(EjemplarDTO.class))).thenReturn(Optional.of(ejemplar1));

        mockMvc.perform(MockMvcRequestBuilders.post("/ejemplar/ejemplar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ejemplar1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEjemplar").value(1))
                .andExpect(jsonPath("$.estado").value("DISPONIBLE"))
                .andExpect(jsonPath("$.isbn").value("232372372372"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearFallido() throws Exception {
        var ejemplar1 = new EjemplarDTO(1, "", "232372372372");
        Mockito.when(ejemplarService.crearEjemplar(Mockito.any(EjemplarDTO.class))).thenThrow(new RuntimeException("Error al crear ejemplar"));

       mockMvc.perform(MockMvcRequestBuilders.post("/ejemplar/ejemplar")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(ejemplar1)))
               .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void borrarEjemplar() throws Exception {
        Integer idEjemplar = 1;

        Mockito.doNothing().when(ejemplarService).eliminarEjemplar(idEjemplar);

        mockMvc.perform(MockMvcRequestBuilders.delete("/ejemplar/borrar/{idEjemplar}", idEjemplar)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(ejemplarService,  Mockito.times(1)).eliminarEjemplar(idEjemplar);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void borrarFallido() throws Exception {
        Integer idFallido = 1;

        Mockito.doThrow(new RuntimeException("Error al borrar ejemeplar")).when(ejemplarService).eliminarEjemplar(idFallido);

        ServletException exception = Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.delete("/ejemplar/borrar/{idEjemplar}", idFallido)
                    .contentType(MediaType.APPLICATION_JSON));

        });

        Assertions.assertInstanceOf(RuntimeException.class, exception.getCause());
        Assertions.assertEquals("Error al borrar ejemeplar", exception.getCause().getMessage());
    }


}
