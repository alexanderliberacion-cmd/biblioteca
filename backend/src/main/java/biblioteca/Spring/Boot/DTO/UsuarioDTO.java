package biblioteca.Spring.Boot.DTO;

import biblioteca.Spring.Boot.Entities.Rol;
import biblioteca.Spring.Boot.Entities.SocioEntity;

public record UsuarioDTO(Long id, String email, String password, Rol rol, SocioEntity socio) {
}
