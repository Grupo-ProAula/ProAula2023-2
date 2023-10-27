
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
   
    @Query("SELECT ap FROM ActividadesProgramadas ap WHERE ap.estado = 'Activo' AND (ap.periodo LIKE %?1% OR ap.actividad.nombre LIKE %?1%)")
    public List<ActividadesProgramadas> buscarActividadesProgramadas(String palabra);
    
    @Query("SELECT ap FROM ActividadesProgramadas ap WHERE ap.estado = 'Activo' AND ap.encargado.idUsuario = ?1")
    public List<ActividadesProgramadas> listarActividadesProgramadasEncargado(int idEncargado);
    
    @Query("SELECT ap FROM ActividadesProgramadas ap WHERE ap.estado = 'Activo' AND ap.encargado.idUsuario = ?1 AND (ap.periodo LIKE %?2% OR ap.actividad.nombre LIKE %?2%)")
    public List<ActividadesProgramadas> buscarActividadesProgramadasEncargado(int idEncargado, String palabra);
    
    @Query("SELECT ap FROM ActividadesProgramadas ap WHERE ap.estado = 'Activo' AND ap.idActividadProgramada NOT IN (SELECT p.actividadProgramada.idActividadProgramada FROM Participaciones p WHERE p.estado = 'Activo' AND p.estudiante.idUsuario = ?1)")
    public List<ActividadesProgramadas> listarActividadesProgramadasDisponibles(int idEstudiante);
    
    @Query("SELECT ap FROM ActividadesProgramadas ap WHERE ap.estado = 'Activo' AND (ap.periodo LIKE %?2% OR ap.actividad.nombre LIKE %?2%) AND ap.idActividadProgramada NOT IN (SELECT p.actividadProgramada.idActividadProgramada FROM Participaciones p WHERE p.estado = 'Activo' AND p.estudiante.idUsuario = ?1)")
    public List<ActividadesProgramadas> buscarActividadesProgramadasDisponibles(int idEstudiante, String palabra);
            
    @Query("SELECT ap FROM ActividadesProgramadas ap WHERE ap.estado = 'Activo' AND ap.idActividadProgramada IN (SELECT p.actividadProgramada.idActividadProgramada FROM Participaciones p WHERE p.estado = 'Activo' AND p.estudiante.idUsuario = ?1)")
    public List<ActividadesProgramadas> listarActividadesProgramadasEstudiante(int idEstudiante);
    
    @Query("SELECT ap FROM ActividadesProgramadas ap WHERE ap.estado = 'Activo' AND (ap.periodo LIKE %?2% OR ap.actividad.nombre LIKE %?2%) AND ap.idActividadProgramada IN (SELECT p.actividadProgramada.idActividadProgramada FROM Participaciones p WHERE p.estado = 'Activo' AND p.estudiante.idUsuario = ?1)")
    public List<ActividadesProgramadas> buscarActividadesProgramadasEstudiante(int idEstudiante, String palabra);
}
