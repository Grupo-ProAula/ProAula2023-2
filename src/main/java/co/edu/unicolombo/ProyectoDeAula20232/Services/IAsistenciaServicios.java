
package co.edu.unicolombo.ProyectoDeAula20232.Services;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Asistencias;
import java.sql.Date;
import java.util.List;

public interface IAsistenciaServicios {
    
    public List<Asistencias> listarAsistencias(Integer idEstudiante, Integer idActividad);
    
    public void guardarAsistencia(Asistencias asistencia);
    
    public void eliminarAsistencia(Asistencias asistencia);
    
    public Asistencias buscarAsistencia(Integer id);
    
    public Asistencias verficiarAsistencia(Integer idActividad, Integer idEstudiante, Date fecha);
}
