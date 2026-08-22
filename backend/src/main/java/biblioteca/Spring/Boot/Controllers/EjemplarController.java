package biblioteca.Spring.Boot.Controllers;

import biblioteca.Spring.Boot.DTO.EjemplarDTO;
import biblioteca.Spring.Boot.Service.EjemplarService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class EjemplarController {

    private final EjemplarService service;

    public EjemplarController(EjemplarService service) {
        this.service = service;
    }

    @GetMapping("/ejemplar")
    public List<EjemplarDTO> listarEjemplar() {
        return service.listarTodos();
    }

    @GetMapping("/ejemplar/{idEjemplar}")
    public ResponseEntity<EjemplarDTO> buscarEjemplar(@PathVariable Integer idEjemplar) {
        return ResponseEntity.of(service.buscarEjemplar(idEjemplar));
}

    @PostMapping("/ejemplar")
    public ResponseEntity<EjemplarDTO> crearEjemplar(@Valid @RequestBody EjemplarDTO dto) {
        return ResponseEntity.of(service.crearEjemplar(dto));
    }

    @PutMapping("ejemplar/{idEjemplar}")
    public ResponseEntity<EjemplarDTO> actualizarEjemplar(@PathVariable Integer idEjemplar, @Valid @RequestBody EjemplarDTO dto) {
        return ResponseEntity.of(service.actualizarEjemplar(idEjemplar, dto));
    }

    @DeleteMapping("/ejemplar/{idEjemplar}")
    public void eliminarEjemplar(@PathVariable Integer idEjemplar) {
        service.eliminarEjemplar(idEjemplar);
    }

}
