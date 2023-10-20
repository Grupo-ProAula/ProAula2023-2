
package co.edu.unicolombo.ProyectoDeAula20232.Models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@Table(name = "Participantes", catalog = "ProAula_BD")
public class Participaciones implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idParticipante;  
    
    @Column(name="fechaInscripcion", nullable = false, columnDefinition = "Date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date fechaInscripcion;
    
    @Column(name="estado", nullable = false, length = 15)
    private String estado;
    
    @ManyToOne
    @JoinColumn(name="idEstudiante", nullable = false)
    private Estudiantes estudiante;
    
    @ManyToOne
    @JoinColumn(name="idActividadProgramada", nullable = false)
    private ActividadesProgramadas actividadProgramada;
}
