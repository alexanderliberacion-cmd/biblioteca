package biblioteca.Spring.Boot.Repositories;

import biblioteca.Spring.Boot.Entities.EjemplarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EjemplarRepository extends JpaRepository<EjemplarEntity,Integer> {
    EjemplarEntity findByEstado(String estado);
    boolean existsByIsbnIsbn(String isbn);
}