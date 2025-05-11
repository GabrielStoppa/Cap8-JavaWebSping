package br.com.fiap.ecotrack.repository;

import br.com.fiap.ecotrack.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    
    List<Notificacao> findByUsuarioId(Long usuarioId);
    
    List<Notificacao> findByUsuarioIdAndLidaFalse(Long usuarioId);
    
    List<Notificacao> findByTipo(Notificacao.TipoNotificacao tipo);
    
    @Query("SELECT n FROM Notificacao n " +
           "WHERE n.usuario.id = :usuarioId " +
           "AND n.dataEnvio BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY n.dataEnvio DESC")
    List<Notificacao> findByUsuarioIdAndPeriodo(
            @Param("usuarioId") Long usuarioId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim);
    
    @Query("SELECT n FROM Notificacao n " +
           "WHERE n.usuario.id = :usuarioId " +
           "AND n.tipo = :tipo " +
           "AND n.lida = false " +
           "ORDER BY n.dataEnvio DESC")
    List<Notificacao> findNaoLidasByUsuarioIdAndTipo(
            @Param("usuarioId") Long usuarioId,
            @Param("tipo") Notificacao.TipoNotificacao tipo);
}
