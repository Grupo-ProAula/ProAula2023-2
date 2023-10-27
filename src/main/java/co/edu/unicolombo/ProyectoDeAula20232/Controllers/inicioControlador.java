
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Administradores;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Coordinadores;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Encargados;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Estudiantes;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IAdministradoresService;
import co.edu.unicolombo.ProyectoDeAula20232.Services.ICoordinadoresServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IEncargadoServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IEstudianteServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IProgramaServicios;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IUsuarioServicios;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class inicioControlador {
    
    @Autowired
    IUsuarioServicios userService;
    
    @Autowired
    IProgramaServicios programService;
    
    @Autowired
    IEstudianteServicios studentService;
    
    @Autowired
    IEncargadoServicios encargadoService;
    
    @Autowired
    ICoordinadoresServicios coordinadorService;
    
    @Autowired
    IAdministradoresService adminService;
    
    @GetMapping("/")
    public String inicio(Model modelo, HttpSession session){  
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        modelo.addAttribute("usuario", logueado);
        return "index";
    }
    
    @GetMapping("/Configuracion/{idUsuario}")
    public String configuracion(Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Estudiante")){
            Estudiantes estudiante = studentService.buscarEstudiante(logueado.getIdUsuario());
            modelo.addAttribute("usuario", logueado);
            modelo.addAttribute("programas", programService.listarProgramas(null));
            modelo.addAttribute("estudiante", estudiante);
            return "Estudiantes/FormularioEstudiantes";
        }else if(logueado.getTipo().equals("Coordinador")){
            Coordinadores coordinador = coordinadorService.buscarCoordinador(logueado.getIdUsuario());
            modelo.addAttribute("usuario", logueado);
            modelo.addAttribute("programas", programService.listarProgramasDisponibles(coordinador.getIdUsuario()));
            modelo.addAttribute("coordinador", coordinador);
            return "Coordinadores/FormularioCoordinadores";
        }else if(logueado.getTipo().equals("Encargado")){
            Encargados encargado = encargadoService.buscarEncargado(logueado.getIdUsuario());
            modelo.addAttribute("usuario", logueado);
            modelo.addAttribute("encargado", encargado);
            return "Encargados/FormularioEncargados";
        }else if(logueado.getTipo().equals("Administrador")){
            Administradores administrador = adminService.buscarAdministrador(logueado.getIdUsuario());
            modelo.addAttribute("usuario", logueado);
            modelo.addAttribute("administrador", administrador);
            return "Administradores/FormularioAdministradores";
        }
        return "index";
    }
    
}
