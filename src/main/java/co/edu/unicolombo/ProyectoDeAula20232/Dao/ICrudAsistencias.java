
package co.edu.unicolombo.ProyectoDeAula20232.Dao;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Asistencias;
import java.sql.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudAsistencias extends JpaRepository<Asistencias, Integer>{
    
    @Query("SELECT a FROM Asistencias a WHERE a.estado = 'Activo'")
    public List<Asistencias> listarAsistenciasActivas();
    
    @Query("SELECT a FROM Asistencias a WHERE a.estado = 'Activo' AND a.participacion.estudiante.idUsuario = ?1")
    public List<Asistencias> listarAsistenciasEstudiante(int idEstudiante);
    
    @Query("SELECT a FROM Asistencias a WHERE a.estado = 'Activo' AND a.participacion.actividadProgramada.idActividadProgramada = ?1")
    public List<Asistencias> listarAsistenciasActividad(int idActividad);
    
    @Query("SELECT a FROM Asistencias a WHERE a.estado = 'Activo' AND a.participacion.actividadProgramada.idActividadProgramada = ?1 AND a.participacion.estudiante.idUsuario = ?2 AND DATE(a.fechaAsistencia) = ?3")
    public Asistencias verificarAsistencia(int idActividad, int idEstudiante, Date fecha);
}
