
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Asistencias;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Estudiantes;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Participaciones;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadProgramadaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IAsistenciaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IEstudianteServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IParticipacionesServicios;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/Asistencias")
public class AsistenciaControlador {
    
    @Autowired
    IActividadProgramadaServicios programActivityService;
    
    @Autowired
    IAsistenciaServicios asistenciaService;
    
    @Autowired
    IEstudianteServicios studentService;
    
    @Autowired
    IParticipacionesServicios participanteService;
    
    @GetMapping("")
    public String listarAsistencias(Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        Estudiantes e = studentService.buscarEstudiante(logueado.getIdUsuario());
        List<Asistencias> listaAsistencias = (List<Asistencias>)asistenciaService.listarAsistencias(e.getIdUsuario(),null);
        String horasTotales = sumarHoras(listaAsistencias);
        log.info("Horas Totales: "+horasTotales);
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiante", e);
        modelo.addAttribute("actividad", new ActividadesProgramadas());
        modelo.addAttribute("totalHoras", horasTotales);
        modelo.addAttribute("asistencias", listaAsistencias);
        return "Asistencias/ListaAsistencias";
    }
    
    @GetMapping("/Estudiante/{idUsuario}")
    public String listarAsistenciasEstudiante(@PathVariable(name="idUsuario")int id, Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        Estudiantes e = studentService.buscarEstudiante(id);
        List<Asistencias> listaAsistencias = (List<Asistencias>)asistenciaService.listarAsistencias(e.getIdUsuario(),null);
        String horasTotales = sumarHoras(listaAsistencias);
        log.info("Horas Totales: "+horasTotales);
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiante", e);
        modelo.addAttribute("actividad", new ActividadesProgramadas());
        modelo.addAttribute("totalHoras", horasTotales);
        modelo.addAttribute("asistencias", listaAsistencias);
        return "Asistencias/ListaAsistencias";
    }
    
    @GetMapping("/Actividad/{idActividadProgramada}")
    public String listarAsistenciasActividad(@PathVariable(name="idActividadProgramada")int id, Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(id);
        List<Asistencias> listaAsistencias = (List<Asistencias>)asistenciaService.listarAsistencias(null,ap.getIdActividadProgramada());
        String horasTotales = sumarHoras(listaAsistencias);
        log.info("Horas Totales: "+horasTotales);
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiante", new Estudiantes());
        modelo.addAttribute("actividad", ap);
        modelo.addAttribute("totalHoras", horasTotales);
        modelo.addAttribute("asistencias", listaAsistencias);
        return "Asistencias/ListaAsistencias";
    }
    
    @GetMapping("/Add/{idActividadProgramada}")
    public String registrarAsistencia(@PathVariable(name="idActividadProgramada")int id, Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Encargado")){
            return "redirect:/";
        }
        ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(id);
        List<Participaciones> listaParticipaciones = (List<Participaciones>)participanteService.listarParticipaciones(null,ap.getIdActividadProgramada() , null);        
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("participantes", listaParticipaciones);
        modelo.addAttribute("actividad", ap);
        return "Asistencias/FormularioAsistencias";
    }
    
    @PostMapping("/Save")
    public String guardarAsistencia(@RequestParam(value = "estudiantes[]") Participaciones[] participantes, @RequestParam(value = "fechaAsistencia") String fecha, @RequestParam(value = "idActividad") int idActividad, RedirectAttributes atributos,  Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        String fechaString = fecha;
        Date h = new Date();
        try {
            ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(idActividad);
            Date f = formato.parse(fechaString);
            if(participantes.length == 0){
                List<Participaciones> listaParticipaciones = (List<Participaciones>)participanteService.listarParticipaciones(null,ap.getIdActividadProgramada() , null);        
                modelo.addAttribute("usuario", logueado);
                modelo.addAttribute("participantes", listaParticipaciones);
                modelo.addAttribute("actividad", ap);
                return "Asistencias/FormularioAsistencias";
            }else{
                for(Participaciones p : participantes){
                    Asistencias asistencia = new Asistencias();
                    asistencia.setParticipacion(p);
                    asistencia.setFechaAsistencia(f);
                    asistencia.setHorasAsistidas(new Time(25200000));
                    asistencia.setEstado("Activo");
                    asistenciaService.guardarAsistencia(asistencia);
                }
                atributos.addFlashAttribute("success", "Asistencia(s) Registrada(s) Exitosamente");
                return "redirect:/Asistencias/Actividad/"+ap.getIdActividadProgramada();
            }
        } catch (ParseException ex) {
            Logger.getLogger(AsistenciaControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/Asistencias/Actividad/"+idActividad;
    }
    
    
    public String sumarHoras(List<Asistencias> lista){
        long milisegundos = 0;
        int horas = 0;
        int minutos = 0;
        int segundos = 0;
        for(Asistencias a : lista){
            milisegundos = a.getHorasAsistidas().getTime();
            Calendar calendario = Calendar.getInstance();
            calendario.setTimeInMillis(milisegundos);
            horas += calendario.get(Calendar.HOUR);
            int m = minutos + calendario.get(Calendar.MINUTE);
            if(m > 60){
                horas += 1;
                int var = m - 60;
                minutos = var;
            }else{
                minutos += calendario.get(Calendar.MINUTE);
            }
            segundos += calendario.get(Calendar.SECOND);
        }
        String horasString = String.format("%02d", horas);
        String minutosString = String.format("%02d", minutos);
        String segundosString = String.format("%02d", segundos);
        String total = horasString+":"+minutosString+":"+segundosString;
        return total;
    }
}
