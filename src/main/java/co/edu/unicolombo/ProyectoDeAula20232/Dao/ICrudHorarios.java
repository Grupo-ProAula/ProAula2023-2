
package co.edu.unicolombo.ProyectoDeAula20232.Dao;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Horarios;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudHorarios extends JpaRepository<Horarios, Integer>{
    
    @Query("SELECT h FROM Horarios h WHERE h.estado = 'Activo'")
    public List<Horarios> listarHorariosActivos();
    
    @Query("SELECT h FROM Horarios h WHERE h.estado = 'Activo' AND h.actividad.idActividadProgramada = ?1")
    public List<Horarios> listarHorariosActividad(int idActividad);
    
    @Query("SELECT h FROM Horarios h WHERE h.estado = 'Activo' AND h.dia = ?1 AND h.horaInicio = ?2 AND h.horaFin = ?3 AND h.actividad.idActividadProgramada = ?4 ")
    public Horarios verificarHorario(String dia, Date horaInicio, Date horaFin, int idActividad);
}
