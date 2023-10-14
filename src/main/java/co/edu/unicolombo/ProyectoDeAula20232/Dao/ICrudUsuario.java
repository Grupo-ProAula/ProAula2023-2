
package co.edu.unicolombo.ProyectoDeAula20232.Dao;

import co.edu.unicolombo.ProyectoDeAula20232.Models.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICrudUsuario extends JpaRepository<Usuarios, Integer>{
    
    @Query("SELECT u FROM Usuarios u WHERE u.cedula = :cedula AND u.estado='Activo'")
    public Usuarios getUsuarioByCedula(@Param("cedula")String cedula);
}
