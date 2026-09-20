package biblioteca.Spring.Boot.Controllers;

import biblioteca.Spring.Boot.DTO.LoginRequestDTO;
import biblioteca.Spring.Boot.DTO.LoginResponseDTO;
import biblioteca.Spring.Boot.DTO.RegisterRequestDTO;
import biblioteca.Spring.Boot.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    
    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO userDTO) {
        return ResponseEntity.ok(service.login(userDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponseDTO> register(@RequestBody RegisterRequestDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(userDTO));
    }
}
