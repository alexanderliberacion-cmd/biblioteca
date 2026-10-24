package biblioteca.Spring.Boot.RepositoryTests;

import biblioteca.Spring.Boot.Entities.UsuarioEntity;
import biblioteca.Spring.Boot.Repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void findByEmail() throws Exception {
        var usuario = new UsuarioEntity();
        usuario.setEmail("alexanderliberacion@gmail.com");
        usuario.setPassword("123");

        entityManager.persistAndFlush(usuario);
        Optional<UsuarioEntity> result = usuarioRepository.findByEmail(usuario.getEmail());
        assertTrue(result.isPresent());
        assertEquals("alexanderliberacion@gmail.com", result.get().getEmail());
        assertEquals("123", result.get().getPassword());
    }

    @Test
    void findByEmailNotFound() throws Exception {
        var usuario = new UsuarioEntity();
        usuario.setEmail("manuelrubio@hotmail.com");
        usuario.setPassword("123");
        entityManager.persistAndFlush(usuario);

        Optional<UsuarioEntity> result = usuarioRepository.findByEmail(usuario.getEmail());
        assertFalse(result.isEmpty());
    }


}
