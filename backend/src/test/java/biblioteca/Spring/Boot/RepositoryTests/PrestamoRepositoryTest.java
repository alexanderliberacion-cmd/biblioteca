package biblioteca.Spring.Boot.RepositoryTests;

import biblioteca.Spring.Boot.Entities.EjemplarEntity;
import biblioteca.Spring.Boot.Entities.PrestamoEntity;
import biblioteca.Spring.Boot.Entities.SocioEntity;
import biblioteca.Spring.Boot.Repositories.EjemplarRepository;
import biblioteca.Spring.Boot.Repositories.PrestamoRepository;
import biblioteca.Spring.Boot.Repositories.SocioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PrestamoRepositoryTest {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EjemplarRepository ejemplarRepository;

    @Autowired
    private SocioRepository socioRepository;

    @Test
    void buscarPrestamoActivoPorEjemplar() throws Exception{
        var ejemplar = new EjemplarEntity();
        var guardado = ejemplarRepository.save(ejemplar);

        var prestamo = new PrestamoEntity();
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.now().plusDays(16);

        prestamo.setFechaInicio(fechaInicio);
        prestamo.setFechaLimite(fechaLimite);
        prestamo.setEjemplarId(guardado);

        entityManager.persistAndFlush(prestamo);

        Optional<PrestamoEntity> result = prestamoRepository.findById(prestamo.getEjemplarId().getIdEjemplar());
        assertTrue(result.isPresent());
        assertEquals(fechaInicio, result.get().getFechaInicio());
        assertEquals(fechaLimite, result.get().getFechaLimite());
    }


    @Test
    void buscarPrestamoActivoPorEjemplarFallido() throws Exception{
        var ejemplar = new EjemplarEntity();
        var guardado = ejemplarRepository.save(ejemplar);

        var prestamo = new  PrestamoEntity();
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.now().plusDays(16);

        prestamo.setFechaInicio(fechaInicio);
        prestamo.setFechaLimite(fechaLimite);
        prestamo.setEjemplarId(guardado);

        entityManager.persistAndFlush(prestamo);

        Optional<PrestamoEntity> result = prestamoRepository.findById(prestamo.getEjemplarId().getIdEjemplar());
        assertFalse(result.isEmpty());
    }

    @Test
    void buscarPrestamoActivoPorSocio() throws Exception {
        var socio = new SocioEntity();
        var guardado = socioRepository.save(socio);

        var prestamo = new PrestamoEntity();
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.now().plusDays(16);
        prestamo.setFechaInicio(fechaInicio);
        prestamo.setFechaLimite(fechaLimite);
        prestamo.setSocioId(guardado);
        entityManager.persistAndFlush(prestamo);

        List<PrestamoEntity> result = prestamoRepository.buscarPrestamosActivosPorSocio(prestamo.getSocioId().getIdSocio());
        assertFalse(result.isEmpty());
    }

    @Test
    void buscarPrestamoActivoPorSocioFallido() throws Exception {
        var socio = new SocioEntity();
        var guardado = socioRepository.save(socio);

        var prestamo = new PrestamoEntity();
        LocalDate fechaInicio = LocalDate.now();
        LocalDate fechaLimite = LocalDate.now().plusDays(16);
        prestamo.setFechaInicio(fechaInicio);
        prestamo.setFechaLimite(fechaLimite);
        prestamo.setSocioId(guardado);
        entityManager.persistAndFlush(prestamo);

        var socio2 = new SocioEntity();
        var guardado2 = socioRepository.save(socio2);

        List<PrestamoEntity> result = prestamoRepository.buscarPrestamosActivosPorSocio(guardado2.getIdSocio());
        assertTrue(result.isEmpty());
    }
}
