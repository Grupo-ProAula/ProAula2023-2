
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Estudiantes;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Participaciones;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadProgramadaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IEstudianteServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IParticipacionesServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Util.ParticipacionesExporterPDF;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/Participaciones")
public class ParticipacionControlador {
    
    @Autowired
    IActividadProgramadaServicios programActivityService;
    
    @Autowired
    IEstudianteServicios studentService;
    
    @Autowired
    IParticipacionesServicios participanteService;
    
    @GetMapping("/Enrol/{idActividadProgramada}")
    public String inscripcionActividadProgramada(ActividadesProgramadas actividad, RedirectAttributes atributos, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(actividad.getIdActividadProgramada());
        Estudiantes e = studentService.buscarEstudiante(logueado.getIdUsuario());
        Participaciones p = participanteService.verificarParticipacion(e.getIdUsuario(), ap.getIdActividadProgramada());
        if(p != null){
            p.setEstado("Activo");
            participanteService.guardarParticipacion(p);
        }else{
            Participaciones participacion = new Participaciones();
            participacion.setActividadProgramada(ap);
            participacion.setEstudiante(e);
            participacion.setFechaInscripcion(new Date());
            participacion.setEstado("Activo");
            participanteService.guardarParticipacion(participacion);
        }
        return "redirect:/ActividadesProgramadas";
    }
    
    @GetMapping("")
    public String listarActividadesInscritas(Model modelo, @Param("palabra")String palabra, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        Estudiantes e = studentService.buscarEstudiante(logueado.getIdUsuario());
        List<Participaciones> listaParticipaciones = (List<Participaciones>) participanteService.listarParticipaciones(e.getIdUsuario(), null, palabra);
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiante", e);
        modelo.addAttribute("actividad", new ActividadesProgramadas());
        modelo.addAttribute("participaciones", listaParticipaciones);
        modelo.addAttribute("palabra", palabra);
        return "Participaciones/ListaParticipaciones";
    }
    
    @GetMapping("/Estudiante/{idUsuario}")
    public String listarParticipacionesEstudiante(@PathVariable(name="idUsuario")int id, Model modelo, @Param("palabra")String palabra, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        Estudiantes e = studentService.buscarEstudiante(id);
        List<Participaciones> listaParticipaciones = (List<Participaciones>) participanteService.listarParticipaciones(e.getIdUsuario(), null, palabra);
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiante", e);
        modelo.addAttribute("actividad", new ActividadesProgramadas());
        modelo.addAttribute("participaciones", listaParticipaciones);
        modelo.addAttribute("palabra", palabra);
        return "Participaciones/ListaParticipaciones";
    }
    
    @GetMapping("/Actividad/{idActividadProgramada}")
    public String listarParticipacionesActividad(@PathVariable(name="idActividadProgramada")int id, Model modelo, @Param("palabra")String palabra, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(id);
        List<Participaciones> listaParticipaciones = (List<Participaciones>)participanteService.listarParticipaciones(null,ap.getIdActividadProgramada() , palabra);        
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiante", new Estudiantes());
        modelo.addAttribute("actividad", ap);
        modelo.addAttribute("participaciones", listaParticipaciones);
        modelo.addAttribute("palabra", palabra);
        return "Participaciones/ListaParticipaciones";
    }
    
    @GetMapping("/Unrol/{idParticipacion}")
    public String desinscribirse(@PathVariable(name="idParticipacion")int id, RedirectAttributes atributos, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        Participaciones p = participanteService.buscarParticipacion(id);
        p.setEstado("Eliminado");
        participanteService.guardarParticipacion(p);
        return "redirect:/Participaciones";
    }
    
    @GetMapping("/Actividad/PDF/{idActividadProgramada}")
    public void generarPDFInscripcionesActividad(@PathVariable(name="idActividadProgramada")int id, HttpServletResponse response) throws IOException{
        ActividadesProgramadas ap = programActivityService.buscarActividadProgramada(id);
        List<Participaciones> lista = participanteService.listarParticipaciones(null, id,null);
        response.setContentType("application/pdf");
        
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fecha = dateFormatter.format(new Date());
        
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Inscripciones_"+ap.getActividad().getNombre()+"_"+fecha+".pdf";
        
        response.setHeader(cabecera, valor);
        
        ParticipacionesExporterPDF exporter = new ParticipacionesExporterPDF(lista, ap, null);
        exporter.Exportar(response);
    }
    
    @GetMapping("/Estudiante/PDF/{idUsuario}")
    public void generarPDFInscripcionesEstudiante(@PathVariable(name="idUsuario")int id, HttpServletResponse response) throws IOException{
        Estudiantes e = studentService.buscarEstudiante(id);
        List<Participaciones> lista = participanteService.listarParticipaciones(id, null,null);
        response.setContentType("application/pdf");
        
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fecha = dateFormatter.format(new Date());
        
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Inscripciones_"+e.getNombre()+"_"+e.getApellidos()+"_"+fecha+".pdf";
        
        response.setHeader(cabecera, valor);
        
        ParticipacionesExporterPDF exporter = new ParticipacionesExporterPDF(lista, null, e);
        exporter.Exportar(response);
    }
}
