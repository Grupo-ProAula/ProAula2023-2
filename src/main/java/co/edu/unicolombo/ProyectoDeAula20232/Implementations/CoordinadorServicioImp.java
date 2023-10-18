
package co.edu.unicolombo.ProyectoDeAula20232.Implementations;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudCoordinadores;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Coordinadores;
import co.edu.unicolombo.ProyectoDeAula20232.Services.ICoordinadoresServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoordinadorServicioImp implements ICoordinadoresServicios{
    
    @Autowired
    private ICrudCoordinadores crudCoordinador;
    
    @Override
    public List<Coordinadores> listarCoordinadores(String palabra) {
        if(palabra != null){
            return crudCoordinador.buscarCoordinadores(palabra);
        }
        return crudCoordinador.listarCoordinadoresActivos();
    }

    @Override
    public void guardarCoordinador(Coordinadores coordinador) {
        crudCoordinador.save(coordinador);
    }

    @Override
    public void eliminarCoordinador(Coordinadores coordinador) {
        crudCoordinador.delete(coordinador);
    }

    @Override
    public Coordinadores buscarCoordinador(Integer id) {
        return crudCoordinador.findById(id).orElse(null);
    }
    
}
