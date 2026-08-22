package biblioteca.Spring.Boot.Repositories;

import biblioteca.Spring.Boot.Entities.LibroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<LibroEntity, String> {
    String isbn(String isbn);
}
