
package co.edu.unicolombo.ProyectoDeAula20232.Services;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Estudiantes;
import java.util.List;

public interface IEstudianteServicios {
       
    public List<Estudiantes> listarEstudiantes(String palabra);
    
    public void guardarEstudiante(Estudiantes student);
    
    public void eliminarEstudiante(Estudiantes student);
    
    public Estudiantes buscarEstudiante(Integer id);
    
    public List<Estudiantes> listarEstudiantesPrograma(int idPrograma, String palabra);
}
