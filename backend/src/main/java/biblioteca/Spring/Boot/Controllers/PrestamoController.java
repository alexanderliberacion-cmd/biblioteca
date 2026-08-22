package biblioteca.Spring.Boot.Controllers;

import biblioteca.Spring.Boot.DTO.PrestamoDTO;
import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.Service.PrestamoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class PrestamoController {
    private final PrestamoService service;

    public PrestamoController(PrestamoService service) {
        this.service = service;
    }

    @GetMapping("/prestamo")
    public List<PrestamoDTO> listarPrestamos() {
        return service.listarPrestamos();
    }


    @GetMapping("/prestamo/{id}")
    public ResponseEntity<PrestamoDTO> buscarPrestamo(@PathVariable Integer id) {
        return ResponseEntity.of(service.buscarPrestamo(id));
    }

    @GetMapping("/prestamo/socios-atrasados")
    public List<SocioDTO> sociosAtrasados() {
        return service.sociosConPrestamosAtrasados();
    }

    @GetMapping("/prestamo/libros-mas-prestados")
    public List<String> librosMasPrestados() {
        return service.librosMasPrestados();
    }

    @GetMapping("/prestamo/meses-mas-prestamos")
    public Map<String, Long> mesesConMasPrestamos() {
        return service.mesesConMasPrestamos();
    }

    @GetMapping("/prestamo/prestamos-activos-socio")
    public List<PrestamoDTO> prestamosActivosSocio(@RequestParam Integer idSocio) {
        return service.listarPrestamosActivosPorSocio(idSocio);
    }

    @PostMapping("/prestamo")
    public ResponseEntity<PrestamoDTO> crearPrestamo(@RequestParam Integer idSocio, @RequestParam Integer idEjemplar) {
        return ResponseEntity.of(service.crearPrestamo(idSocio, idEjemplar));
    }

    @PutMapping("/prestamo/{id}")
    public ResponseEntity<PrestamoDTO> actualizarPrestamo(@PathVariable Integer id) {
        return ResponseEntity.of(service.actualizarPrestamo(id));
    }

    @PutMapping("/prestamo/devolver")
    public ResponseEntity<PrestamoDTO> devolverPrestamo (@RequestParam Integer idEjemplar) {
        return ResponseEntity.of(service.devolerPrestamo(idEjemplar));
    }

    @DeleteMapping("/prestamo/{id}")
    public void eliminarPrestamo(@PathVariable Integer id) {
        service.borrarPrestamo(id);
    }
}