
package co.edu.unicolombo.ProyectoDeAula20232.Dao;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Encargados;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudEncargados extends JpaRepository<Encargados, Integer>{
    
    @Query("SELECT e FROM Encargados e WHERE e.estado = 'Activo'")
    public List<Encargados> listarEncargadosActivos();
    
    @Query("SELECT e FROM Encargados e WHERE e.estado = 'Activo' AND (e.nombre LIKE %?1% OR e.apellidos LIKE %?1% OR e.cedula LIKE %?1%)")
    public List<Encargados> buscarEncargados(String palabra);
}
