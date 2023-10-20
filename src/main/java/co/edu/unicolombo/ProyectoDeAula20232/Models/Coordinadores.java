
package co.edu.unicolombo.ProyectoDeAula20232.Models;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="Coordinadores", catalog = "ProAula_BD")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
@Data
public class Coordinadores extends Usuarios implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @OneToOne
    @JoinColumn(name = "idPrograma", nullable = false)
    @NotEmpty
    private Programas programa;
}
