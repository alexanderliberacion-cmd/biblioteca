package biblioteca.Spring.Boot.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name = "socio")
public class SocioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSocio;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;
}