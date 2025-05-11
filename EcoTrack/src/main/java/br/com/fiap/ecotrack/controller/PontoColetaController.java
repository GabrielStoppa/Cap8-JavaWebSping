package br.com.fiap.ecotrack.controller;

import br.com.fiap.ecotrack.dto.MaterialPontoDTO;
import br.com.fiap.ecotrack.dto.PontoColetaDTO;
import br.com.fiap.ecotrack.service.PontoColetaService;
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
@RequestMapping("/api/pontos-coleta")
@Tag(name = "Pontos de Coleta", description = "Endpoints para gerenciamento de pontos de coleta de resíduos")
public class PontoColetaController {

    @Autowired
    private PontoColetaService pontoColetaService;

    @GetMapping
    @Operation(summary = "Listar todos os pontos de coleta", description = "Retorna todos os pontos de coleta ativos")
    public ResponseEntity<List<PontoColetaDTO>> listarTodos() {
        return ResponseEntity.ok(pontoColetaService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ponto de coleta por ID", description = "Retorna um ponto de coleta pelo seu ID")
    public ResponseEntity<PontoColetaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pontoColetaService.buscarPorId(id));
    }

    @GetMapping("/material/{materialId}")
    @Operation(summary = "Buscar pontos de coleta por material", description = "Retorna os pontos de coleta que aceitam determinado material")
    public ResponseEntity<List<PontoColetaDTO>> buscarPorMaterial(@PathVariable Long materialId) {
        return ResponseEntity.ok(pontoColetaService.buscarPorMaterial(materialId));
    }

    @GetMapping("/cidade/{cidade}")
    @Operation(summary = "Buscar pontos de coleta por cidade", description = "Retorna os pontos de coleta de uma determinada cidade")
    public ResponseEntity<List<PontoColetaDTO>> buscarPorCidade(@PathVariable String cidade) {
        return ResponseEntity.ok(pontoColetaService.buscarPorCidade(cidade));
    }

    @GetMapping("/proximos")
    @Operation(summary = "Buscar pontos de coleta próximos", description = "Retorna os pontos de coleta próximos às coordenadas informadas")
    public ResponseEntity<List<PontoColetaDTO>> buscarProximos(
            @RequestParam String latitude,
            @RequestParam String longitude,
            @RequestParam(defaultValue = "5.0") double raioKm) {
        return ResponseEntity.ok(pontoColetaService.buscarProximos(latitude, longitude, raioKm));
    }

    @PostMapping
    @Operation(
        summary = "Criar novo ponto de coleta", 
        description = "Cria um novo ponto de coleta", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPRESA')")
    public ResponseEntity<PontoColetaDTO> criar(@Valid @RequestBody PontoColetaDTO pontoColetaDTO) {
        return new ResponseEntity<>(pontoColetaService.criar(pontoColetaDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar ponto de coleta", 
        description = "Atualiza um ponto de coleta existente", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPRESA')")
    public ResponseEntity<PontoColetaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PontoColetaDTO pontoColetaDTO) {
        return ResponseEntity.ok(pontoColetaService.atualizar(id, pontoColetaDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir ponto de coleta", 
        description = "Marca um ponto de coleta como inativo", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPRESA')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pontoColetaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{pontoId}/materiais")
    @Operation(
        summary = "Adicionar material ao ponto de coleta", 
        description = "Adiciona um material aceito pelo ponto de coleta", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPRESA', 'COLETOR')")
    public ResponseEntity<PontoColetaDTO> adicionarMaterial(
            @PathVariable Long pontoId,
            @Valid @RequestBody MaterialPontoDTO materialPontoDTO) {
        return ResponseEntity.ok(pontoColetaService.adicionarMaterial(pontoId, materialPontoDTO));
    }

    @PutMapping("/{pontoId}/materiais/{materialId}/nivel")
    @Operation(
        summary = "Atualizar nível de material", 
        description = "Atualiza o nível atual de um material no ponto de coleta", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'COLETOR')")
    public ResponseEntity<PontoColetaDTO> atualizarNivelMaterial(
            @PathVariable Long pontoId,
            @PathVariable Long materialId,
            @RequestParam Double novoNivel) {
        return ResponseEntity.ok(pontoColetaService.atualizarNivelMaterial(pontoId, materialId, novoNivel));
    }
}
