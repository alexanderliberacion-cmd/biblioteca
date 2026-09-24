package biblioteca.Spring.Boot.Mappers;

import biblioteca.Spring.Boot.DTO.SocioDTO;
import biblioteca.Spring.Boot.Entities.SocioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SocioMapper {

   SocioDTO socioADto(SocioEntity socio);
   @Mapping(target = "idSocio", ignore = true)
    SocioEntity dtoASocio(SocioDTO socioDTO);
}