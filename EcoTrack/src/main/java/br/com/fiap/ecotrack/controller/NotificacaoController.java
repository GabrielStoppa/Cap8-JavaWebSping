package br.com.fiap.ecotrack.controller;

import br.com.fiap.ecotrack.dto.NotificacaoDTO;
import br.com.fiap.ecotrack.model.Notificacao;
import br.com.fiap.ecotrack.service.NotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
@Tag(name = "Notificações", description = "Endpoints para gerenciamento de notificações aos usuários")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    @GetMapping("/usuario/{usuarioId}")
    @Operation(
        summary = "Listar notificações por usuário", 
        description = "Retorna todas as notificações de um determinado usuário", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#usuarioId)")
    public ResponseEntity<List<NotificacaoDTO>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacaoService.listarPorUsuario(usuarioId));
    }

    @GetMapping("/usuario/{usuarioId}/nao-lidas")
    @Operation(
        summary = "Listar notificações não lidas por usuário", 
        description = "Retorna todas as notificações não lidas de um determinado usuário", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCurrentUser(#usuarioId)")
    public ResponseEntity<List<NotificacaoDTO>> listarNaoLidasPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(notificacaoService.listarNaoLidasPorUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar notificação por ID", 
        description = "Retorna uma notificação pelo seu ID", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isNotificacaoUsuario(#id)")
    public ResponseEntity<NotificacaoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacaoService.buscarPorId(id));
    }

    @PutMapping("/{id}/ler")
    @Operation(
        summary = "Marcar notificação como lida", 
        description = "Marca uma notificação como lida", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN') or @securityService.isNotificacaoUsuario(#id)")
    public ResponseEntity<NotificacaoDTO> marcarComoLida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacaoService.marcarComoLida(id));
    }

    @PostMapping("/dica-descarte")
    @Operation(
        summary = "Enviar dica de descarte", 
        description = "Envia uma notificação com dica de descarte correto para um usuário", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR')")
    public ResponseEntity<Void> enviarDicaDescarte(
            @RequestParam Long usuarioId,
            @RequestParam Long materialId) {
        notificacaoService.enviarNotificacaoDicaDescarte(usuarioId, materialId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/lote")
    @Operation(
        summary = "Enviar notificações em lote", 
        description = "Envia notificações em lote para vários usuários", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> enviarNotificacoesLote(
            @RequestParam List<Long> usuariosIds,
            @RequestParam String titulo,
            @RequestParam String conteudo,
            @RequestParam Notificacao.TipoNotificacao tipo) {
        notificacaoService.enviarNotificacoesLote(usuariosIds, titulo, conteudo, tipo);
        return ResponseEntity.ok().build();
    }
}
