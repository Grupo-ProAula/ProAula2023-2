
package co.edu.unicolombo.ProyectoDeAula20232.Implementations;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudEncargados;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Encargados;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IEncargadoServicios;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EncargadoServicioImp implements IEncargadoServicios{
    
    @Autowired
    private ICrudEncargados crudEncargado;
    
    @Override
    public List<Encargados> listarEncargados(String palabra) {
        if(palabra != null){
            return crudEncargado.buscarEncargados(palabra);
        }
        return crudEncargado.listarEncargadosActivos();
    }

    @Override
    public void guardarEncargado(Encargados encargado) {
        crudEncargado.save(encargado);
    }

    @Override
    public void eliminarEncargado(Encargados encargado) {
        crudEncargado.delete(encargado);
    }

    @Override
    public Encargados buscarEncargado(Encargados encargado) {
        return crudEncargado.findById(encargado.getIdUsuario()).orElse(null);
    }
    
}
