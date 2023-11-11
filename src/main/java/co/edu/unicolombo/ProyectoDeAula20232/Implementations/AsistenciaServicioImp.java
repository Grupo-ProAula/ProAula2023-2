
package co.edu.unicolombo.ProyectoDeAula20232.Implementations;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudAsistencias;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Asistencias;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IAsistenciaServicios;
import java.sql.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsistenciaServicioImp implements IAsistenciaServicios{
    
    @Autowired
    private ICrudAsistencias crudAsistencia;

    @Override
    public List<Asistencias> listarAsistencias(Integer idEstudiante, Integer idActividad) {
        if(idEstudiante != null){
            return crudAsistencia.listarAsistenciasEstudiante(idEstudiante);
        }else if(idActividad != null){
            return crudAsistencia.listarAsistenciasActividad(idActividad);
        }
        return crudAsistencia.listarAsistenciasActivas();
    }

    @Override
    public void guardarAsistencia(Asistencias asistencia) {
        crudAsistencia.save(asistencia);
    }

    @Override
    public void eliminarAsistencia(Asistencias asistencia) {
        crudAsistencia.delete(asistencia);
    }

    @Override
    public Asistencias buscarAsistencia(Integer id) {
        return crudAsistencia.findById(id).orElse(null);
    }

    @Override
    public Asistencias verficiarAsistencia(Integer idActividad, Integer idEstudiante, Date fecha) {
        return crudAsistencia.verificarAsistencia(idActividad, idEstudiante, fecha);
    }
}
