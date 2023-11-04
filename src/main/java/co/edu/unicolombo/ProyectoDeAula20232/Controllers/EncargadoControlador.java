
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Encargados;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IEncargadoServicios;
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
@RequestMapping("/Encargados")
public class EncargadoControlador {
    
    @Autowired
    IUsuarioServicios userService;
    
    @Autowired
    IEncargadoServicios encargadoService;
    
    @GetMapping("")
    public String listarEncargados(Model modelo, @Param("palabra")String palabra, HttpSession session){
        List<Encargados> listaEncargados = (List<Encargados>)encargadoService.listarEncargados(palabra);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("encargados", listaEncargados);
        modelo.addAttribute("palabra", palabra);
        log.info("Ejecuntando el controlador listar encargados");
        return "Encargados/ListaEncargados";
    }
    
    @GetMapping("/Add")
    public String registarEncargado(Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("encargado", new Encargados());
        log.info("Ejecuntando el controlador registrar encargado");
        return "Encargados/FormularioEncargados";
    }
    
    @PostMapping("/Save")
    public String guardarEncargado(@Valid @ModelAttribute Encargados encargado, Model modelo, RedirectAttributes atributos, HttpSession session){
        encargado.setEstado("Activo");
        encargado.setTipo("Encargado");
        String defaultPassword = "1234";
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        try{
            int id = encargado.getIdUsuario();
            if(id == 0){
                if(!logueado.getTipo().equals("Administrador")){
                    atributos.addFlashAttribute("danger", "Accion No Permitida");
                    return "redirect:/";
                }
                BCryptPasswordEncoder encoder1 = new BCryptPasswordEncoder();
                String password1 = encoder1.encode(defaultPassword);
                encargado.setPassword(password1);
                encargadoService.guardarEncargado(encargado);
                atributos.addFlashAttribute("success", "Encargado Registrado Exitosamente");
                return "redirect:/Encargados";
            }else{
                if(encargado.getIdUsuario() == logueado.getIdUsuario()){
                    if(!logueado.getTipo().equals("Encargado")){
                        atributos.addFlashAttribute("danger", "Accion No Permitida");
                        return "redirect:/";
                    }
                    BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();
                    String password2 = encoder2.encode(encargado.getPassword());
                    encargado.setPassword(password2);
                    encargadoService.guardarEncargado(encargado);
                    Usuarios user = userService.buscarUsuario(encargado.getIdUsuario());
                    session.setAttribute("usuario.session", user);
                    atributos.addFlashAttribute("success", "Datos Modificados Exitosamente");
                    return "redirect:/";
                }else{
                    if(!logueado.getTipo().equals("Administrador")){
                        atributos.addFlashAttribute("danger", "Accion No Permitida");
                        return "redirect:/";
                    }
                    Encargados e = encargadoService.buscarEncargado(id);
                    e.setCedula(encargado.getCedula());
                    e.setNombre(encargado.getNombre());
                    e.setApellidos(encargado.getApellidos());
                    e.setCorreo(encargado.getCorreo());
                    e.setTelefono(encargado.getTelefono());
                    e.setCargo(encargado.getCargo());
                    encargadoService.guardarEncargado(e);
                    atributos.addFlashAttribute("success", "Encargado Modificado Exitosamente");
                    return "redirect:/Encargados";
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
            modelo.addAttribute("encargado", encargado);
            return "Encargados/FormularioEncargados";
        }
    }
    
    @GetMapping("/Edit/{idUsuario}")
    public String editarEncargado(Encargados encargado, Model modelo, HttpSession session){
        encargado = encargadoService.buscarEncargado(encargado.getIdUsuario());
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Coordinador") || logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("encargado", encargado);
        return "Encargados/FormularioEncargados";
    }
    
    @GetMapping("/Delete/{idUsuario}")
    public String eliminarEncargado(Encargados encargado, RedirectAttributes atributos){
        Encargados e = encargadoService.buscarEncargado(encargado.getIdUsuario());
        e.setEstado("Eliminado");
        encargadoService.guardarEncargado(e);
        atributos.addFlashAttribute("warning", "Encargado Eliminado");
        return "redirect:/Encargados";
    }
}
