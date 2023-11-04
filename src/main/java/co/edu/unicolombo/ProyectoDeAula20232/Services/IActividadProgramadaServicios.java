
package co.edu.unicolombo.ProyectoDeAula20232.Services;

import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import java.util.List;

public interface IActividadProgramadaServicios {
    
    public List<ActividadesProgramadas> listarActividadesProgramadas(String palabra);
    
    public void guardarActividadProgramada(ActividadesProgramadas activity);
    
    public void eliminarActividadProgramada(ActividadesProgramadas activity);
    
    public ActividadesProgramadas buscarActividadProgramada(int idActividad);
    
    public List<ActividadesProgramadas> listarActividadesProgramadasEncargados(int idEncargado, String palabra);
    
    public List<ActividadesProgramadas> listarActividadesProgramadasDisponibles(int idEstudiante, String palabra);
    
    public List<ActividadesProgramadas> listarActividadesProgramadasActividad(int idActividad);
}
