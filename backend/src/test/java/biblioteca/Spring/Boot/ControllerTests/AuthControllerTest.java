package biblioteca.Spring.Boot.ControllerTests;

import biblioteca.Spring.Boot.Controllers.AuthController;
import biblioteca.Spring.Boot.DTO.LoginRequestDTO;
import biblioteca.Spring.Boot.DTO.LoginResponseDTO;
import biblioteca.Spring.Boot.DTO.RegisterRequestDTO;
import biblioteca.Spring.Boot.Security.JwtUtil;
import biblioteca.Spring.Boot.Security.UsuarioDetailsService;
import biblioteca.Spring.Boot.Service.AuthService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UsuarioDetailsService usuarioDetailsService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void loginSuccess() throws Exception{
        var request = new LoginRequestDTO("AlexanderQuirosAmador@gmail.com", "123454");
        var response = new LoginResponseDTO("AlexanderQuirosAmador@gmail.com", "Basic", "mocked-jwt-util-xyz");

        Mockito.when(authService.login(request)).thenReturn(response);

        mockMvc.perform(post("/auth/login") // A) Aquí pones la URL real que definiste en tu @PostMapping del AuthController
                        .contentType(MediaType.APPLICATION_JSON) // B) Le dices que envías un JSON
                        .content(objectMapper.writeValueAsString(request))) //
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("AlexanderQuirosAmador@gmail.com")) // E) Validamos la respuesta
                .andExpect(jsonPath("$.token").value("mocked-jwt-util-xyz"));

    }

    @Test
    void loginFail() throws Exception{
        var request = new LoginRequestDTO("", "123454");
        Mockito.when(authService.login(request)).thenThrow(new RuntimeException("Error al loguearse"));

        ServletException exception = Assertions.assertThrows(ServletException.class,()->{
            mockMvc.perform(post("/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        });
        Assertions.assertInstanceOf(RuntimeException.class, exception.getCause());
        Assertions.assertEquals("Error al loguearse", exception.getCause().getMessage());
    }

    @Test
    void registerSuccess() throws Exception {
        var request = new RegisterRequestDTO("alexanderquirosamador@gmail.com", "12345454");
        var response = new LoginResponseDTO("alexanderquirosamador@gmail.com", "Basic", "mocked-jwt-util-xyz");

        Mockito.when(authService.register(request)).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("alexanderquirosamador@gmail.com"))
                .andExpect(jsonPath("$.token").value("mocked-jwt-util-xyz"));
    }

    @Test
    void registerFail() throws Exception {
        var request = new RegisterRequestDTO("", "12345454");
        Mockito.when(authService.register(request)).thenThrow(new RuntimeException("Error al loguearse"));

        ServletException exception = Assertions.assertThrows(ServletException.class,()->{
            mockMvc.perform(post("/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));
        });
        Assertions.assertInstanceOf(RuntimeException.class, exception.getCause());
        Assertions.assertEquals("Error al loguearse", exception.getCause().getMessage());

    }
}
