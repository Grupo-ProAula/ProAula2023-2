package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Programas;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IProgramaServicios;
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
public class ProgramaControlador {
    
    @Autowired
    IProgramaServicios programService;
    
    @GetMapping("/Programas")
    public String listarEstudiantes(Model modelo, @Param("palabra")String palabra, HttpSession session){
        List<Programas> listaProgramas = (List<Programas>)programService.listarProgramas(palabra);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("programas", listaProgramas);
        modelo.addAttribute("palabra", palabra);
        log.info("Ejecuntando el controlador listar Programas");
        return "Programas/ListaProgramas";
    }
    
    @GetMapping("/RegistrarPrograma")
    public String MostrarFormularioProgramas(Model modelo, HttpSession session){
        Programas p = new Programas();
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("programa", p);
        return "Programas/FormularioProgramas";
    }
    
    @PostMapping("/GuardarPrograma")
    public String guardarPrograma(@Valid Programas programa , Errors errores, Model modelo, RedirectAttributes atributos){
        if(errores.hasErrors()){
            modelo.addAttribute("programa", new Programas());
            atributos.addFlashAttribute("danger", "Ha Ocurrido Un Error");
            return "Programas/FormularioProgramas";
        }
        programa.setEstado("Activo");
        if(programa.getIdPrograma() == 0){
            programService.guardarPrograma(programa);
            atributos.addFlashAttribute("success", "Programa Registrado Exitosamente");
        }else{
            programService.guardarPrograma(programa);
            atributos.addFlashAttribute("success", "Programa Modificado Exitosamente");
        }
        return "redirect:/Programas";
    }
    
    @GetMapping("/EditarPrograma/{idPrograma}")
    public String editarPrograma(Programas programa, Model modelo, HttpSession session){
        programa = programService.buscarPrograma(programa);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("programa", programa);
        return "Programas/FormularioProgramas";
    }
    
    @GetMapping("/EliminarPrograma/{idPrograma}")
    public String eliminarPrograma(Programas programa, RedirectAttributes atributos){
        Programas p = programService.buscarPrograma(programa);
        p.setEstado("Eliminado");
        programService.guardarPrograma(p);
        atributos.addFlashAttribute("warning", "Programa Eliminado");
        return "redirect:/Programas";
    }
}