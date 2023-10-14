
package co.edu.unicolombo.ProyectoDeAula20232.Dao;

import co.edu.unicolombo.ProyectoDeAula20232.Models.ActividadesProgramadas;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudActividadesProgramadas extends JpaRepository<ActividadesProgramadas, Integer>{
    
    @Query("SELECT ap FROM ActividadesProgramadas ap WHERE ap.estado = 'Activo'")
    public List<ActividadesProgramadas> listarActividadesProgramadasActivas();
    
    //@Query("SELECT ap FROM ActividadesProgramadas ap INNER JOIN Actividades a WHERE ap.estado = 'Activo' AND ap.periodo LIKE %?1% OR a.nombre LIKE %?1%")
    @Query("SELECT ap FROM ActividadesProgramadas ap WHERE ap.estado = 'Activo' AND ap.periodo LIKE %?1% OR ap.actividad.nombre LIKE %?1%")
    public List<ActividadesProgramadas> buscarActividadesProgramadas(String palabra);
}
