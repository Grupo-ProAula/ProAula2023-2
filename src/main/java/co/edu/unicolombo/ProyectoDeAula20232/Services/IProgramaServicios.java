
package co.edu.unicolombo.ProyectoDeAula20232.Services;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Administradores;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Programas;
import java.util.List;

public interface IProgramaServicios {
    
    public List<Programas> listarProgramas(String palabra);
    
    public void guardarPrograma(Programas program);
    
    public void eliminarPrograma(Programas program);
    
    public Programas buscarPrograma(Programas program);
    
    public List<Programas> listarProgramasDisponibles(int idCoordinador);
}
