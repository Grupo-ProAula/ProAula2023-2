
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
import org.springframework.validation.Errors;
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
    public String guardarActividad(@Valid Actividades actividad , Errors errores, Model modelo, RedirectAttributes atributos){
        if(errores.hasErrors()){
            modelo.addAttribute("actividad", new Actividades());
            return "Actividades/FormularioActividades";
        }
        actividad.setEstado("Activo");
        activiyService.guardarActividad(actividad);
        atributos.addFlashAttribute("success", "Actividad Registrada Exitosamente");
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
