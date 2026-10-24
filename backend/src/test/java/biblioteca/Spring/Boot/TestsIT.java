package biblioteca.Spring.Boot;

import biblioteca.Spring.Boot.Entities.EjemplarEntity;
import biblioteca.Spring.Boot.Entities.Rol;
import biblioteca.Spring.Boot.Entities.SocioEntity;
import biblioteca.Spring.Boot.Entities.UsuarioEntity;
import biblioteca.Spring.Boot.Repositories.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BibliotecaSpringBootApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class TestsIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EjemplarRepository ejemplarRepository;


    @Autowired
    private SocioRepository  socioRepository;

    @Autowired
    private EntityManager entityManager;


    @Autowired
    private UsuarioRepository usuarioRepository;



    @Test
    @Transactional
    @WithMockUser(username = "socio", roles = "SOCIO")
    void socioRealizaPrestamo() throws Exception {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        var usuario = new UsuarioEntity();
        usuario.setEmail("socio@gmail.com");
        usuario.setPassword("fgfghfghkfghk");
        usuario.setRol(Rol.SOCIO);
        usuarioRepository.saveAndFlush(usuario);

        var socio = new SocioEntity();
        var ejemplar = new EjemplarEntity();
        ejemplar.setEstado("DISPONIBLE");

        var socioGuardado = socioRepository.saveAndFlush(socio);
        var ejemplarGuardado = ejemplarRepository.saveAndFlush(ejemplar);

        mockMvc.perform(post("/prestamo/prestamo")
                        .with(user("socio").roles("SOCIO"))
                .param("idSocio", socioGuardado.getIdSocio().toString())
                .param("idEjemplar", ejemplarGuardado.getIdEjemplar().toString()))
                        .andExpect(status().isOk());

        entityManager.flush();
        entityManager.clear();

        EjemplarEntity ejemplarModificado = ejemplarRepository
                .findById(ejemplarGuardado.getIdEjemplar())
                .orElseThrow();

        assertThat(ejemplarModificado.getEstado()).isEqualTo("PRESTADO");
    }


}
