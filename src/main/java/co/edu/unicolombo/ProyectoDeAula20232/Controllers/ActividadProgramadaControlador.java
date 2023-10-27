
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Encargados;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Estudiantes;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadProgramadaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IEncargadoServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IEstudianteServicios;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/ActividadesProgramadas")
public class ActividadProgramadaControlador {
    
    @Autowired
    IActividadProgramadaServicios programActivityService;
    
    @Autowired
    IActividadServicios activityService;
    
    @Autowired
    IEncargadoServicios encargadoService;
    
    @Autowired
    IEstudianteServicios studentService;
        
    @GetMapping("")
    public String listarActividadesProgramadas(Model modelo, @Param("palabra")String palabra, HttpSession session){
        List<ActividadesProgramadas> listaActividades = (List<ActividadesProgramadas>)programActivityService.listarActividadesProgramadas(palabra);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Coordinador")){
            return "redirect:/";
        }
        if(logueado.getTipo().equals("Encargado")){
            Encargados e = encargadoService.buscarEncargado(logueado.getIdUsuario());
            listaActividades = programActivityService.listarActividadesProgramadasEncargados(e.getIdUsuario(), palabra);
        }
        if(logueado.getTipo().equals("Estudiante")){
            Estudiantes e = studentService.buscarEstudiante(logueado.getIdUsuario());
            listaActividades = programActivityService.listarActividadesProgramadasDisponibles(e.getIdUsuario(), palabra);
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
    
    @GetMapping("/Add")
    public String MostrarFormularioActividadesProgramadas(Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("encargados", encargadoService.listarEncargados(null));
        modelo.addAttribute("actividades", activityService.listarActividades(null));
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("actividadProgramada", new ActividadesProgramadas());
        return "ActividadesProgramadas/FormularioActividadesProgramadas";
    }
    
    @PostMapping("/Save")
    public String guardarActividadProgramada(@Valid ActividadesProgramadas actividadProgramada, Model modelo, RedirectAttributes atributos, HttpSession session){
        actividadProgramada.setEstado("Activo");
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        try{
            if(actividadProgramada.getFechaInicio().after(actividadProgramada.getFechaFin())){
                modelo.addAttribute("usuario", logueado);
                modelo.addAttribute("encargados", encargadoService.listarEncargados(null));
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
            modelo.addAttribute("encargados", encargadoService.listarEncargados(null));
            modelo.addAttribute("actividades", activityService.listarActividades(null));
            modelo.addAttribute("danger", ""+mensaje);
            modelo.addAttribute("actividadProgramada", actividadProgramada);
            return "ActividadesProgramadas/FormularioActividadesProgramadas";
        }
        return "redirect:/ActividadesProgramadas";
    }
    
    @GetMapping("/Edit/{idActividadProgramada}")
    public String editarActividadProgramada(ActividadesProgramadas actividad, Model modelo, HttpSession session){
        actividad = programActivityService.buscarActividadProgramada(actividad.getIdActividadProgramada());
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("encargados", encargadoService.listarEncargados(null));
        modelo.addAttribute("actividades", activityService.listarActividades(null));
        modelo.addAttribute("actividadProgramada", actividad);
        return "ActividadesProgramadas/FormularioActividadesProgramadas";
    }
    
    @GetMapping("/Delete/{idActividadProgramada}")
    public String eliminarActividadProgramada(ActividadesProgramadas actividad, RedirectAttributes atributos){
        ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(actividad.getIdActividadProgramada());
        ap.setEstado("Eliminado");
        programActivityService.guardarActividadProgramada(ap);
        atributos.addFlashAttribute("warning", "Actividad Eliminada");
        return "redirect:/ActividadesProgramadas";
    }
}




























