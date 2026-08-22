package biblioteca.Spring.Boot.mappers;

import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.Entities.SocioEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SocioMapper {
   SocioDTO socioADto(SocioEntity socio);
    SocioEntity dtoASocio(SocioDTO socioDTO);
}