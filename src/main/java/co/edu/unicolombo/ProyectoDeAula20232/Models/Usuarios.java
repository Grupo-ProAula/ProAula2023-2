
package co.edu.unicolombo.ProyectoDeAula20232.Models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Usuarios", catalog = "ProAula_BD")
public class Usuarios implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "idUsuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotEmpty
    private int idUsuario;
    
    @Column(name = "cedula", unique = true)
    @NotEmpty
    private String cedula;
    
    @Column(name = "nombre")
    @NotEmpty
    private String nombre;
    
    @Column(name = "apellidos")
    @NotEmpty
    private String apellidos;
    
    @Column(name = "correo")
    @Email
    @NotEmpty
    private String correo;
    
    @Column(name = "telefono")
    @NotEmpty
    private String telefono;
    
    @Column(name = "password")
    @NotEmpty
    private String password;
    
    @Column(name = "estado")
    @NotEmpty
    private String estado;
    
    @Column(name = "tipo")
    @NotEmpty
    private String tipo;
}
