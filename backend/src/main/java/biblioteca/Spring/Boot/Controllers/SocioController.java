package biblioteca.Spring.Boot.Controllers;

import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.DTO.UsuarioDTO;
import biblioteca.Spring.Boot.Entities.UsuarioEntity;
import biblioteca.Spring.Boot.Service.SocioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socio")
public class SocioController {

    private final SocioService service;

    public SocioController(SocioService service) {
        this.service = service;
    }

    @GetMapping("/listar")
    public List<SocioDTO> listarSocio(){
        return service.listarTodos();
    }

    @PostMapping("/socio")
    public SocioDTO crearSocio(@Valid @RequestBody SocioDTO dto) {
        return service.crearSocio(dto);
    }

    @PutMapping("/actualizar/{idSocio}")
    public ResponseEntity<SocioDTO> actualizarSocio(@PathVariable Integer idSocio, @Valid @RequestBody SocioDTO dto) {
        return ResponseEntity.of(service.actualizarSocio(idSocio, dto));
    }

    @DeleteMapping("/borrar/{idSocio}")
    public void borrarSocio(@PathVariable Integer idSocio) {
        service.eliminarSocio(idSocio);
    }

    @GetMapping("/usuarios-pendientes")
    public List<UsuarioDTO> listaUsuariosPendientes() {
        return service.listaUsuariosPendientes();
    }

    @PatchMapping("/activacion/{idUsuario}")
    public SocioDTO activarSocio(@PathVariable Long idUsuario, @RequestBody SocioDTO dto) {
        return service.activarSocio(idUsuario, dto);
    }
}