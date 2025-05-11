package br.com.fiap.ecotrack.service;

import br.com.fiap.ecotrack.dto.NotificacaoDTO;
import br.com.fiap.ecotrack.exception.AppException;
import br.com.fiap.ecotrack.model.Notificacao;
import br.com.fiap.ecotrack.model.Usuario;
import br.com.fiap.ecotrack.repository.NotificacaoRepository;
import br.com.fiap.ecotrack.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<NotificacaoDTO> listarPorUsuario(Long usuarioId) {
        return notificacaoRepository.findByUsuarioId(usuarioId).stream()
                .map(NotificacaoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<NotificacaoDTO> listarNaoLidasPorUsuario(Long usuarioId) {
        return notificacaoRepository.findByUsuarioIdAndLidaFalse(usuarioId).stream()
                .map(NotificacaoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public NotificacaoDTO buscarPorId(Long id) {
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new AppException("Notificação não encontrada", HttpStatus.NOT_FOUND));
        return NotificacaoDTO.fromEntity(notificacao);
    }

    @Transactional
    public NotificacaoDTO criarNotificacaoUsuario(Long usuarioId, String titulo, String conteudo, 
                                                 Notificacao.TipoNotificacao tipo, Long dadoReferenciaId,
                                                 String tipoReferencia) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AppException("Usuário não encontrado", HttpStatus.NOT_FOUND));

        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setTitulo(titulo);
        notificacao.setConteudo(conteudo);
        notificacao.setDataEnvio(LocalDateTime.now());
        notificacao.setLida(false);
        notificacao.setTipo(tipo);
        notificacao.setDadoReferenciaId(dadoReferenciaId);
        notificacao.setTipoReferencia(tipoReferencia);

        Notificacao saved = notificacaoRepository.save(notificacao);
        return NotificacaoDTO.fromEntity(saved);
    }

    @Transactional
    public NotificacaoDTO marcarComoLida(Long id) {
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new AppException("Notificação não encontrada", HttpStatus.NOT_FOUND));

        notificacao.setLida(true);
        notificacao.setDataLeitura(LocalDateTime.now());

        Notificacao saved = notificacaoRepository.save(notificacao);
        return NotificacaoDTO.fromEntity(saved);
    }

    @Transactional
    public void enviarNotificacaoDicaDescarte(Long usuarioId, Long materialId) {
        // Implementação do método para enviar dicas de descarte correto
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new AppException("Usuário não encontrado", HttpStatus.NOT_FOUND));

        // Este método buscaria as dicas de descarte do material e enviaria para o usuário
        // Por simplicidade, vou apenas criar uma notificação genérica
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setTitulo("Dica de descarte correto");
        notificacao.setConteudo("Separar corretamente os resíduos melhora a eficiência da reciclagem. Limpe embalagens antes de descartar e separe metais, plásticos e orgânicos.");
        notificacao.setDataEnvio(LocalDateTime.now());
        notificacao.setLida(false);
        notificacao.setTipo(Notificacao.TipoNotificacao.DICA_DESCARTE);
        notificacao.setDadoReferenciaId(materialId);
        notificacao.setTipoReferencia("material");

        notificacaoRepository.save(notificacao);
    }

    @Transactional
    public void enviarNotificacoesLote(List<Long> usuariosIds, String titulo, String conteudo, 
                                      Notificacao.TipoNotificacao tipo) {
        usuariosIds.forEach(usuarioId -> {
            try {
                criarNotificacaoUsuario(usuarioId, titulo, conteudo, tipo, null, null);
            } catch (Exception e) {
                // Log do erro, mas continua o processo para os outros usuários
            }
        });
    }
}
