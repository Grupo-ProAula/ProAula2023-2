
package co.edu.unicolombo.ProyectoDeAula20232.Models;

import java.io.Serializable;
import java.sql.Time;
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
@Table(name = "Asistencias", catalog = "ProAula_BD")
public class Asistencias implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAsistencia;
    
    @Column(name="fechaAsistencia", nullable = false, columnDefinition = "Date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date fechaAsistencia;
    
    @Column(name="horasAsistidas", nullable = false, length = 15)
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private Time horasAsistidas;
    
    @Column(name="estado", nullable = false, length = 15)
    private String estado;
    
    @JoinColumn(name = "idParticipacion", nullable = false)
    @ManyToOne
    private Participaciones participacion;
}
