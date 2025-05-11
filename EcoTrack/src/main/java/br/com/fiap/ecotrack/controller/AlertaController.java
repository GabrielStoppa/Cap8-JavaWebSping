package br.com.fiap.ecotrack.controller;

import br.com.fiap.ecotrack.dto.AlertaDTO;
import br.com.fiap.ecotrack.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@Tag(name = "Alertas", description = "Endpoints para gerenciamento de alertas automáticos para coleta de materiais recicláveis")
public class AlertaController {

    @Autowired
    private AlertaService alertaService;

    @GetMapping
    @Operation(
        summary = "Listar todos os alertas", 
        description = "Retorna todos os alertas cadastrados", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR', 'EMPRESA')")
    public ResponseEntity<List<AlertaDTO>> listarTodos() {
        return ResponseEntity.ok(alertaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar alerta por ID", 
        description = "Retorna um alerta pelo seu ID", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR', 'EMPRESA')")
    public ResponseEntity<AlertaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.buscarPorId(id));
    }

    @GetMapping("/ponto-coleta/{pontoColetaId}")
    @Operation(
        summary = "Buscar alertas por ponto de coleta", 
        description = "Retorna os alertas de um determinado ponto de coleta", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR', 'EMPRESA')")
    public ResponseEntity<List<AlertaDTO>> buscarPorPontoColeta(@PathVariable Long pontoColetaId) {
        return ResponseEntity.ok(alertaService.buscarPorPontoColeta(pontoColetaId));
    }

    @GetMapping("/ativos")
    @Operation(
        summary = "Buscar alertas ativos", 
        description = "Retorna todos os alertas não resolvidos", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR')")
    public ResponseEntity<List<AlertaDTO>> buscarAlertasAtivos() {
        return ResponseEntity.ok(alertaService.buscarAlertasAtivos());
    }

    @GetMapping("/nao-lidos")
    @Operation(
        summary = "Buscar alertas não lidos", 
        description = "Retorna todos os alertas não lidos", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR')")
    public ResponseEntity<List<AlertaDTO>> buscarAlertasNaoLidos() {
        return ResponseEntity.ok(alertaService.buscarAlertasNaoLidos());
    }

    @PutMapping("/{id}/ler")
    @Operation(
        summary = "Marcar alerta como lido", 
        description = "Marca um alerta como lido", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR')")
    public ResponseEntity<AlertaDTO> marcarComoLido(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.marcarComoLido(id));
    }

    @PutMapping("/{id}/resolver")
    @Operation(
        summary = "Marcar alerta como resolvido", 
        description = "Marca um alerta como resolvido", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR')")
    public ResponseEntity<AlertaDTO> marcarComoResolvido(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.marcarComoResolvido(id));
    }
}
