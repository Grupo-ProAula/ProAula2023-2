
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequestMapping("/Estudiantes")
public class EstudianteControlador {
    
    @Autowired
    ICoordinadoresServicios coordinadorService;
    
    @Autowired
    IUsuarioServicios userService;
    
    @Autowired
    IEstudianteServicios studentService;
    
    @Autowired
    IProgramaServicios programService;
    
    @GetMapping("")
    public String listarEstudiantes(Model modelo, @Param("palabra")String palabra, HttpSession session){
        List<Estudiantes> listaEstudiantes = (List<Estudiantes>)studentService.listarEstudiantes(palabra);
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Estudiante") || logueado.getTipo().equals("Encargado")){
            return "redirect:/";
        }
        if(logueado.getTipo().equals("Coordinador")){
            Coordinadores c = coordinadorService.buscarCoordinador(logueado.getIdUsuario());
            listaEstudiantes = studentService.listarEstudiantesPrograma(c.getPrograma().getIdPrograma(), palabra);
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("estudiantes", listaEstudiantes);
        modelo.addAttribute("palabra", palabra);
        return "Estudiantes/ListaEstudiantes";
    }
    
    @GetMapping("/Add")
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
        return "Estudiantes/FormularioEstudiantes";
    }
    
    @PostMapping("/Save")
    public String guardarEstudiante(@Valid @ModelAttribute Estudiantes estudiante, Model modelo, RedirectAttributes atributos, HttpSession session){
        estudiante.setEstado("Activo");
        estudiante.setTipo("Estudiante");
        String defaultPassword = "1234";
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        try{
            if(estudiante.getSemestre() <= 0){
                modelo.addAttribute("usuario", logueado);
                modelo.addAttribute("estudiante", estudiante);
                modelo.addAttribute("programas", programService.listarProgramas(null));
                modelo.addAttribute("danger", "El semestre no puede ser menor o igual a 0");
                return "Estudiantes/FormularioEstudiantes";
            }else if(estudiante.getSemestre() > estudiante.getPrograma().getSemestresTotales()){
                modelo.addAttribute("usuario", logueado);
                modelo.addAttribute("estudiante", estudiante);
                modelo.addAttribute("programas", programService.listarProgramas(null));
                modelo.addAttribute("danger", "El semestre cursado no puede ser mayor a los semestres totales del programa");
                return "Estudiantes/FormularioEstudiantes";
            }else{
                int id = estudiante.getIdUsuario();
                if(id == 0){
                    if(!logueado.getTipo().equals("Administrador")){
                        atributos.addFlashAttribute("danger", "Accion No Permitida");
                        return "redirect:/";
                    }
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    String password = encoder.encode(defaultPassword);
                    estudiante.setPassword(password);
                    studentService.guardarEstudiante(estudiante);
                    atributos.addFlashAttribute("success", "Estudiante Registrado Exitosamente");
                }else{
                    if(estudiante.getIdUsuario() == logueado.getIdUsuario()){
                        if(!logueado.getTipo().equals("Estudiante")){
                            atributos.addFlashAttribute("danger", "Accion No Permitida");
                            return "redirect:/";
                        }
                        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                        String password = encoder.encode(estudiante.getPassword());
                        estudiante.setPassword(password);
                        studentService.guardarEstudiante(estudiante);
                        Usuarios user = userService.buscarUsuario(estudiante.getIdUsuario());
                        session.setAttribute("usuario.session", user);
                        atributos.addFlashAttribute("success", "Datos Modificados Exitosamente");
                        return "redirect:/";
                    }else{
                        if(!logueado.getTipo().equals("Administrador")){
                            atributos.addFlashAttribute("danger", "Accion No Permitida");
                            return "redirect:/";
                        }
                        Estudiantes e = studentService.buscarEstudiante(id);
                        e.setCedula(estudiante.getCedula());
                        e.setCodigoEstudiantil(estudiante.getCodigoEstudiantil());
                        e.setNombre(estudiante.getNombre());
                        e.setApellidos(estudiante.getApellidos());
                        e.setCorreo(estudiante.getCorreo());
                        e.setTelefono(estudiante.getTelefono());
                        e.setPrograma(estudiante.getPrograma());
                        e.setSemestre(estudiante.getSemestre());
                        studentService.guardarEstudiante(e);
                        atributos.addFlashAttribute("success", "Estudiante Modificado Exitosamente");
                    }
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
    
    @GetMapping("/Edit/{idUsuario}")
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
    
    @GetMapping("/Delete/{idUsuario}")
    public String eliminarEstudiante(Estudiantes estudiante, RedirectAttributes atributos){
        Estudiantes e = studentService.buscarEstudiante(estudiante.getIdUsuario());
        e.setEstado("Eliminado");
        studentService.guardarEstudiante(e);
        atributos.addFlashAttribute("warning", "Estudiante Eliminado");
        return "redirect:/Estudiantes";
    }
}
