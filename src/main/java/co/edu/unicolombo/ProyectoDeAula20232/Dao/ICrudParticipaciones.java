
package co.edu.unicolombo.ProyectoDeAula20232.Dao;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Participaciones;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudParticipaciones extends JpaRepository<Participaciones, Integer>{
    
    @Query("SELECT p FROM Participaciones p WHERE p.estado = 'Activo'")
    public List<Participaciones> listarParticipacionesActivas();
    
    @Query("SELECT p FROM Participaciones p WHERE p.estado = 'Activo' AND p.estudiante.idUsuario = ?1")
    public List<Participaciones> listarParticipacionesEstudiante(int idEstudiante);
    
    @Query("SELECT p FROM Participaciones p WHERE p.estado = 'Activo' AND p.actividadProgramada.idActividadProgramada = ?1")
    public List<Participaciones> listarParticipacionesActividad(int idActividad);
    
    @Query("SELECT p FROM Participaciones p WHERE p.estado = 'Activo' AND (p.actividadProgramada.periodo LIKE %?1% OR p.actividadProgramada.actividad.nombre LIKE %?1%)")
    public List<Participaciones> buscarParticipacionesActivas(String palabra);
    
    @Query("SELECT p FROM Participaciones p WHERE p.estado = 'Activo' AND p.estudiante.idUsuario = ?1 AND (p.actividadProgramada.periodo LIKE %?2% OR p.actividadProgramada.actividad.nombre LIKE %?2%)")
    public List<Participaciones> buscarParticipacionesEstudiante(int idEstudiante, String palabra);
    
    @Query("SELECT p FROM Participaciones p WHERE p.estado = 'Activo' AND p.actividadProgramada.idActividadProgramada = ?1 AND (p.estudiante.cedula LIKE %?2% OR p.estudiante.nombre LIKE %?2% OR p.estudiante.apellidos LIKE %?2% OR p.estudiante.programa.nombre LIKE %?2% )") 
    public List<Participaciones> buscarParticipacionesActividad(int idActividad, String palabra);
    
    @Query("SELECT p FROM Participaciones p WHERE p.actividadProgramada.idActividadProgramada = ?2 AND p.estudiante.idUsuario = ?1")
    public Participaciones buscarParticipacion(int idEstudiante, int idActividad);
}
