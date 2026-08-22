package biblioteca.Spring.Boot.mappers;

import biblioteca.Spring.Boot.DTO.EjemplarDTO;
import biblioteca.Spring.Boot.Entities.EjemplarEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EjemplarMapper {
    @Mapping(target = "isbn", source = "isbn.isbn")
    EjemplarDTO ejemplarADto(EjemplarEntity ejemplar);
    @Mapping(target = "isbn", ignore = true)
    EjemplarEntity dtoAEjemplar (EjemplarDTO ejemplarDTO);
}