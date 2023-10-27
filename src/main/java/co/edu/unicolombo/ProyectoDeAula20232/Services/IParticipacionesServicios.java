
package co.edu.unicolombo.ProyectoDeAula20232.Services;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Participaciones;
import java.util.List;

public interface IParticipacionesServicios {
    
    public List<Participaciones> listarParticipaciones(Integer idEstudiante, Integer idActividad, String palabra);
    
    public void guardarParticipacion(Participaciones participacion);
    
    public void eliminarParticipacion(Participaciones participacion);
    
    public Participaciones buscarParticipacion(Integer id);
    
    public Participaciones verificarParticipacion(Integer idEstudiante, Integer idActividad);
}
