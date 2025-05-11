package br.com.fiap.ecotrack.repository;

import br.com.fiap.ecotrack.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    
    List<Alerta> findByPontoColetaId(Long pontoColetaId);
    
    List<Alerta> findByMaterialId(Long materialId);
    
    List<Alerta> findByTipo(Alerta.TipoAlerta tipo);
    
    List<Alerta> findByResolvidoFalse();
    
    List<Alerta> findByLidoFalse();
    
    @Query("SELECT a FROM Alerta a " +
           "WHERE a.pontoColeta.id = :pontoColetaId " +
           "AND a.resolvido = false")
    List<Alerta> findAtivosByPontoColetaId(@Param("pontoColetaId") Long pontoColetaId);
    
    @Query("SELECT a FROM Alerta a " +
           "WHERE a.dataCriacao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY a.dataCriacao DESC")
    List<Alerta> findByPeriodo(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
}
