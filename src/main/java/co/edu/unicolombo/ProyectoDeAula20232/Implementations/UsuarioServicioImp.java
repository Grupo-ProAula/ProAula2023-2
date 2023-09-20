
package co.edu.unicolombo.ProyectoDeAula20232.Implementations;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudUsuario;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IUsuarioServicios;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicioImp implements IUsuarioServicios{
    
    @Autowired
    private ICrudUsuario crudUser;
    
    @Override
    public List<Usuarios> listarUsuarios() {
        List<Usuarios> listaUsuarios = StreamSupport.stream(this.crudUser.findAll().spliterator(), false).collect(Collectors.toList());
        return listaUsuarios;
    }

    @Override
    public void guardarUsuario(Usuarios user) {
        crudUser.save(user);
    }

    @Override
    public void eliminarUsuario(Usuarios user) {
        crudUser.delete(user);
    }

    @Override
    public Usuarios buscarUsuario(Usuarios user) {
        return crudUser.findById(user.getCedula()).orElse(null);
    }
    
}
