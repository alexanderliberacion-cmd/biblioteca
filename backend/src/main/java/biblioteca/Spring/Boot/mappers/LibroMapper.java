package biblioteca.Spring.Boot.mappers;

import biblioteca.Spring.Boot.DTO.LibroDTO;
import biblioteca.Spring.Boot.Entities.LibroEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public  interface LibroMapper {
    LibroDTO libroADto(LibroEntity libro);

    LibroEntity dtoALibro(LibroDTO libroDTO);
}