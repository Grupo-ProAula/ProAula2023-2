
package co.edu.unicolombo.ProyectoDeAula20232.Services;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Horarios;
import java.util.List;

public interface IHorarioServicios {
    
    public List<Horarios> listarHorarios(Integer idActividad);
    
    public void guardarHorario(Horarios horario);
    
    public void eliminarHorario(Horarios horario);
    
    public Horarios buscarHorario(Integer id);
    
    public Horarios buscarHorarioDatos(String dia, Integer idActividad);
}
