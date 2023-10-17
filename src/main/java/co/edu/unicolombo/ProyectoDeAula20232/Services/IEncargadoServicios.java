
package co.edu.unicolombo.ProyectoDeAula20232.Services;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Encargados;
import java.util.List;

public interface IEncargadoServicios {
    
    public List<Encargados> listarEncargados(String palabra);
    
    public void guardarEncargado(Encargados encargado);
    
    public void eliminarEncargado(Encargados encargado);
    
    public Encargados buscarEncargado(Encargados encargado);
}
