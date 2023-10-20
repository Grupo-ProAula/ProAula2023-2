
package co.edu.unicolombo.ProyectoDeAula20232.Models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name="Encargados", catalog = "ProAula_BD")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
public class Encargados extends Usuarios implements Serializable{
    
    private static final long serialVersionUID = 1L;
        
    @Column(name = "cargo", nullable = false)
    @NotEmpty
    private String cargo;

}
