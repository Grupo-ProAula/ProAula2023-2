
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Coordinadores;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Estudiantes;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.ICoordinadoresServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IEstudianteServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IProgramaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IUsuarioServicios;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class EstudianteControlador {
    
    @Autowired
    ICoordinadoresServicios coordinadorService;
    
    @Autowired
    IUsuarioServicios userService;
    
    @Autowired
    IEstudianteServicios studentService;
    
    @Autowired
    IProgramaServicios programService;
    
    @GetMapping("/Estudiantes")
    public String listarEstudiantes(Model modelo, @Param("palabra")String palabra, HttpSession session){
        List<Estudiantes> listaEstudiantes = (List<Estudiantes>)studentService.listarEstudiantes(palabra);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        if(logueado.getTipo().equals("Coordinador")){
            Coordinadores c = coordinadorService.buscarCoordinador(logueado.getIdUsuario());
            listaEstudiantes = studentService.listarEstudiantesPrograma(c.getPrograma().getIdPrograma());
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiantes", listaEstudiantes);
        modelo.addAttribute("palabra", palabra);
        log.info("Ejecuntando el controlador listar estudiantes");
        return "Estudiantes/ListaEstudiantes";
    }
    
    @GetMapping("/RegistrarEstudiante")
    public String registarEstudiante(Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiante", new Estudiantes());
        modelo.addAttribute("programas", programService.listarProgramas(null));
        log.info("Ejecuntando el controlador registrar estudiante");
        return "Estudiantes/FormularioEstudiantes";
    }
    
    @PostMapping("/GuardarEstudiante")
    public String guardarEstudiante(@Valid @ModelAttribute Estudiantes estudiante, Model modelo, RedirectAttributes atributos, HttpSession session){
        estudiante.setEstado("Activo");
        estudiante.setTipo("Estudiante");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(estudiante.getPassword());
        estudiante.setPassword(password);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        try{
            studentService.guardarEstudiante(estudiante);
            if(estudiante.getIdUsuario() == 0){
                atributos.addFlashAttribute("success", "Estudiante Registrado Exitosamente");
            }else{
                if(estudiante.getIdUsuario() == logueado.getIdUsuario()){
                    Usuarios user = userService.buscarUsuario(estudiante.getIdUsuario());
                    session.setAttribute("usuario.session", user);
                    atributos.addFlashAttribute("success", "Datos Modificados Exitosamente");
                    return "redirect:/";
                }else{
                    atributos.addFlashAttribute("success", "Estudiante Modificado Exitosamente");
                }
            }
        }catch(Exception ex){
            String mensaje="";
            if(ex.getMessage().contains("ConstraintViolationException")){
                mensaje="La Cedula Ingresada Ya Existe";
            }else{
                mensaje= ex.getMessage();
            }
            modelo.addAttribute("usuario", logueado);
            modelo.addAttribute("danger", ""+mensaje);
            modelo.addAttribute("estudiante", estudiante);
            modelo.addAttribute("programas", programService.listarProgramas(null));
            return "Estudiantes/FormularioEstudiantes";
        }
        return "redirect:/Estudiantes";
    }
    
    @GetMapping("/EditarEstudiante/{idUsuario}")
    public String editarEstudiante(Estudiantes estudiante, Model modelo, HttpSession session){
        estudiante = studentService.buscarEstudiante(estudiante.getIdUsuario());
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Coordinador") || logueado.getTipo().equals("Encargado")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("programas", programService.listarProgramas(null));
        modelo.addAttribute("estudiante", estudiante);
        return "Estudiantes/FormularioEstudiantes";
    }
    
    @GetMapping("/EliminarEstudiante/{idUsuario}")
    public String eliminarEstudiante(Estudiantes estudiante, RedirectAttributes atributos){
        Estudiantes e = studentService.buscarEstudiante(estudiante.getIdUsuario());
        e.setEstado("Eliminado");
        studentService.guardarEstudiante(e);
        atributos.addFlashAttribute("warning", "Estudiante Eliminado");
        return "redirect:/Estudiantes";
    }
}
