package biblioteca.Spring.Boot.ControllerTests;

import biblioteca.Spring.Boot.Controllers.SocioController;
import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.DTO.UsuarioDTO;
import biblioteca.Spring.Boot.Entities.Rol;
import biblioteca.Spring.Boot.Repositories.SocioRepository;
import biblioteca.Spring.Boot.Repositories.UsuarioRepository;
import biblioteca.Spring.Boot.Security.JwtUtil;
import biblioteca.Spring.Boot.Security.UsuarioDetailsService;
import biblioteca.Spring.Boot.Service.SocioService;
import jakarta.servlet.ServletException;
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

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SocioController.class)
public class SocioControllerTest {

    @MockitoBean
    SocioService socioService;

    @MockitoBean
    SocioRepository socioRepository;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    @MockitoBean
    private UsuarioRepository  usuarioRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void listarSocios() throws Exception {
        var socio1 = new SocioDTO(1, "Alexander", "alexanderliberacion@gmail.com");
        Mockito.when(socioService.listarTodos()).thenReturn(List.of(socio1));

        mockMvc.perform(MockMvcRequestBuilders.get("/socio/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idSocio").value("1"))
                .andExpect(jsonPath("$[0].nombre").value("Alexander"))
                .andExpect(jsonPath("$[0].email").value("alexanderliberacion@gmail.com"));
        Mockito.verify(socioService).listarTodos();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearSocio() throws Exception {
        var socio1 = new SocioDTO(1, "Alexander", "alexanderliberacion@gmail.com");
        Mockito.when(socioService.crearSocio(Mockito.any(SocioDTO.class))).thenReturn(socio1);

        mockMvc.perform(MockMvcRequestBuilders.post("/socio/socio")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(socio1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSocio").value(1))
                .andExpect(jsonPath("$.nombre").value("Alexander"))
                .andExpect(jsonPath("$.email").value("alexanderliberacion@gmail.com"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void crearFallido() throws Exception {
        var socio1 = new SocioDTO(1, "", "alexanderliberacion@gmail.com");
        Mockito.when(socioService.crearSocio(Mockito.any(SocioDTO.class))).thenThrow(new RuntimeException("No se ha podido crear el socio"));

        mockMvc.perform(MockMvcRequestBuilders.post("/socio/socio")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(socio1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizarSocio() throws Exception {
        Integer idSocio = 2;
        var socio1 = new SocioDTO(idSocio, "Alexander1", "alexanderliberacion2@gmail.com");
        Mockito.when(socioService.actualizarSocio(idSocio, socio1)).thenReturn(Optional.of(socio1));

        mockMvc.perform(MockMvcRequestBuilders.put("/socio/actualizar/{idSocio}", idSocio)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(socio1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSocio").value(idSocio))
                .andExpect(jsonPath("$.nombre").value("Alexander1"))
                .andExpect(jsonPath("$.email").value("alexanderliberacion2@gmail.com"));
        Mockito.verify(socioService).actualizarSocio(idSocio, socio1);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizarFallido() throws Exception {
        Integer idSocio = 0;
        var socioActualizado = new SocioDTO(idSocio, "Alexander1", "alexanderliberacion2@gmail.com");
        Mockito.when(socioService.actualizarSocio(idSocio, socioActualizado)).thenThrow(new RuntimeException("No se ha podido actualizar el socio"));

        ServletException exception = Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.put("/socio/actualizar/{idSocio}", idSocio)
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                            .content(objectMapper.writeValueAsString(socioActualizado)))
                    .andExpect(status().isInternalServerError());
        });
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarSocio() throws Exception {
        Integer idSocio = 1;
        Mockito.doNothing().when(socioService).eliminarSocio(idSocio);

        mockMvc.perform(MockMvcRequestBuilders.delete("/socio/borrar/{idSocio}", idSocio)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
        Mockito.verify(socioService).eliminarSocio(idSocio);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void eliminarFallido() throws Exception {
        Integer idSocio = 0;
        Mockito.doThrow(new RuntimeException("No se ha podido borrar el socio")).when(socioService).eliminarSocio(idSocio);

        ServletException exception = Assertions.assertThrows(ServletException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.delete("/socio/borrar/{idSocio}", idSocio)
                            .contentType(String.valueOf(MediaType.APPLICATION_JSON)));
        });

        Assertions.assertInstanceOf(RuntimeException.class, exception.getCause());
        Assertions.assertEquals("No se ha podido borrar el socio", exception.getCause().getMessage());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void listaUsuariosPendientes() throws Exception {
        var usuario1 = new UsuarioDTO(1L, "alexanderliberacion@gmail.com","gfgkffg", Rol.BASIC, null);
        Mockito.when(socioService.listaUsuariosPendientes()).thenReturn(List.of(usuario1));

        mockMvc.perform(MockMvcRequestBuilders.get("/socio/usuarios-pendientes")
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(List.of(usuario1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("alexanderliberacion@gmail.com"))
                .andExpect(jsonPath("$[0].password").value("gfgkffg"))
                .andExpect(jsonPath("$[0].rol").value("BASIC"));
        Mockito.verify(socioService).listaUsuariosPendientes();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void activarSocio() throws Exception {
        Long idUsuario = 2L;
        var socioEntrada = new SocioDTO(null ,"Alexander", "alexanderliberacion@gmail.com");
        var socioSalida = new SocioDTO(1, "Alexander", "alexanderliberacion@gmail.com");

        Mockito.when(socioService.activarSocio(idUsuario, socioEntrada)).thenReturn(socioSalida);

        mockMvc.perform(MockMvcRequestBuilders.patch("/socio/activacion/{idUsuario}", idUsuario)
                .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                .content(objectMapper.writeValueAsString(socioEntrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSocio").value(1))
                .andExpect(jsonPath("$.nombre").value("Alexander"))
                .andExpect(jsonPath("$.email").value("alexanderliberacion@gmail.com"));
        Mockito.verify(socioService).activarSocio(idUsuario, socioEntrada);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void activarFallido() throws Exception {
        Long idUsuario = 0L;
        var socioEntrada = new SocioDTO(null ,"Alexander", "alexanderliberacion@gmail.com");
        var socioSalida = new SocioDTO(1, "Alexander", "alexanderliberacion@gmail.com");

        Mockito.when(socioService.activarSocio(idUsuario, socioEntrada)).thenThrow(new RuntimeException("No se ha podido activar al socio"));

       ServletException exception = Assertions.assertThrows(ServletException.class, () -> {
           mockMvc.perform(MockMvcRequestBuilders.patch("/socio/activacion/{idUsuario}", idUsuario)
                           .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                           .content(objectMapper.writeValueAsString(socioEntrada)))
                   .andExpect(status().isBadRequest());
       });
        Assertions.assertInstanceOf(RuntimeException.class, exception.getCause());
        Assertions.assertEquals("No se ha podido activar al socio", exception.getCause().getMessage());
    }
}
