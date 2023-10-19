/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.unicolombo.ProyectoDeAula20232.Controllers;
import co.edu.unicolombo.ProyectoDeAula20232.Models.Administradores;

import co.edu.unicolombo.ProyectoDeAula20232.Services.IProgramaServicios;
import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import co.edu.unicolombo.ProyectoDeAula20232.Services.IAdministradoresService;

@Controller
@Slf4j
public class AdministradoresControlador {
        
    @Autowired
    IAdministradoresService adminService;
    
    @GetMapping("/Administradores")
    public String listarAdministradores(Model modelo, @Param("palabra")String palabra){
        List<Administradores> listaAdministradores = (List<Administradores>)adminService.listarAdministradores(palabra);
        modelo.addAttribute("administradores", listaAdministradores);
        modelo.addAttribute("palabra", palabra);
        log.info("Ejecuntando el controlador listar administradores");
        return "Administradores/ListaAdministradores";
    }
    
    
    @GetMapping("/RegistrarAdministrador")
    public String RegistrarAdministradores(Model modelo){
        modelo.addAttribute("administrador", new Administradores());
        log.info("Ejecuntando el controlador registrar estudiante");
        return "Administradores/FormularioAdministradores";
    }
    
    @PostMapping("/GuardarAdministrador")
    public String guardarAdministradores(@Valid @ModelAttribute Administradores Administradores , Errors errores, Model modelo, RedirectAttributes atributos){
        Administradores.setEstado("Activo");
        Administradores.setTipo("Administradores");
        if(Administradores.getIdUsuario() == 0){
            adminService.guardarAdministrador(Administradores);
            atributos.addFlashAttribute("success", "Administrador Registrado Exitosamente");
        }else{
            adminService.guardarAdministrador(Administradores);
            atributos.addFlashAttribute("success", "Administrador Modificado Exitosamente");
        }
        return "redirect:/Administradores";
    }
    
    @GetMapping("/EditarAdministrador/{idUsuario}")
    public String editarAdministradores(Administradores administrador, Model modelo){
        administrador = adminService.buscarAdministrador(administrador.getIdUsuario());
        modelo.addAttribute("administrador", administrador);
        return "Administradores/FormularioAdministradores";
    }
    
    @GetMapping("/EliminarAdministrador/{idUsuario}")
    public String eliminaradministradores(Administradores administrador, RedirectAttributes atributos){
        Administradores a = adminService.buscarAdministrador(administrador.getIdUsuario());
        a.setEstado("Eliminado");
        adminService.guardarAdministrador(a);
        atributos.addFlashAttribute("warning", "Administrador Eliminado");
        return "redirect:/Administradores";
    }
}


