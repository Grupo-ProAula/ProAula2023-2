
package co.edu.unicolombo.ProyectoDeAula20232.Services;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Coordinadores;
import java.util.List;

public interface ICoordinadoresServicios {
    
    public List<Coordinadores> listarCoordinadores(String palabra);
    
    public void guardarCoordinador(Coordinadores coordinador);
    
    public void eliminarCoordinador(Coordinadores coordinador);
    
    public Coordinadores buscarCoordinador(Integer id);
}
