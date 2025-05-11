package br.com.fiap.ecotrack.dto;

import br.com.fiap.ecotrack.model.Notificacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoDTO {
    
    private Long id;
    
    private Long usuarioId;
    
    private String nomeUsuario;
    
    private String titulo;
    
    private String conteudo;
    
    private LocalDateTime dataEnvio;
    
    private boolean lida;
    
    private LocalDateTime dataLeitura;
    
    private Notificacao.TipoNotificacao tipo;
    
    private String linkAcao;
    
    private Long dadoReferenciaId;
    
    private String tipoReferencia;
    
    public static NotificacaoDTO fromEntity(Notificacao notificacao) {
        NotificacaoDTO dto = new NotificacaoDTO();
        dto.setId(notificacao.getId());
        
        if (notificacao.getUsuario() != null) {
            dto.setUsuarioId(notificacao.getUsuario().getId());
            dto.setNomeUsuario(notificacao.getUsuario().getNome());
        }
        
        dto.setTitulo(notificacao.getTitulo());
        dto.setConteudo(notificacao.getConteudo());
        dto.setDataEnvio(notificacao.getDataEnvio());
        dto.setLida(notificacao.isLida());
        dto.setDataLeitura(notificacao.getDataLeitura());
        dto.setTipo(notificacao.getTipo());
        dto.setLinkAcao(notificacao.getLinkAcao());
        dto.setDadoReferenciaId(notificacao.getDadoReferenciaId());
        dto.setTipoReferencia(notificacao.getTipoReferencia());
        
        return dto;
    }
}
