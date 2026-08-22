package biblioteca.Spring.Boot.Controllers;

import biblioteca.Spring.Boot.DTO.LibroDTO;
import biblioteca.Spring.Boot.Service.LibroService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping
public class LibroController {

    private final LibroService service;

    public LibroController(LibroService service) {
        this.service = service;
    }

    @GetMapping("/libro")
    public List<LibroDTO> listarTodos(){
        return service.listarLibros();
    }


    @GetMapping("/libro/{isbn}")
    public ResponseEntity<LibroDTO> buscarLibro(@PathVariable String isbn) {
        return ResponseEntity.of(service.buscarLibro(isbn));
    }

    @PostMapping("/libro")
    public ResponseEntity<LibroDTO> crearLibro(@Valid @RequestBody LibroDTO libroDTO){
        return ResponseEntity.of(service.crearLibro(libroDTO));
    }

    @PutMapping("/libro/{isbn}")
    public ResponseEntity<LibroDTO> actualizarLibro(@Valid @RequestBody LibroDTO dto,@PathVariable String isbn){
        return ResponseEntity.of(service.actualizarLibro(isbn, dto));
    }

    @DeleteMapping("/libro/{isbn}")
    public void borrarLibro(@PathVariable String isbn){
        service.eliminarLibro(isbn);
    }
}
