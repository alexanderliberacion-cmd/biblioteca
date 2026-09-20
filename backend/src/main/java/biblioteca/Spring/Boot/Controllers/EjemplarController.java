package biblioteca.Spring.Boot.Controllers;

import biblioteca.Spring.Boot.DTO.EjemplarDTO;
import biblioteca.Spring.Boot.Service.EjemplarService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/ejemplar")
@RestController
public class EjemplarController {

    private final EjemplarService service;

    public EjemplarController(EjemplarService service) {
        this.service = service;
    }

    @GetMapping("/listar")
    public List<EjemplarDTO> listarEjemplar() {
        return service.listarTodos();
    }


    @PostMapping("/ejemplar")
    public ResponseEntity<EjemplarDTO> crearEjemplar(@Valid @RequestBody EjemplarDTO dto) {
        return ResponseEntity.of(service.crearEjemplar(dto));
    }

    @DeleteMapping("/borrar/{idEjemplar}")
    public void eliminarEjemplar(@PathVariable Integer idEjemplar) {
        service.eliminarEjemplar(idEjemplar);
    }

}
