
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadProgramadaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadServicios;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
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
        SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        form.setTimeZone(TimeZone.getTimeZone("America/Bogota"));
        for(ActividadesProgramadas a : listaActividades){
            String fecha = form.format(a.getFechaInicio());
            log.info("fecha Inicio: "+fecha);
        }
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
    public String guardarActividadProgramada(@Valid ActividadesProgramadas actividadProgramada, Model modelo, RedirectAttributes atributos, HttpSession session){
        actividadProgramada.setEstado("Activo");
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        try{
            if(actividadProgramada.getFechaInicio().after(actividadProgramada.getFechaFin())){
                modelo.addAttribute("usuario", logueado);
                modelo.addAttribute("actividades", activityService.listarActividades(null));
                modelo.addAttribute("danger", "La Fecha De Fin No Puede Ser Menor A La De Inicio");
                modelo.addAttribute("actividadProgramada", actividadProgramada);
                return "ActividadesProgramadas/FormularioActividadesProgramadas";
            }else{
                if(actividadProgramada.getIdActividadProgramada()== 0){
                    programActivityService.guardarActividadProgramada(actividadProgramada);
                    atributos.addFlashAttribute("success", "Actividad Programada Exitosamente");
                }else{
                    programActivityService.guardarActividadProgramada(actividadProgramada);
                    atributos.addFlashAttribute("success", "Actividad Programada Modificada Exitosamente");
                }
            }
        }catch(Exception ex){
            String mensaje="";
            if(ex.getMessage().contains("ConstraintViolationException")){
                mensaje="Hubo Un Error";
            }else{
                mensaje= ex.getMessage();
            }
            modelo.addAttribute("usuario", logueado);
            modelo.addAttribute("actividades", activityService.listarActividades(null));
            modelo.addAttribute("danger", ""+mensaje);
            modelo.addAttribute("actividadProgramada", actividadProgramada);
            return "ActividadesProgramadas/FormularioActividadesProgramadas";
        }
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



























