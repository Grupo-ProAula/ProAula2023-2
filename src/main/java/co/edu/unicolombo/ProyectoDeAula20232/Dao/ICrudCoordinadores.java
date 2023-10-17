
package co.edu.unicolombo.ProyectoDeAula20232.Dao;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Coordinadores;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudCoordinadores extends JpaRepository<Coordinadores, Integer>{
    
    @Query("SELECT c FROM Coordinadores c WHERE c.estado = 'Activo'")
    public List<Coordinadores> listarCoordinadoresActivos();
    
    @Query("SELECT c FROM Coordinadores c WHERE c.estado = 'Activo' AND (c.nombre LIKE %?1% OR c.apellidos LIKE %?1% OR c.cedula LIKE %?1%)")
    public List<Coordinadores> buscarCoordinadores(String palabra);
}
