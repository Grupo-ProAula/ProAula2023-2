
package co.edu.unicolombo.ProyectoDeAula20232.Implementations;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadProgramadaServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActividadProgramadaServicioImp implements IActividadProgramadaServicios{
    
    @Autowired
    ICrudActividadesProgramadas crudActividad;

    @Override
    public List<ActividadesProgramadas> listarActividadesProgramadas(String palabra) {
        if(palabra != null){
            return crudActividad.buscarActividadesProgramadas(palabra);
        }
        return crudActividad.listarActividadesProgramadasActivas();
    }

    @Override
    public void guardarActividadProgramada(ActividadesProgramadas activity) {
        crudActividad.save(activity);
    }

    @Override
    public void eliminarActividadProgramada(ActividadesProgramadas activity) {
        crudActividad.delete(activity);
    }

    @Override
    public ActividadesProgramadas buscarActividadProgramada(int idActividad) {
        return crudActividad.findById(idActividad).orElse(null);
    }

    @Override
    public List<ActividadesProgramadas> listarActividadesProgramadasEncargados(int idEncargado, String palabra) {
        if(palabra != null){
            return crudActividad.buscarActividadesProgramadasEncargado(idEncargado, palabra);
        }
        return crudActividad.listarActividadesProgramadasEncargado(idEncargado);
    }

    @Override
    public List<ActividadesProgramadas> listarActividadesProgramadasDisponibles(int idEstudiante, String palabra) {
        if(palabra != null){
            return crudActividad.buscarActividadesProgramadasDisponibles(idEstudiante, palabra);
        }
        return crudActividad.listarActividadesProgramadasDisponibles(idEstudiante);
    }
    
}
