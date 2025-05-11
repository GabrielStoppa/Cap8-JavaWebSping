package br.com.fiap.ecotrack.controller;

import br.com.fiap.ecotrack.dto.RegistroDescarteDTO;
import br.com.fiap.ecotrack.service.RegistroDescarteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registros-descarte")
@Tag(name = "Registros de Descarte", description = "Endpoints para gerenciamento de registros de descarte de resíduos")
public class RegistroDescarteController {

    @Autowired
    private RegistroDescarteService registroDescarteService;

    @GetMapping
    @Operation(
        summary = "Listar todos os registros de descarte", 
        description = "Retorna todos os registros de descarte cadastrados", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR')")
    public ResponseEntity<List<RegistroDescarteDTO>> listarTodos() {
        return ResponseEntity.ok(registroDescarteService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar registro de descarte por ID", 
        description = "Retorna um registro de descarte pelo seu ID", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR', 'CIDADAO', 'EMPRESA')")
    public ResponseEntity<RegistroDescarteDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(registroDescarteService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(
        summary = "Buscar registros de descarte por usuário", 
        description = "Retorna os registros de descarte de um determinado usuário", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR') or @securityService.isCurrentUser(#usuarioId)")
    public ResponseEntity<List<RegistroDescarteDTO>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(registroDescarteService.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/ponto-coleta/{pontoColetaId}")
    @Operation(
        summary = "Buscar registros de descarte por ponto de coleta", 
        description = "Retorna os registros de descarte de um determinado ponto de coleta", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR', 'EMPRESA')")
    public ResponseEntity<List<RegistroDescarteDTO>> buscarPorPontoColeta(@PathVariable Long pontoColetaId) {
        return ResponseEntity.ok(registroDescarteService.buscarPorPontoColeta(pontoColetaId));
    }

    @GetMapping("/material/{materialId}")
    @Operation(
        summary = "Buscar registros de descarte por material", 
        description = "Retorna os registros de descarte de um determinado material", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR')")
    public ResponseEntity<List<RegistroDescarteDTO>> buscarPorMaterial(@PathVariable Long materialId) {
        return ResponseEntity.ok(registroDescarteService.buscarPorMaterial(materialId));
    }

    @PostMapping
    @Operation(
        summary = "Registrar descarte", 
        description = "Registra um novo descarte de resíduos", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'CIDADAO', 'EMPRESA')")
    public ResponseEntity<RegistroDescarteDTO> registrarDescarte(@Valid @RequestBody RegistroDescarteDTO registroDescarteDTO) {
        return new ResponseEntity<>(registroDescarteService.registrarDescarte(registroDescarteDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/validar")
    @Operation(
        summary = "Validar descarte", 
        description = "Valida um registro de descarte existente", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR')")
    public ResponseEntity<RegistroDescarteDTO> validarDescarte(
            @PathVariable Long id,
            @RequestParam Long usuarioValidacaoId) {
        return ResponseEntity.ok(registroDescarteService.validarDescarte(id, usuarioValidacaoId));
    }
}
