package biblioteca.Spring.Boot.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name="ejemplar")
public class EjemplarEntity {

    @Column(name = "ejemplar_id")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer idEjemplar;

    @Column(name="estado")
    private String estado;

    @JoinColumn(name="isbn")
    @ManyToOne(fetch=FetchType.LAZY) //Clave foranea unida a libro
    private LibroEntity isbn;
}