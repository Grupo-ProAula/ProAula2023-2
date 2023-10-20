
package co.edu.unicolombo.ProyectoDeAula20232.Implementations;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudProgramas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Programas;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IProgramaServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgramaServicioImp implements IProgramaServicios{
    
    @Autowired
    private ICrudProgramas crudPrograma;
    
    @Override
    public List<Programas> listarProgramas(String palabra) {
        if(palabra != null){
            return crudPrograma.buscarProgramas(palabra);
        }
        return crudPrograma.listarProgramasActivos();
    }

    @Override
    public void guardarPrograma(Programas program) {
        crudPrograma.save(program);
    }

    @Override
    public void eliminarPrograma(Programas program) {
        crudPrograma.delete(program);
    }

    @Override
    public Programas buscarPrograma(Programas program) {
        return crudPrograma.findById(program.getIdPrograma()).orElse(null);
    }

    @Override
    public List<Programas> listarProgramasDisponibles(int idCoordinador) {
        if(idCoordinador != 0){
            return crudPrograma.listarProgramasDisponibles(idCoordinador);
        }
        return crudPrograma.listarProgramasDisponibles();
    }


    
}
