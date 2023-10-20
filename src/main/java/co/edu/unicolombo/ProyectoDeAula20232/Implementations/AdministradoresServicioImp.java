
package co.edu.unicolombo.ProyectoDeAula20232.Implementations;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudAdministradores;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Administradores;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IAdministradoresService;

@Service
public class AdministradoresServicioImp implements IAdministradoresService{
    
    @Autowired
    private ICrudAdministradores crudAdministradores;

    @Override
    public List<Administradores> listarAdministradores(String palabra) {
        if(palabra!=null){
            return crudAdministradores.buscarAdministradores(palabra);
        }
        return crudAdministradores.listarAdministradoresActivos();
    }

    @Override
    public void guardarAdministrador(Administradores admin) {
        crudAdministradores.save(admin);
    }

    @Override
    public void eliminarAdministrador(Administradores admin) {
        crudAdministradores.delete(admin);
    }

    @Override
    public Administradores buscarAdministrador(Integer id) {
        return crudAdministradores.findById(id).orElse(null);
    }    
}



