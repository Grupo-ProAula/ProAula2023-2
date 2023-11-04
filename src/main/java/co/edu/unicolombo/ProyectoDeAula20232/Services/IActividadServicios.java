
package co.edu.unicolombo.ProyectoDeAula20232.Services;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Actividades;
import java.util.List;

public interface IActividadServicios {
    
    public List<Actividades> listarActividades(String palabra);
    
    public void guardarActividad(Actividades activity);
    
    public void eliminarActividad(Actividades activity);
    
    public Actividades buscarActividad(int idActividad);
}
