
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
import javax.persistence.TemporalType;
import lombok.Data;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@Table(name = "Horarios", catalog = "ProAula_BD")
public class Horarios implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idHorario;
    
    @Column(name="dia", nullable = false)
    private String dia;
    
    @Column(name="horaInicio", nullable = false, length = 15, columnDefinition = "time")
    @DateTimeFormat(pattern = "HH:mm")
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIME)
    private Date horaInicio;
    
    @Column(name="horaFin", nullable = false, length = 15, columnDefinition = "time")
    @DateTimeFormat(pattern = "HH:mm")
    @javax.persistence.Temporal(javax.persistence.TemporalType.TIME)
    private Date horaFin;
    
    @Column(name="estado", nullable = false, length = 15)
    private String estado;
    
    @JoinColumn(name = "idActividadProgramada", nullable = false)
    @ManyToOne
    private ActividadesProgramadas actividad;
    
}
