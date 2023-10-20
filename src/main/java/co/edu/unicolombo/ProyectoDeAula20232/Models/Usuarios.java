
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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Usuarios", catalog = "ProAula_BD")
@Data
public class Usuarios implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "idUsuario")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotEmpty
    private int idUsuario;
    
    @Column(name = "cedula", unique = true, nullable = false)
    @NotEmpty
    private String cedula;
    
    @Column(name = "nombre", nullable = false)
    @NotEmpty
    private String nombre;
    
    @Column(name = "apellidos", nullable = false)
    @NotEmpty
    private String apellidos;
    
    @Column(name = "correo", nullable = false)
    @Email
    @NotEmpty
    private String correo;
    
    @Column(name = "telefono", nullable = false)
    @NotEmpty
    private String telefono;
    
    @Column(name = "password", nullable = false)
    @NotEmpty
    private String password;
    
    @Column(name = "estado", nullable = false)
    @NotEmpty
    private String estado;
    
    @Column(name = "tipo", nullable = false)
    @NotEmpty
    private String tipo;
}
