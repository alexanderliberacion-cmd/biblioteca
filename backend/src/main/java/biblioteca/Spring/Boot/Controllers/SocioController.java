package biblioteca.Spring.Boot.Controllers;

import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.Service.SocioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class SocioController {

    private final SocioService service;

    public SocioController(SocioService service) {
        this.service = service;
    }

    @GetMapping("/socio")
    public List<SocioDTO> listarSocio(){
        return service.listarTodos();
    }

    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<SocioDTO> buscarSocio(@PathVariable Integer idSocio){
        return ResponseEntity.of(service.buscarSocio(idSocio));
    }

    @PostMapping("/socio")
    public SocioDTO crearSocio(@Valid @RequestBody SocioDTO dto) {
        return service.crearSocio(dto);
    }

    @PutMapping("/socio/{idSocio}")
    public ResponseEntity<SocioDTO> actualizarSocio(@PathVariable Integer idSocio, @Valid @RequestBody SocioDTO dto) {
        return ResponseEntity.of(service.actualizarSocio(idSocio, dto));
    }

    @DeleteMapping("/socio/{idSocio}")
    public void borrarSocio(@PathVariable Integer idSocio) {
        service.eliminarSocio(idSocio);
    }
}