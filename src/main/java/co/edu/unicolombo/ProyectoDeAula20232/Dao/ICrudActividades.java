
package co.edu.unicolombo.ProyectoDeAula20232.Dao;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Actividades;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudActividades extends JpaRepository<Actividades, Integer>{
    
    @Query("SELECT a FROM Actividades a WHERE a.estado = 'Activo'")
    public List<Actividades> listarActividadesActivas();
    
    @Query("SELECT a FROM Actividades a WHERE a.estado = 'Activo' AND (a.nombre LIKE %?1% OR a.tipo LIKE %?1%)")
    public List<Actividades> buscarActividades(String palabra);
}
