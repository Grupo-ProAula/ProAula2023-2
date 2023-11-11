
package co.edu.unicolombo.ProyectoDeAula20232.Implementations;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudHorarios;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Horarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IHorarioServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HorarioServicioImp implements IHorarioServicios{
    
    @Autowired
    private ICrudHorarios crudHorario;
    
    @Override
    public List<Horarios> listarHorarios(Integer idActividad) {
        return crudHorario.listarHorariosActividad(idActividad);
    }

    @Override
    public void guardarHorario(Horarios horario) {
        crudHorario.save(horario);
    }

    @Override
    public void eliminarHorario(Horarios horario) {
        crudHorario.delete(horario);
    }

    @Override
    public Horarios buscarHorario(Integer id) {
        return crudHorario.findById(id).orElse(null);
    }

    @Override
    public Horarios buscarHorarioDatos(String dia, Integer idActividad) {
        return crudHorario.verificarHorario(dia, idActividad);
    }
    
}
