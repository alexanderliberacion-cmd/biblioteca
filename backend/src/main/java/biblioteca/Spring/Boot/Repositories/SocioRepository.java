package biblioteca.Spring.Boot.Repositories;

import biblioteca.Spring.Boot.Entities.SocioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocioRepository  extends JpaRepository<SocioEntity, Integer> {
}