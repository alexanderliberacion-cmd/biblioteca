package biblioteca.Spring.Boot.Repositories;

import biblioteca.Spring.Boot.DTO.LibroDTO;
import biblioteca.Spring.Boot.Entities.PrestamoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrestamoRepository extends JpaRepository<PrestamoEntity, Integer> {
    @Query("SELECT p FROM PrestamoEntity p WHERE p.ejemplarId.idEjemplar = :idEjemplar AND p.fechaDevolucion IS NULL")
    Optional<PrestamoEntity> buscarPrestamoActivoPorEjemplar(@Param("idEjemplar") Integer idEjemplar);
    boolean existsBySocioIdIdSocio(Integer socioId);
    @Query("SELECT p FROM PrestamoEntity p where p.socioId.idSocio = :idSocio and p.fechaDevolucion is null")
    List<PrestamoEntity> buscarPrestamosActivosPorSocio(@Param("idSocio") Integer idSocio);

    Integer id(Integer id);
}