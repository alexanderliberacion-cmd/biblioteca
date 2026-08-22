package biblioteca.Spring.Boot.Entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Data
@Getter
@Setter
@Table(name="prestamo")
public class PrestamoEntity {
    @Column
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name="fecha_limite")
    private LocalDate fechaLimite;

    @Column(name="fecha_devolucion")
    private LocalDate fechaDevolucion;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ejemplar_id")
    private EjemplarEntity ejemplarId;

    @JoinColumn(name="socio_id")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private SocioEntity socioId;

    //Esta atrasado
    public boolean estaAtrasado() {
        return (this.fechaDevolucion != null) ? this.fechaDevolucion.isAfter(this.fechaLimite) : LocalDate.now().isAfter(this.fechaLimite);
    }

    //Esta devuelto
    public boolean estaDevuelto() {
        return fechaDevolucion != null;
    }
}