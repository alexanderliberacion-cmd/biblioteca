package biblioteca.Spring.Boot.ControllerTests;


import biblioteca.Spring.Boot.Controllers.PrestamoController;
import biblioteca.Spring.Boot.DTO.PrestamoDTO;
import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.Entities.PrestamoEntity;
import biblioteca.Spring.Boot.Repositories.PrestamoRepository;
import biblioteca.Spring.Boot.Repositories.UsuarioRepository;
import biblioteca.Spring.Boot.Security.JwtUtil;
import biblioteca.Spring.Boot.Security.UsuarioDetailsService;
import biblioteca.Spring.Boot.Service.PrestamoService;
import jakarta.servlet.ServletException;
import lombok.With;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PrestamoController.class)
public class PrestamoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PrestamoService service;

    @MockitoBean
    private PrestamoEntity entity;

    @MockitoBean
    private PrestamoRepository repository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = {"ADMIN", "SOCIO"})
    void listarPrestamos() throws Exception {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.of(2020, 1, 1);
        var prestamo1 = new PrestamoDTO(fechaInicio, fechaLimite, null, 1, 2);

        Mockito.when(service.listarPrestamos()).thenReturn(List.of(prestamo1));

        mockMvc.perform(MockMvcRequestBuilders.get("/prestamo/listar")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(List.of(prestamo1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].fechaInicio").value(fechaInicio.toString()))
                .andExpect(jsonPath("$.[0].fechaLimite").value(fechaLimite.toString()))
                .andExpect(jsonPath("$.[0].idSocio").value(1))
                .andExpect(jsonPath("$.[0].idEjemplar").value(2));
        Mockito.verify(service).listarPrestamos();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarAtrasados() throws Exception {
        var socio1 = new SocioDTO(1, "Juan", "juanito123@gmail.com");

        Mockito.when(service.sociosConPrestamosAtrasados()).thenReturn(List.of(socio1));

        mockMvc.perform(MockMvcRequestBuilders.get("/prestamo/socios-atrasados")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(List.of(socio1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idSocio").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].email").value("juanito123@gmail.com"));
        Mockito.verify(service).sociosConPrestamosAtrasados();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void librosMasPrestados() throws Exception {
        List<String> simulados = List.of("quijote", "les miserables", "geronimo stilton");
        Mockito.when(service.librosMasPrestados()).thenReturn(simulados);
        mockMvc.perform(MockMvcRequestBuilders.get("/prestamo/libros-mas-prestados")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(List.of())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0]").value("quijote"))
                .andExpect(jsonPath("$[1]").value("les miserables"))
                .andExpect(jsonPath("$[2]").value("geronimo stilton"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void mesesConMasPrestamos() throws Exception {

        Map<String, Long> meses = Map.of(
                "October", 5L,
                "December", 3L
                );
        Mockito.when(service.mesesConMasPrestamos()).thenReturn(meses);

        mockMvc.perform(MockMvcRequestBuilders.get("/prestamo/meses-mas-prestamos")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(List.of(meses))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.October").value(5L))
                .andExpect(jsonPath("$.December").value(3L))
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void prestamosActivosSocio() throws Exception {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.of(2020, 1, 1);
        Integer idSocio = 1;
        var prestamo1 = new PrestamoDTO(fechaInicio, fechaLimite, null, idSocio, 2);

        Mockito.when(service.listarPrestamosActivosPorSocio(idSocio)).thenReturn(List.of(prestamo1));
        mockMvc.perform(MockMvcRequestBuilders.get("/prestamo/prestamos-activos-socio")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(List.of(prestamo1)))
                .param("idSocio", String.valueOf(idSocio)))
                .andExpect(status().isOk());
        Mockito.verify(service).listarPrestamosActivosPorSocio(idSocio);
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "SOCIO"})
    void crearPrestamo() throws Exception {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.of(2020, 1, 1);
        Integer idSocio = 1;
        Integer idEjemplar = 2;
        var prestamo1 = new PrestamoDTO(fechaInicio, fechaLimite, null, idSocio, idEjemplar);

        Mockito.when(service.crearPrestamo(idSocio, idEjemplar)).thenReturn(Optional.of(prestamo1));
        mockMvc.perform(MockMvcRequestBuilders.post("/prestamo/prestamo")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(prestamo1))
                .param("idSocio", String.valueOf(idSocio))
                .param("idEjemplar", String.valueOf(idEjemplar)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fechaInicio").value(fechaInicio.toString()))
                .andExpect(jsonPath("$.fechaLimite").value(fechaLimite.toString()))
                .andExpect(jsonPath("$.idSocio").value(idSocio))
                .andExpect(jsonPath("$.idEjemplar").value(idEjemplar));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "SOCIO"})
    void crearFallido()  throws Exception {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.of(2020, 1, 1);
        Integer idSocio = 1;
        Integer idEjemplar = 2;

        var prestamoFallido = new PrestamoDTO(fechaInicio, fechaLimite, null, null, idEjemplar);
        Mockito.when(service.crearPrestamo(idSocio, idEjemplar)).thenThrow(new RuntimeException("Error al crear"));

        mockMvc.perform(MockMvcRequestBuilders.post("/prestamo/prestamo")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(prestamoFallido)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizarPrestamo() throws Exception {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.of(2021, 1, 1);
        Integer id = 1;
        var prestamo1 = new PrestamoDTO(fechaInicio, fechaLimite, null, 2, 3);

        Mockito.when(service.actualizarPrestamo(id)).thenReturn(Optional.of(prestamo1));

        mockMvc.perform(MockMvcRequestBuilders.put("/prestamo/actualizar/{id}", id)
                        .with(csrf())
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(prestamo1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fechaInicio").value(fechaInicio.toString()))
                .andExpect(jsonPath("$.fechaLimite").value(fechaLimite.toString()))
                .andExpect(jsonPath("$.idSocio").value(2))
                .andExpect(jsonPath("$.idEjemplar").value(3));
        Mockito.verify(service).actualizarPrestamo(id);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizarFallido() throws Exception {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.of(2021, 1, 1);
        Integer id = 1;
        var prestamo1 = new PrestamoDTO(fechaInicio, fechaLimite, null, null, 3);

        Mockito.when(service.actualizarPrestamo(id)).thenThrow(new RuntimeException("Error al actualizar"));

        ServletException exception = Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.put("/prestamo/actualizar/{id}", id)
                            .with(csrf())
                    .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                    .content(objectMapper.writeValueAsString(prestamo1)))
                    .andExpect(status().isInternalServerError());
        });

    }

    @Test
    @WithMockUser(roles = {"ADMIN", "SOCIO"})
    void devolverPrestamo() throws Exception {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.of(2021, 1, 1);
        Integer idEjemplar = 1;
        var prestamo = new PrestamoDTO(fechaInicio, fechaLimite, null, 1, idEjemplar);
        Mockito.when(service.devolverPrestamo(1)).thenReturn(Optional.of(prestamo));

        mockMvc.perform(MockMvcRequestBuilders.put("/prestamo/devolver/{idEjemeplar}", idEjemplar)
                .with(csrf())
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(prestamo)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "SOCIO"})
    void devolverFallido() throws Exception {
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.of(2021, 1, 1);
        Integer idEjemplar = 1;
        var prestamo = new PrestamoDTO(fechaInicio, fechaLimite, null, 1, idEjemplar);
        Mockito.when(service.devolverPrestamo(1)).thenThrow(new RuntimeException("Error al devolver"));

        ServletException exception = Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.put("/prestamo/devolver/{idEjemplar}", idEjemplar)
                            .with(csrf())
                    .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                    .content(objectMapper.writeValueAsString(prestamo)))
                    .andExpect(status().isNotFound());
        });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarPrestamo() throws Exception {
        Integer id = 1;

        Mockito.doNothing().when(service).borrarPrestamo(id);

        mockMvc.perform(MockMvcRequestBuilders.delete("/prestamo/borrar/{id}", id)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        Mockito.verify(service).borrarPrestamo(id);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarFallido() throws Exception {
        Integer id = 0;
        Mockito.doThrow(new RuntimeException("Error al eliminar")).when(service).borrarPrestamo(id);

        ServletException exception = Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.delete("/prestamo/borrar/{id}", id)
                    .contentType(String.valueOf(MediaType.APPLICATION_JSON)));
        });
        Assertions.assertInstanceOf(RuntimeException.class, exception.getCause());
        Assertions.assertEquals("Error al eliminar", exception.getCause().getMessage());
    }
}
