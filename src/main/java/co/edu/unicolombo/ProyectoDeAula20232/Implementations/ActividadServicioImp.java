
package co.edu.unicolombo.ProyectoDeAula20232.Implementations;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudActividades;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Actividades;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActividadServicioImp implements IActividadServicios{
    
    @Autowired
    private ICrudActividades crudActividad;

    @Override
    public List<Actividades> listarActividades(String palabra) {
        if(palabra != null){
            return crudActividad.buscarActividades(palabra);
        }
        return crudActividad.listarActividadesActivas();
    }

    @Override
    public void guardarActividad(Actividades activity) {
        crudActividad.save(activity);
    }

    @Override
    public void eliminarActividad(Actividades activity) {
        crudActividad.delete(activity);
    }

    @Override
    public Actividades buscarActividad(int idActividad) {
        return crudActividad.findById(idActividad).orElse(null);
    }
    
}
