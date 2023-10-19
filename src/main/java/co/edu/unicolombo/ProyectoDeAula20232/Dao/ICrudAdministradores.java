package co.edu.unicolombo.ProyectoDeAula20232.Dao;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Administradores;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudAdministradores extends JpaRepository<Administradores, Integer>{
    
    @Query("SELECT a FROM Administradores a WHERE a.estado = 'Activo'")
    public List<Administradores> listarAdministradoresActivos();
    
    @Query("SELECT a FROM Administradores a WHERE a.estado = 'Activo' AND (a.nombre LIKE %?1% OR a.apellidos LIKE %?1% OR a.cedula LIKE %?1%)")
    public List<Administradores> buscarAdministradores(String palabra);
}

