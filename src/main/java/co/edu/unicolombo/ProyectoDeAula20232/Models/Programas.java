package co.edu.unicolombo.ProyectoDeAula20232.Models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Table(name = "Programas", catalog = "ProAula_BD")
@Entity
@Data
public class Programas implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "idPrograma")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPrograma;
        
    @Column(name = "codigoPrograma", nullable = false, length = 15, unique = true)
    private String codigoPrograma;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "semestresTotales", nullable = false)
    private int semestresTotales;
        
    @Column(name = "estado", nullable = false, length = 15)
    private String estado;
}
