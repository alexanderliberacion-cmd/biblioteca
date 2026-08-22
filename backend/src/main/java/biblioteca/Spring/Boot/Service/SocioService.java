package biblioteca.Spring.Boot.Service;

import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.Entities.SocioEntity;
import biblioteca.Spring.Boot.Exceptions.RecursoNoEncontradoException;
import biblioteca.Spring.Boot.Repositories.PrestamoRepository;
import biblioteca.Spring.Boot.Repositories.SocioRepository;
import biblioteca.Spring.Boot.mappers.SocioMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SocioService {
    private final SocioRepository socioRepository;
    private final SocioMapper socioMapper;
    private final PrestamoRepository prestamoRepository;

    public SocioService(SocioRepository socioRepository, SocioMapper socioMapper, PrestamoRepository prestamoRepository) {
        this.socioRepository = socioRepository;
        this.socioMapper = socioMapper;
        this.prestamoRepository = prestamoRepository;
    }

    //Crear socio
    public SocioDTO crearSocio(SocioDTO socioDTO) {
        SocioEntity socioEntity = socioMapper.dtoASocio(socioDTO);
        socioEntity = socioRepository.save(socioEntity);
        return socioMapper.socioADto(socioEntity);
    }

    //Buscar Socio
    public Optional<SocioDTO> buscarSocio(Integer idSocio) {
        return socioRepository.findById(idSocio)
                .map(socioMapper::socioADto);
    }

    //Listar todos los socios
    public List<SocioDTO> listarTodos() {
        return socioRepository.findAll()
                .stream()
                .map(socioMapper::socioADto)
                .toList();
    }


    //Actualizar socio
    public Optional<SocioDTO> actualizarSocio(Integer idSocio, SocioDTO socioNuevo) {
        SocioEntity socioEncontrado = socioRepository.findById(idSocio).orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado"));
        socioEncontrado.setNombre(socioNuevo.nombre());
        socioEncontrado.setEmail(socioNuevo.email());
        SocioEntity socioActualizado = socioRepository.save(socioEncontrado);
        return Optional.of(socioMapper.socioADto(socioActualizado));
    }

    //Borrar socio
    public void eliminarSocio(Integer idSocio) {
        if(!prestamoRepository.existsBySocioIdIdSocio(idSocio)) {
            socioRepository.deleteById(idSocio);
        }
    }
}