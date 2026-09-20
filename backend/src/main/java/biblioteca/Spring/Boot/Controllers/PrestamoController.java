package biblioteca.Spring.Boot.Controllers;

import biblioteca.Spring.Boot.DTO.PrestamoDTO;
import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.Service.PrestamoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/prestamo")
public class PrestamoController {
    private final PrestamoService service;

    public PrestamoController(PrestamoService service) {
        this.service = service;
    }

    @GetMapping("/listar")
    public List<PrestamoDTO> listarPrestamos() {
        return service.listarPrestamos();
    }

    @GetMapping("/socios-atrasados")
    public List<SocioDTO> sociosAtrasados() {
        return service.sociosConPrestamosAtrasados();
    }

    @GetMapping("/libros-mas-prestados")
    public List<String> librosMasPrestados() {
        return service.librosMasPrestados();
    }

    @GetMapping("/meses-mas-prestamos")
    public Map<String, Long> mesesConMasPrestamos() {
        return service.mesesConMasPrestamos();
    }

    @GetMapping("/prestamos-activos-socio")
    public List<PrestamoDTO> prestamosActivosSocio(@RequestParam Integer idSocio) {
        return service.listarPrestamosActivosPorSocio(idSocio);
    }

    @PostMapping("/prestamo")
    public ResponseEntity<PrestamoDTO> crearPrestamo(@RequestParam Integer idSocio, @RequestParam Integer idEjemplar) {
        return ResponseEntity.of(service.crearPrestamo(idSocio, idEjemplar));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<PrestamoDTO> actualizarPrestamo(@PathVariable Integer id) {
        return ResponseEntity.of(service.actualizarPrestamo(id));
    }

    @PutMapping("/devolver/{idEjemplar}")
    public ResponseEntity<PrestamoDTO> devolverPrestamo (@PathVariable Integer idEjemplar) {
        return ResponseEntity.of(service.devolerPrestamo(idEjemplar));
    }

    @DeleteMapping("/borrar/{id}")
    public void eliminarPrestamo(@PathVariable Integer id) {
        service.borrarPrestamo(id);
    }
}