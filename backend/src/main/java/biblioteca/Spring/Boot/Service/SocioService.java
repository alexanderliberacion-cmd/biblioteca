package biblioteca.Spring.Boot.Service;

import biblioteca.Spring.Boot.DTO.RegisterRequestDTO;
import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.DTO.UsuarioDTO;
import biblioteca.Spring.Boot.Entities.Rol;
import biblioteca.Spring.Boot.Entities.SocioEntity;
import biblioteca.Spring.Boot.Entities.UsuarioEntity;
import biblioteca.Spring.Boot.Exceptions.OperacionNoPermitidaException;
import biblioteca.Spring.Boot.Exceptions.RecursoNoEncontradoException;
import biblioteca.Spring.Boot.Mappers.UsuarioMapper;
import biblioteca.Spring.Boot.Repositories.PrestamoRepository;
import biblioteca.Spring.Boot.Repositories.SocioRepository;
import biblioteca.Spring.Boot.Mappers.SocioMapper;
import biblioteca.Spring.Boot.Repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SocioService {
    private final SocioRepository socioRepository;
    private final SocioMapper socioMapper;
    private final UsuarioMapper usuarioMapper;
    private final PrestamoRepository prestamoRepository;
    private final UsuarioRepository usuarioRepository;

    public SocioService(SocioRepository socioRepository, SocioMapper socioMapper, UsuarioMapper usuarioMapper, PrestamoRepository prestamoRepository, UsuarioRepository usuarioRepository) {
        this.socioRepository = socioRepository;
        this.socioMapper = socioMapper;
        this.usuarioMapper = usuarioMapper;
        this.prestamoRepository = prestamoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //Crear socio
    public SocioDTO crearSocio(SocioDTO socioDTO) {
        SocioEntity socioEntity = socioMapper.dtoASocio(socioDTO);
        socioEntity = socioRepository.save(socioEntity);
        return socioMapper.socioADto(socioEntity);
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
        verificarPropietario(idSocio);
        SocioEntity socioEncontrado = socioRepository.findById(idSocio).orElseThrow(() -> new RecursoNoEncontradoException("Socio no encontrado"));
        socioEncontrado.setNombre(socioNuevo.nombre());
        socioEncontrado.setEmail(socioNuevo.email());
        SocioEntity socioActualizado = socioRepository.save(socioEncontrado);
        return Optional.of(socioMapper.socioADto(socioActualizado));
    }

    //verificar el socio para security
    public void verificarPropietario(Integer idSocio) {
        UserDetails user =  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = user.getUsername();
        UsuarioEntity usuarioAutenticado = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new OperacionNoPermitidaException("Usuario no encontrado"));
        if(usuarioAutenticado.getRol() == Rol.ADMIN) {
            return;
        }
        if(!usuarioAutenticado.getSocio().getIdSocio().equals(idSocio)) {
            throw new OperacionNoPermitidaException("no puedes modificar este socio");
        }
    }

    //Borrar socio
    public void eliminarSocio(Integer idSocio) {
        if(!prestamoRepository.existsBySocioIdIdSocio(idSocio)) {
            socioRepository.deleteById(idSocio);
        }
    }

    //Usuarios pendientes de autorizar
    public List<UsuarioDTO> listaUsuariosPendientes() {
        List<UsuarioEntity> usuarioFiltrado = usuarioRepository.findAll().stream()
                .filter(user -> user.getRol() == Rol.BASIC)
                .toList();
        return usuarioFiltrado.stream()
                .map(usuarioMapper::usuarioADTO)
                .collect(Collectors.toList());

    }

    @Transactional
    //Activacion de socio
    public SocioDTO activarSocio(Long idUsuario ,SocioDTO socio) {
        //Busca el usuario
        UsuarioEntity usuarioEncontrado = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado al usuario"));
        //Chequea si tiene el rol basic, si no continua con la activacion.
        //Se mapea a dto la entidad, se guarda en el repositorio, se vincula el socio
        if(!usuarioEncontrado.getRol().equals(Rol.BASIC)) {
           throw new OperacionNoPermitidaException("No puedes modificar este socio");
        }
        SocioEntity socioEntity = socioMapper.dtoASocio(socio);
        SocioEntity socioGuardado = socioRepository.save(socioEntity);
        usuarioEncontrado.setSocio(socioGuardado);
        usuarioEncontrado.setRol(Rol.SOCIO);
        usuarioRepository.save(usuarioEncontrado);
        return socioMapper.socioADto(socioGuardado);
    }
}