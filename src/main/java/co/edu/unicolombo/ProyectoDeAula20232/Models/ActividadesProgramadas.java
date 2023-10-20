
package co.edu.unicolombo.ProyectoDeAula20232.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import javax.persistence.Temporal;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Table(name = "ActividadesProgramadas", catalog = "ProAula_BD")
@Entity
@Data
public class ActividadesProgramadas implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idActividadProgramada;
    
    @Column(name="periodo", nullable =false, length = 20)
    private String periodo;
    
    @Column(name="fechaInicio", nullable = false, columnDefinition = "Date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date fechaInicio;
    
    @Column(name="fechaFin", nullable = false, columnDefinition = "Date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date fechaFin;
    
    @Column(name="estado", nullable = false, length = 15)
    private String estado;
    
    @ManyToOne
    @JoinColumn(name = "idActividad", nullable = false)
    private Actividades actividad;
}
