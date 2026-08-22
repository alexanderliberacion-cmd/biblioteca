package biblioteca.Spring.Boot.mappers;

import biblioteca.Spring.Boot.DTO.PrestamoDTO;
import biblioteca.Spring.Boot.Entities.PrestamoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrestamoMapper {
    //Mapean explicitamente tanto el nombre como el id
    @Mapping(target="idSocio", source="socioId.idSocio")
    @Mapping(target="idEjemplar", source="ejemplarId.idEjemplar")
    PrestamoDTO prestamoADto(PrestamoEntity prestamo);

    @Mapping(target = "socioId", ignore = true)
    @Mapping(target = "ejemplarId", ignore = true)
    @Mapping(target = "id", ignore = true)
    PrestamoEntity dtoAPrestamo(PrestamoDTO prestamoDTO);
}