/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Administradores;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;

import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IAdministradoresService;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IUsuarioServicios;
import javax.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/Administradores")
public class AdministradoresControlador {
    
    @Autowired
    IUsuarioServicios userService;
    
    @Autowired
    IAdministradoresService adminService;
    
    @GetMapping("")
    public String listarAdministradores(Model modelo, @Param("palabra")String palabra, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        List<Administradores> listaAdministradores = (List<Administradores>)adminService.listarAdministradores(palabra);
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("administradores", listaAdministradores);
        modelo.addAttribute("palabra", palabra);
        log.info("Ejecuntando el controlador listar administradores");
        return "Administradores/ListaAdministradores";
    }
    
    
    @GetMapping("/Add")
    public String RegistrarAdministradores(Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(!logueado.getTipo().equals("Administrador")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        modelo.addAttribute("administrador", new Administradores());
        log.info("Ejecuntando el controlador registrar estudiante");
        return "Administradores/FormularioAdministradores";
    }
    
    @PostMapping("/Save")
    public String guardarAdministradores(@Valid @ModelAttribute Administradores administrador , Model modelo, RedirectAttributes atributos, HttpSession session){
        administrador.setEstado("Activo");
        administrador.setTipo("Administrador");
        String defaultPassword = "1234";
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        try{
            int id = administrador.getIdUsuario();
            if(id == 0){
                if(!logueado.getTipo().equals("Administrador")){
                    atributos.addFlashAttribute("danger", "Accion No Permitida");
                    return "redirect:/";
                }
                BCryptPasswordEncoder encoder1 = new BCryptPasswordEncoder();
                String password1 = encoder1.encode(defaultPassword);
                administrador.setPassword(password1);
                adminService.guardarAdministrador(administrador);
                atributos.addFlashAttribute("success", "Administrador Registrado Exitosamente");
                return "redirect:/Administradores";
            }else{
                if(administrador.getIdUsuario() == logueado.getIdUsuario()){
                    if(!logueado.getTipo().equals("Administrador")){
                        atributos.addFlashAttribute("danger", "Accion No Permitida");
                        return "redirect:/";
                    }
                    BCryptPasswordEncoder encoder2 = new BCryptPasswordEncoder();
                    String password2 = encoder2.encode(administrador.getPassword());
                    administrador.setPassword(password2);
                    adminService.guardarAdministrador(administrador);                    
                    Usuarios user = userService.buscarUsuario(administrador.getIdUsuario());
                    session.setAttribute("usuario.session", user);
                    atributos.addFlashAttribute("success", "Datos Modificados Exitosamente");
                    return "redirect:/";
                }else{
                    if(!logueado.getTipo().equals("Administrador")){
                        atributos.addFlashAttribute("danger", "Accion No Permitida");
                        return "redirect:/";
                    }
                    Administradores a = adminService.buscarAdministrador(id);
                    a.setCedula(a.getCedula());
                    a.setNombre(a.getNombre());
                    a.setApellidos(a.getApellidos());
                    a.setCorreo(a.getCorreo());
                    a.setTelefono(a.getTelefono());
                    a.setCargo(a.getCargo());
                    adminService.guardarAdministrador(a);
                    atributos.addFlashAttribute("success", "Administrador Modificado Exitosamente");
                    return "redirect:/Administradores";
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
            modelo.addAttribute("administrador", administrador);
            return "Administradores/FormularioAdministradores";
        }
    }
    
    @GetMapping("/Edit/{idUsuario}")
    public String editarAdministradores(Administradores administrador, Model modelo, HttpSession session){
        Usuarios logueado = (Usuarios) session.getAttribute("usuario.session");
        if(logueado == null){
            return "redirect:/login";
        }
        if(logueado.getTipo().equals("Encargado") || logueado.getTipo().equals("Coordinador") || logueado.getTipo().equals("Estudiante")){
            return "redirect:/";
        }
        modelo.addAttribute("usuario", logueado);
        administrador = adminService.buscarAdministrador(administrador.getIdUsuario());
        modelo.addAttribute("administrador", administrador);
        return "Administradores/FormularioAdministradores";
    }
    
    @GetMapping("/Delete/{idUsuario}")
    public String eliminaradministradores(Administradores administrador, RedirectAttributes atributos){
        Administradores a = adminService.buscarAdministrador(administrador.getIdUsuario());
        a.setEstado("Eliminado");
        adminService.guardarAdministrador(a);
        atributos.addFlashAttribute("warning", "Administrador Eliminado");
        return "redirect:/Administradores";
    }
}


