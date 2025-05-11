package br.com.fiap.ecotrack.repository;

import br.com.fiap.ecotrack.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    
    Optional<Material> findByNome(String nome);
    
    @Query("SELECT m FROM Material m " +
           "JOIN m.pontosColeta mp " +
           "WHERE mp.pontoColeta.id = :pontoColetaId")
    List<Material> findByPontoColetaId(@Param("pontoColetaId") Long pontoColetaId);
    
    @Query("SELECT DISTINCT m FROM Material m " +
           "JOIN m.registrosDescarte rd " +
           "WHERE rd.usuario.id = :usuarioId")
    List<Material> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}
