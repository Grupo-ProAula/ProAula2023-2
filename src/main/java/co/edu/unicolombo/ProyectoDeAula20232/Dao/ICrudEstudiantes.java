
package co.edu.unicolombo.ProyectoDeAula20232.Dao;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Estudiantes;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudEstudiantes extends JpaRepository<Estudiantes, Integer>{
    
    @Query("SELECT e FROM Estudiantes e WHERE e.estado = 'Activo'")
    public List<Estudiantes> listarEstudiantesActivos();
    
    @Query("SELECT e FROM Estudiantes e WHERE e.estado = 'Activo' AND (e.nombre LIKE %?1% OR e.apellidos LIKE %?1% OR e.codigoEstudiantil LIKE %?1% OR e.cedula LIKE %?1%)")
    public List<Estudiantes> buscarEstudiantes(String palabra);
    
    @Query("SELECT e FROM Estudiantes e WHERE e.programa.idPrograma = ?1")
    public List<Estudiantes> listarEstudiantesPrograma(int idPrograma);
}
