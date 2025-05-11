package br.com.fiap.ecotrack.repository;

import br.com.fiap.ecotrack.model.RegistroDescarte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RegistroDescarteRepository extends JpaRepository<RegistroDescarte, Long> {
    
    List<RegistroDescarte> findByUsuarioId(Long usuarioId);
    
    List<RegistroDescarte> findByPontoColetaId(Long pontoColetaId);
    
    List<RegistroDescarte> findByMaterialId(Long materialId);
    
    @Query("SELECT rd FROM RegistroDescarte rd " +
           "WHERE rd.pontoColeta.id = :pontoColetaId " +
           "AND rd.material.id = :materialId")
    List<RegistroDescarte> findByPontoColetaIdAndMaterialId(
            @Param("pontoColetaId") Long pontoColetaId,
            @Param("materialId") Long materialId);
    
    @Query("SELECT rd FROM RegistroDescarte rd " +
           "WHERE rd.dataDescarte BETWEEN :dataInicio AND :dataFim")
    List<RegistroDescarte> findByPeriodo(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
    
    @Query("SELECT rd FROM RegistroDescarte rd " +
           "WHERE rd.pontoColeta.id = :pontoColetaId " +
           "AND rd.dataDescarte BETWEEN :dataInicio AND :dataFim")
    List<RegistroDescarte> findByPontoColetaIdAndPeriodo(
            @Param("pontoColetaId") Long pontoColetaId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
    
    @Query("SELECT SUM(rd.quantidade) FROM RegistroDescarte rd " +
           "WHERE rd.material.id = :materialId " +
           "AND rd.pontoColeta.id = :pontoColetaId " +
           "AND rd.validado = true")
    Double sumQuantidadeByMaterialIdAndPontoColetaId(
            @Param("materialId") Long materialId,
            @Param("pontoColetaId") Long pontoColetaId);
}
