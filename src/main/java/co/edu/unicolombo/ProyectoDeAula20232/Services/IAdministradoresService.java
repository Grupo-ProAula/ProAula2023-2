/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.edu.unicolombo.ProyectoDeAula20232.Services;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Administradores;
import java.util.List;

public interface IAdministradoresService {
    
    public List<Administradores> listarAdministradores(String palabra);
    
    public void guardarAdministrador(Administradores admin);
    
    public void eliminarAdministrador(Administradores admin);
    
    public Administradores buscarAdministrador(Integer id);

}
