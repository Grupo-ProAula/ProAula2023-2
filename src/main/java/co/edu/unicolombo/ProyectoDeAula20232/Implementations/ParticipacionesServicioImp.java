
package co.edu.unicolombo.ProyectoDeAula20232.Implementations;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudParticipaciones;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Participaciones;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IParticipacionesServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParticipacionesServicioImp implements IParticipacionesServicios{
    
    @Autowired
    private ICrudParticipaciones crudParticipacion;
    
    @Override
    public List<Participaciones> listarParticipaciones(Integer idEstudiante, Integer idActividad, String palabra) {
        if(palabra != null){
            if(idEstudiante != null){
                return crudParticipacion.buscarParticipacionesEstudiante(idEstudiante, palabra);
            }else if(idActividad != null){
                return crudParticipacion.buscarParticipacionesActividad(idActividad, palabra);
            }else{
                return crudParticipacion.buscarParticipacionesActivas(palabra);
            }
        }else if(idEstudiante != null){
            return crudParticipacion.listarParticipacionesEstudiante(idEstudiante);
        }else if(idActividad != null){
            return crudParticipacion.listarParticipacionesActividad(idActividad);
        }
        return crudParticipacion.listarParticipacionesActivas();
    }

    @Override
    public void guardarParticipacion(Participaciones participacion) {
        crudParticipacion.save(participacion);
    }

    @Override
    public void eliminarParticipacion(Participaciones participacion) {
        crudParticipacion.delete(participacion);
    }

    @Override
    public Participaciones buscarParticipacion(Integer id) {
        return crudParticipacion.findById(id).orElse(null);
    }

    @Override
    public Participaciones verificarParticipacion(Integer idEstudiante, Integer idActividad) {
        return crudParticipacion.buscarParticipacion(idEstudiante,idActividad);
    }
    
}
