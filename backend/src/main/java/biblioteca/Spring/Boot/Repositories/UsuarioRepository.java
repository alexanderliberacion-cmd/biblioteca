package biblioteca.Spring.Boot.Repositories;

import biblioteca.Spring.Boot.Entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);

    List<UsuarioEntity> email(String email);

    Long id(Long id);
}
