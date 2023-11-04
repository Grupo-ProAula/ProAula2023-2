
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Coordinadores;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.ICoordinadoresServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IProgramaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IUsuarioServicios;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/Coordinadores")
public class CoordinadorControlador {
    
    @Autowired
    IUsuarioServicios userService;
    
    @Autowired
    ICoordinadoresServicios coordinadorService;
    
    @Autowired
    IProgramaServicios programService;
    
    @GetMapping("")
    public String listarCoordinadores(Model modelo, @Param("palabra")String palabra, HttpSession session){
        List<Coordinadores> listaCoordinadores = (List<Coordinadores>)coordinadorService.listarCoordinadores(palabra);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("coordinadores", listaCoordinadores);
        modelo.addAttribute("palabra", palabra);
        log.info("Ejecuntando el controlador listar Coordinadores");
        return "Coordinadores/ListaCoordinadores";
    }
    
    @GetMapping("/Add")
    public String registarCoordinador(Model modelo, HttpSession session, RedirectAttributes atributos){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        atributos.addFlashAttribute("danger", "La Cedula Ingresada Ya Existe");
        modelo.addAttribute("coordinador", new Coordinadores());
        modelo.addAttribute("programas", programService.listarProgramasDisponibles(0));
        log.info("Ejecuntando el controlador registrar coordinador");
        return "Coordinadores/FormularioCoordinadores";
    }
    
    @PostMapping("/Save")
    public String guardarCoordinador(@Valid @ModelAttribute Coordinadores coordinador, Model modelo, RedirectAttributes atributos, HttpSession session){
        coordinador.setEstado("Activo");
        coordinador.setTipo("Coordinador");
        String defaultPassword = "1234";
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        try{
            int id = coordinador.getIdUsuario();
            if(id == 0){
                if(!logueado.getTipo().equals("Administrador")){
                    atributos.addFlashAttribute("danger", "Accion No Permitida");
                    return "redirect:/";
                }
                BCryptPasswordEncoder encoder1 = new BCryptPasswordEncoder();
                String password1 = encoder1.encode(defaultPassword);
                coordinador.setPassword(password1);
                coordinadorService.guardarCoordinador(coordinador);
                atributos.addFlashAttribute("success", "Coordinador Registrado Exitosamente");
                return "redirect:/Coordinadores";
            }else{
                if(coordinador.getIdUsuario() == logueado.getIdUsuario()){
                    if(!logueado.getTipo().equals("Coordinador")){
                        atributos.addFlashAttribute("danger", "Accion No Permitida");
                        return "redirect:/";
                    }
                    BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();
                    String password2 = encoder2.encode(coordinador.getPassword());
                    coordinador.setPassword(password2);
                    coordinadorService.guardarCoordinador(coordinador);
                    Usuarios user = userService.buscarUsuario(coordinador.getIdUsuario());
                    session.setAttribute("usuario.session", user);
                    atributos.addFlashAttribute("success", "Datos Modificados Exitosamente");
                    return "redirect:/";
                }else{
                    if(!logueado.getTipo().equals("Administrador")){
                        atributos.addFlashAttribute("danger", "Accion No Permitida");
                        return "redirect:/";
                    }
                    Coordinadores c = coordinadorService.buscarCoordinador(id);
                    c.setCedula(coordinador.getCedula());
                    c.setNombre(coordinador.getNombre());
                    c.setApellidos(coordinador.getApellidos());
                    c.setCorreo(coordinador.getCorreo());
                    c.setTelefono(coordinador.getTelefono());
                    c.setPrograma(coordinador.getPrograma());
                    coordinadorService.guardarCoordinador(c);
                    atributos.addFlashAttribute("success", "Coordinador Modificado Exitosamente");
                    return "redirect:/Coordinadores";
                }
            }
        }catch(Exception ex){
            String mensaje="";
            if(ex.getMessage().contains("ConstraintViolationException")){
                mensaje="La Cedula Ingresada Ya Existe";
            }else{
                mensaje= ex.getMessage();
            }
            modelo.addAttribute("usuario", logueado);
            modelo.addAttribute("danger", ""+mensaje);
            modelo.addAttribute("coordinador", coordinador);
            modelo.addAttribute("programas", programService.listarProgramasDisponibles(coordinador.getIdUsuario()));
            return "Coordinadores/FormularioCoordinadores";
        }
    }
    
    @GetMapping("/Edit/{idUsuario}")
    public String editarCoordinador(Coordinadores coordinador, Model modelo, HttpSession session){
        coordinador = coordinadorService.buscarCoordinador(coordinador.getIdUsuario());
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        log.info(logueado.getTipo());
        if(logueado.getTipo().equals("Estudiante") || logueado.getTipo().equals("Encargado")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("programas", programService.listarProgramasDisponibles(coordinador.getIdUsuario()));
        modelo.addAttribute("coordinador", coordinador);
        return "Coordinadores/FormularioCoordinadores";
    }
    
    @GetMapping("/Delete/{idUsuario}")
    public String eliminarEstudiante(Coordinadores coordinador, RedirectAttributes atributos){
        Coordinadores c = coordinadorService.buscarCoordinador(coordinador.getIdUsuario());
        c.setEstado("Eliminado");
        coordinadorService.guardarCoordinador(c);
        atributos.addFlashAttribute("warning", "Coordinador Eliminado");
        return "redirect:/Coordinadores";
    }
}
