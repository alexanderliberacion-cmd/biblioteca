package biblioteca.Spring.Boot.Service;

import biblioteca.Spring.Boot.DTO.PrestamoDTO;
import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.Entities.EjemplarEntity;
import biblioteca.Spring.Boot.Entities.PrestamoEntity;
import biblioteca.Spring.Boot.Entities.SocioEntity;
import biblioteca.Spring.Boot.Exceptions.OperacionNoPermitidaException;
import biblioteca.Spring.Boot.Exceptions.RecursoNoEncontradoException;
import biblioteca.Spring.Boot.Repositories.EjemplarRepository;
import biblioteca.Spring.Boot.Repositories.PrestamoRepository;
import biblioteca.Spring.Boot.Repositories.SocioRepository;
import biblioteca.Spring.Boot.mappers.PrestamoMapper;
import biblioteca.Spring.Boot.mappers.SocioMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final SocioRepository socioRepository;
    private final EjemplarRepository ejemplarRepository;
    private final PrestamoMapper prestamoMapper;
    private final SocioMapper socioMapper;

    public PrestamoService(PrestamoRepository prestamoRepository, SocioRepository socioRepository, EjemplarRepository ejemplarRepository, PrestamoMapper prestamoMapper, SocioMapper socioMapper) {
        this.prestamoRepository = prestamoRepository;
        this.socioRepository = socioRepository;
        this.ejemplarRepository = ejemplarRepository;
        this.prestamoMapper = prestamoMapper;
        this.socioMapper = socioMapper;
    }

    //crear prestamo
    public Optional<PrestamoDTO> crearPrestamo(Integer idSocio, Integer idEjemplar) {
        Optional<SocioEntity> socioEncontrado = socioRepository.findById(idSocio);
        Optional<EjemplarEntity> ejemplarEncontrado = ejemplarRepository.findById(idEjemplar);
        if(!socioEncontrado.isPresent() || !ejemplarEncontrado.isPresent() || !ejemplarEncontrado.get().getEstado().equals("DISPONIBLE")){
            return Optional.empty();
        }

        EjemplarEntity ejemplar = ejemplarEncontrado.get();
        ejemplar.setEstado("PRESTADO");
        ejemplarRepository.save(ejemplar);
        PrestamoEntity prestamo = new PrestamoEntity();
        prestamo.setFechaInicio(LocalDate.now());
        prestamo.setFechaLimite(LocalDate.now().plusDays(15));
        prestamo.setSocioId(socioEncontrado.get());
        prestamo.setEjemplarId(ejemplarEncontrado.get());
        PrestamoEntity guardado = prestamoRepository.save(prestamo);
        return Optional.of(prestamoMapper.prestamoADto(guardado));
    }

    //buscar prestamo
    public Optional<PrestamoDTO> buscarPrestamo(Integer id) {
        return prestamoRepository.findById(id)
                .map(prestamoMapper::prestamoADto);
    }

    //Listar prestamos
    public List<PrestamoDTO> listarPrestamos() {
        return prestamoRepository.findAll()
                .stream()
                .map(prestamoMapper::prestamoADto)
                .toList();
    }


    //Devolver prestamo
    public Optional<PrestamoDTO> devolerPrestamo(Integer idEjemplar) {
        Optional<PrestamoEntity> prestamo = prestamoRepository.buscarPrestamoActivoPorEjemplar(idEjemplar);
        if(!prestamo.isPresent()) {
            return Optional.empty();
        }
        PrestamoEntity prestamoEncontrado = prestamo.get();
        prestamoEncontrado.setFechaDevolucion(LocalDate.now());
        prestamoEncontrado.getEjemplarId().setEstado("DISPONIBLE");
        PrestamoEntity guardado = prestamoRepository.save(prestamoEncontrado);
        return Optional.of(prestamoMapper.prestamoADto(guardado));
    }

    //Actualizar prestamo
    public Optional<PrestamoDTO> actualizarPrestamo(Integer id) {
        PrestamoEntity prestamoEncontrado = prestamoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Prestamo no encontrado"));
        if(prestamoEncontrado.estaDevuelto()) {
            return Optional.empty();
        }
        prestamoEncontrado.setFechaLimite(prestamoEncontrado.getFechaLimite().plusDays(15));
        PrestamoEntity prestamoActualizado = prestamoRepository.save(prestamoEncontrado);
        return Optional.of(prestamoMapper.prestamoADto(prestamoActualizado));
    }

    //Borrar un prestamo
    public void borrarPrestamo(Integer id) {
        PrestamoEntity prestamoEncontrado = prestamoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Prestamo no encontrado"));
        if(prestamoEncontrado.estaDevuelto()) {
            prestamoRepository.deleteById(id);
        } else {
           throw  new OperacionNoPermitidaException("No se puede borrar un prestamo activo");
        }
    }

    //Socios con prestamos mas atrasados
    public List<SocioDTO> sociosConPrestamosAtrasados() {
        List<PrestamoEntity> prestamos = prestamoRepository.findAll();
       return prestamos.stream()
               .filter(PrestamoEntity::estaAtrasado)
               .map(PrestamoEntity::getSocioId)
               .distinct()
               .map(socioMapper::socioADto)
               .toList();
    }

    //5 libros mas prestados
    public List<String> librosMasPrestados() {
        List<PrestamoEntity> prestamos = prestamoRepository.findAll();
        Map<String, Long> conteoPorIsbn = prestamos.stream()
                .collect(Collectors.groupingBy(prestamo -> prestamo.getEjemplarId().getIsbn().getIsbn() ,Collectors.counting()));

        return conteoPorIsbn.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();
    }

    //Meses con mas prestamos
    public Map<String, Long> mesesConMasPrestamos() {
        List<PrestamoEntity> prestamos = prestamoRepository.findAll();
        return prestamos.stream()
                .collect(Collectors.groupingBy(prestamo -> prestamo.getFechaInicio().getYear() + "-" + prestamo.getFechaInicio().getMonth().getValue(), Collectors.counting()));
    }

    //Prestamos activos por socio
    public List<PrestamoDTO> listarPrestamosActivosPorSocio(Integer idSocio) {
        return prestamoRepository.buscarPrestamosActivosPorSocio(idSocio)
                .stream()
                .map(prestamoMapper::prestamoADto)
                .toList();
    }
}