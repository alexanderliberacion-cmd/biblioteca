package biblioteca.Spring.Boot.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name = "Usuario")
public class UsuarioEntity {

    @Column(name = "usuario_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private Rol rol;

    @JoinColumn(name = "socio_id")
    @OneToOne(cascade = CascadeType.PERSIST)
    private SocioEntity socio;
}
