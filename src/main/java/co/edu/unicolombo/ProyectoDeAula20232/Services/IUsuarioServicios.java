
package co.edu.unicolombo.ProyectoDeAula20232.Services;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import java.util.List;

public interface IUsuarioServicios{
    
    public List<Usuarios> listarUsuarios();
    
    public void guardarUsuario(Usuarios user);
    
    public void eliminarUsuario(Usuarios user);
    
    public Usuarios buscarUsuario(Integer id);
}
