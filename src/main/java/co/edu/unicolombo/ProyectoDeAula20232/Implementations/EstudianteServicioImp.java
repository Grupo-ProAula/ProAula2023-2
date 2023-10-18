
package co.edu.unicolombo.ProyectoDeAula20232.Implementations;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudEstudiantes;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Estudiantes;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IEstudianteServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstudianteServicioImp implements IEstudianteServicios{
    
    @Autowired
    private ICrudEstudiantes crudEstudiante;
    
    @Override
    public List<Estudiantes> listarEstudiantes(String palabra) {
       if(palabra != null){
           return crudEstudiante.buscarEstudiantes(palabra);
       }
       return crudEstudiante.listarEstudiantesActivos();
    }

    @Override
    public void guardarEstudiante(Estudiantes student) {
        crudEstudiante.save(student);
    }

    @Override
    public void eliminarEstudiante(Estudiantes student) {
        crudEstudiante.delete(student);
    }

    @Override
    public Estudiantes buscarEstudiante(Integer id) {
        return crudEstudiante.findById(id).orElse(null);
    }

    @Override
    public List<Estudiantes> listarEstudiantesPrograma(int idPrograma) {
        return crudEstudiante.listarEstudiantesPrograma(idPrograma);
    }
    
}
