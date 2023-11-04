
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Actividades;
import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadProgramadaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IActividadServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Util.ActividadesExporterPDF;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/Actividades")
public class ActividadControlador {
    
    @Autowired
    IActividadProgramadaServicios programActivityService;
    
    @Autowired
    IActividadServicios activiyService;
    
    @GetMapping("")
    public String listarActividades(Model modelo, @Param("palabra")String palabra,HttpSession session){
        List<Actividades> listaActividad = (List<Actividades>)activiyService.listarActividades(palabra);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("actividades", listaActividad);
        modelo.addAttribute("palabra", palabra);
        log.info("Ejecuntando el controlador listar Actividades");
        return "Actividades/ListaActividades";
    }
    
    @GetMapping("/Add")
    public String MostrarFormularioActividades(Model modelo,HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("actividad", new Actividades());
        return "Actividades/FormularioActividades";
    }
    
    @PostMapping("/Save")
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
    
    @GetMapping("/Edit/{idActividad}")
    public String editarActividad(Actividades actividad, Model modelo,HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        actividad = activiyService.buscarActividad(actividad.getIdActividad());
        modelo.addAttribute("actividad", actividad);
        return "Actividades/FormularioActividades";
    }
    
    @GetMapping("/Delete/{idActividad}")
    public String eliminarActividad(Actividades actividad, RedirectAttributes atributos){
        Actividades a = activiyService.buscarActividad(actividad.getIdActividad());
        a.setEstado("Eliminado");
        activiyService.guardarActividad(a);
        atributos.addFlashAttribute("warning", "Actividad Eliminada");
        return "redirect:/Actividades";
    }
    
    @GetMapping("/PDF/{idActividad}")
    public void generarPDFActividad(@PathVariable(name="idActividad")int id, HttpServletResponse response) throws IOException{
        Actividades a = activiyService.buscarActividad(id);
        List<ActividadesProgramadas> listaActividades = programActivityService.listarActividadesProgramadasActividad(id);        
        response.setContentType("application/pdf");
        
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fecha = dateFormatter.format(new Date());
        
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Programaciones_"+a.getNombre()+"_"+fecha+".pdf";
        
        response.setHeader(cabecera, valor);
        
        ActividadesExporterPDF exporter = new ActividadesExporterPDF(listaActividades, a);
        exporter.Exportar(response);
    }
}
