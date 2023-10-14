
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadProgramadaServicios;
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
public class ActividadProgramadaControlador {
    
    @Autowired
    IActividadProgramadaServicios programActivityService;
    
    @Autowired
    IActividadServicios activityService;
        
    @GetMapping("/ActividadesProgramadas")
    public String listarActividadesProgramadas(Model modelo, @Param("palabra")String palabra, HttpSession session){
        List<ActividadesProgramadas> listaActividades = (List<ActividadesProgramadas>)programActivityService.listarActividadesProgramadas(palabra);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("actividadesProgramadas", listaActividades);
        modelo.addAttribute("palabra", palabra);
        log.info("Ejecuntando el controlador listar Actividades Programadas");
        return "ActividadesProgramadas/ListaActividadesProgramadas";
    }
    
    @GetMapping("/RegistrarActividadProgramada")
    public String MostrarFormularioActividadesProgramadas(Model modelo, HttpSession session){
        modelo.addAttribute("actividades", activityService.listarActividades(null));
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("actividadProgramada", new ActividadesProgramadas());
        return "ActividadesProgramadas/FormularioActividadesProgramadas";
    }
    
    @PostMapping("/GuardarActividadProgramada")
    public String guardarActividadProgramada(@Valid ActividadesProgramadas actividadProgramada , Errors errores, Model modelo, RedirectAttributes atributos){
        if(errores.hasErrors()){
            modelo.addAttribute("actividades", activityService.listarActividades(null));
            modelo.addAttribute("actividadProgramada", new ActividadesProgramadas());
            atributos.addFlashAttribute("danger", "Hubo un error al programar la actividad");
            log.info("HUBO UN ERROR");
            return "redirect:/RegistrarActividadProgramada";
        }
        actividadProgramada.setEstado("Activo");
        programActivityService.guardarActividadProgramada(actividadProgramada);
        atributos.addFlashAttribute("success", "Actividad Programada Exitosamente");
        return "redirect:/ActividadesProgramadas";
    }
    
    @GetMapping("/EditarActividadProgramada/{idActividadProgramada}")
    public String editarActividadProgramada(ActividadesProgramadas actividad, Model modelo, HttpSession session){
        actividad = programActivityService.buscarActividadProgramada(actividad);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("actividades", activityService.listarActividades(null));
        modelo.addAttribute("actividadProgramada", actividad);
        return "ActividadesProgramadas/FormularioActividadesProgramadas";
    }
    
    @GetMapping("/EliminarActividadProgramada/{idActividadProgramada}")
    public String eliminarActividadProgramada(ActividadesProgramadas actividad, RedirectAttributes atributos){
        ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(actividad);
        ap.setEstado("Eliminado");
        programActivityService.guardarActividadProgramada(ap);
        atributos.addFlashAttribute("warning", "Actividad Eliminada");
        return "redirect:/ActividadesProgramadas";
    }
}



























