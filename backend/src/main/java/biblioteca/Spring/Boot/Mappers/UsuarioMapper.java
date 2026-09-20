package biblioteca.Spring.Boot.Mappers;

import biblioteca.Spring.Boot.DTO.UsuarioDTO;
import biblioteca.Spring.Boot.Entities.UsuarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target="id", source="id")
    UsuarioDTO usuarioADTO(UsuarioEntity usuarioEntity);

    @Mapping(target="id", ignore = true)
    UsuarioEntity dtoAPusuario(UsuarioDTO usuarioDTO);
}
