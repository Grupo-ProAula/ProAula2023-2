
package co.edu.unicolombo.ProyectoDeAula20232.Security;

import co.edu.unicolombo.ProyectoDeAula20232.Dao.ICrudUsuario;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UsuarioDetailsServiceImp implements UserDetailsService {
    
    @Autowired
    private ICrudUsuario userCrud;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuarios u = userCrud.getUsuarioByCedula(username);
        if(u == null){
            throw new UsernameNotFoundException("No se Pudo Encontrar El Usuario");
        }
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute("usuario.session", u);
        return new UsuarioDetails(u);
    }
    
}
