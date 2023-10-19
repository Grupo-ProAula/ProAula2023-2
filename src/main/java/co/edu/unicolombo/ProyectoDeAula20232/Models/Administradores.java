/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
@Table(name="Administradores", catalog = "ProAula_BD")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "idUsuario", referencedColumnName = "idUsuario")
public class Administradores extends Usuarios implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Column(name="cargo", nullable = false)
    @NotEmpty
    private String cargo;
}
