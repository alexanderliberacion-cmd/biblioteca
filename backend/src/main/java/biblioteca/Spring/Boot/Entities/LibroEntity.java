package biblioteca.Spring.Boot.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Data
@Getter
@Setter
@Table(name="libro")
public class LibroEntity {
    @Id
    private String isbn;

    @Column(name="titulo")
    private String titulo;

    @Column(name="autor")
    private String autor;
}