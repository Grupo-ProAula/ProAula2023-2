
package co.edu.unicolombo.ProyectoDeAula20232.Dao;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Programas;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudProgramas extends JpaRepository<Programas, Integer>{
    
    @Query("SELECT p FROM Programas p WHERE p.estado = 'Activo'")
    public List<Programas> listarProgramasActivos();
    
    @Query("SELECT p FROM Programas p WHERE p.estado = 'Activo' AND p.nombre LIKE %?1%")
    public List<Programas> buscarProgramas(String palabra);
    
    @Query("SELECT p FROM Programas p WHERE p.idPrograma NOT IN (SELECT c.programa.idPrograma FROM Coordinadores c WHERE c.estado = 'Activo')")
    public List<Programas> listarProgramasDisponibles();
    
    @Query("SELECT p FROM Programas p WHERE p.idPrograma NOT IN (SELECT c.programa.idPrograma FROM Coordinadores c WHERE c.estado = 'Activo' AND c.idUsuario <> ?1)")
    public List<Programas> listarProgramasDisponibles(int id);
}
