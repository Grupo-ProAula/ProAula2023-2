
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Horarios;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadProgramadaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IHorarioServicios;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/Horarios")
public class HorarioControlador {
    
    @Autowired
    IHorarioServicios horarioService;
    
    @Autowired
    IActividadProgramadaServicios programActivityService;
    
    @GetMapping("/{idActividadProgramada}")
    public String listarHorarios(@PathVariable(name="idActividadProgramada")int id ,Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Coordinador")){
            return "redirect:/";
        }
        ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(id);
        List<Horarios> listaHorarios = (List<Horarios>)horarioService.listarHorarios(id);
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("actividad", ap);
        modelo.addAttribute("horarios", listaHorarios);
        return "Horarios/ListaHorarios";
    }
    
    @GetMapping("/Add/{idActividadProgramada}")
    public String registrarHorario(@PathVariable(name="idActividadProgramada")int id, Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(id);
        Horarios h = new Horarios();
        h.setActividad(ap);
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("horario", h);
        modelo.addAttribute("actividad", ap);
        return "Horarios/FormularioHorarios";
    }
    
    @PostMapping("/Save")
    public String guardarHorario(@ModelAttribute Horarios horario, RedirectAttributes atributos,  Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        log.info(horario.getHoraInicio()+"");
        try {
            if(horario.getHoraInicio().after(horario.getHoraFin())){
                modelo.addAttribute("usuario", logueado);
                modelo.addAttribute("horario", horario);
                modelo.addAttribute("actividad", horario.getActividad());
                modelo.addAttribute("danger", "La Hora De Fin No Puede Ser Menor A La De Inicio");
                return "Horarios/FormularioHorarios";
            }else{
                Horarios h = horarioService.buscarHorarioDatos(horario.getDia(), horario.getActividad().getIdActividadProgramada());
                if(h == null){
                    if(horario.getIdHorario()== 0){
                        horario.setEstado("Activo");
                        horarioService.guardarHorario(horario);
                        atributos.addFlashAttribute("success", "Horario Guardado Exitosamente");
                    }else{
                        horario.setEstado("Activo");
                        horarioService.guardarHorario(horario);
                        atributos.addFlashAttribute("success", "Horario Modificado Exitosamente");
                    }
                }else{
                    if(horario.getIdHorario() == h.getIdHorario()){
                        horarioService.guardarHorario(horario);
                    }else{
                        throw new Exception("Esta actividad ya tiene un horario en el dia seleccionado");
                    }               
                }
            }
        } catch (Exception ex) {
            String mensaje=ex.getMessage();
            modelo.addAttribute("usuario", logueado);
            modelo.addAttribute("horario", horario);
            modelo.addAttribute("actividad", horario.getActividad());
            modelo.addAttribute("danger", ""+mensaje);
            return "Horarios/FormularioHorarios";
        }
        return "redirect:/Horarios/"+horario.getActividad().getIdActividadProgramada();
    }
    
    @GetMapping("/Edit/{idHorario}")
    public String editarHorario(Horarios horario, Model modelo, HttpSession session){
        horario = horarioService.buscarHorario(horario.getIdHorario());
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("horario", horario);
        modelo.addAttribute("actividad", horario.getActividad());
        return "Horarios/FormularioHorarios";
    }
        
    @GetMapping("/Delete/{idHorario}")
    public String eliminarHorario(Horarios horario, RedirectAttributes atributos){
        Horarios h = horarioService.buscarHorario(horario.getIdHorario());
        h.setEstado("Eliminado");
        horarioService.guardarHorario(h);
        atributos.addFlashAttribute("warning", "Horario Eliminado");
        return "redirect:/Horarios/"+horario.getActividad().getIdActividadProgramada();
    }
}
