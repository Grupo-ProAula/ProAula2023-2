
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class EncargadoControlador {
    
    @Autowired
    IUsuarioServicios userService;
    
    @Autowired
    IEncargadoServicios encargadoService;
    
    @GetMapping("/Encargados")
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
    
    @GetMapping("/RegistrarEncargado")
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
    
    @PostMapping("/GuardarEncargado")
    public String guardarEncargado(@Valid @ModelAttribute Encargados encargado, Model modelo, RedirectAttributes atributos, HttpSession session){
        encargado.setEstado("Activo");
        encargado.setTipo("Encargado");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(encargado.getPassword());
        encargado.setPassword(password);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        try{
            int id = encargado.getIdUsuario();
            encargadoService.guardarEncargado(encargado);
            if(id == 0){
                atributos.addFlashAttribute("success", "Encargado Registrado Exitosamente");
            }else{
                if(encargado.getIdUsuario() == logueado.getIdUsuario()){
                    Usuarios user = userService.buscarUsuario(encargado.getIdUsuario());
                    session.setAttribute("usuario.session", user);
                    atributos.addFlashAttribute("success", "Datos Modificados Exitosamente");
                    return "redirect:/";
                }
                atributos.addFlashAttribute("success", "Encargado Modificado Exitosamente");
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
        return "redirect:/Encargados";
    }
    
    @GetMapping("/EditarEncargado/{idUsuario}")
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
    
    @GetMapping("/EliminarEncargado/{idUsuario}")
    public String eliminarEncargado(Encargados encargado, RedirectAttributes atributos){
        Encargados e = encargadoService.buscarEncargado(encargado.getIdUsuario());
        e.setEstado("Eliminado");
        encargadoService.guardarEncargado(e);
        atributos.addFlashAttribute("warning", "Encargado Eliminado");
        return "redirect:/Encargados";
    }
}
