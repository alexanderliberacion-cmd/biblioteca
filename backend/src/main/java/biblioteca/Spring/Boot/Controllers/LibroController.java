package biblioteca.Spring.Boot.Controllers;

import biblioteca.Spring.Boot.DTO.LibroDTO;
import biblioteca.Spring.Boot.Service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/libro")
public class LibroController {

    private final LibroService service;

    public LibroController(LibroService service) {
        this.service = service;
    }

    @GetMapping("/listar")
    public List<LibroDTO> listarTodos(){
        return service.listarLibros();
    }


    @PostMapping("/libro")
    public ResponseEntity<LibroDTO> crearLibro(@Valid @RequestBody LibroDTO libroDTO){
        return ResponseEntity.of(service.crearLibro(libroDTO));
    }

    @PutMapping("/actualizar/{isbn}")
    public ResponseEntity<LibroDTO> actualizarLibro(@Valid @RequestBody LibroDTO dto,@PathVariable String isbn){
        return ResponseEntity.of(service.actualizarLibro(isbn, dto));
    }

    @DeleteMapping("/borrar/{isbn}")
    public void borrarLibro(@PathVariable String isbn){
        service.eliminarLibro(isbn);
    }
}
