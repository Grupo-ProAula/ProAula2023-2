
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Actividades;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadServicios;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class ActividadControlador {
    
    @Autowired
    IActividadServicios activiyService;
    
    @GetMapping("/Actividades")
    public String listarActividades(Model modelo, @Param("palabra")String palabra,HttpSession session){
        List<Actividades> listaActividad = (List<Actividades>)activiyService.listarActividades(palabra);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("actividades", listaActividad);
        modelo.addAttribute("palabra", palabra);
        log.info("Ejecuntando el controlador listar Actividades");
        return "Actividades/ListaActividades";
    }
    
    @GetMapping("/RegistrarActividad")
    public String MostrarFormularioActividades(Model modelo,HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("actividad", new Actividades());
        return "Actividades/FormularioActividades";
    }
    
    @PostMapping("/GuardarActividad")
    public String guardarActividad(@Valid Actividades actividad, Model modelo, RedirectAttributes atributos, HttpSession session){
        actividad.setEstado("Activo");
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        try{
            if(actividad.getIdActividad() == 0){
                activiyService.guardarActividad(actividad);
                atributos.addFlashAttribute("success", "Actividad Registrada Exitosamente");
            }else{
                activiyService.guardarActividad(actividad);
                atributos.addFlashAttribute("success", "Actividad Modificada Exitosamente");
            }
        }catch(Exception ex){
            String mensaje="";
            if(ex.getMessage().contains("ConstraintViolationException")){
                mensaje="Hubo Un Error";
            }else{
                mensaje= ex.getMessage();
            }
            modelo.addAttribute("usuario", logueado);
            modelo.addAttribute("danger", ""+mensaje);
            modelo.addAttribute("actividad", actividad);
            return "Actividades/FormularioActividades";
        }
        return "redirect:/Actividades";
    }
    
    @GetMapping("/EditarActividad/{idActividad}")
    public String editarActividad(Actividades actividad, Model modelo,HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        modelo.addAttribute("usuario", logueado);
        actividad = activiyService.buscarActividad(actividad);
        modelo.addAttribute("actividad", actividad);
        return "Actividades/FormularioActividades";
    }
    
    @GetMapping("/EliminarActividad/{idActividad}")
    public String eliminarActividad(Actividades actividad, RedirectAttributes atributos){
        Actividades a = activiyService.buscarActividad(actividad);
        a.setEstado("Eliminado");
        activiyService.guardarActividad(a);
        atributos.addFlashAttribute("warning", "Actividad Eliminada");
        return "redirect:/Actividades";
    }
}
