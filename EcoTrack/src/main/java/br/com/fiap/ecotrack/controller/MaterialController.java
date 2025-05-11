package br.com.fiap.ecotrack.controller;

import br.com.fiap.ecotrack.dto.MaterialDTO;
import br.com.fiap.ecotrack.service.MaterialService;
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
@RequestMapping("/api/materiais")
@Tag(name = "Materiais", description = "Endpoints para gerenciamento de materiais recicláveis")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    @Operation(summary = "Listar todos os materiais", description = "Retorna todos os materiais cadastrados")
    public ResponseEntity<List<MaterialDTO>> listarTodos() {
        return ResponseEntity.ok(materialService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar material por ID", description = "Retorna um material pelo seu ID")
    public ResponseEntity<MaterialDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.buscarPorId(id));
    }

    @GetMapping("/ponto-coleta/{pontoColetaId}")
    @Operation(summary = "Buscar materiais por ponto de coleta", description = "Retorna os materiais aceitos em um determinado ponto de coleta")
    public ResponseEntity<List<MaterialDTO>> buscarPorPontoColeta(@PathVariable Long pontoColetaId) {
        return ResponseEntity.ok(materialService.buscarPorPontoColeta(pontoColetaId));
    }

    @PostMapping
    @Operation(
        summary = "Criar novo material", 
        description = "Cadastra um novo material reciclável", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaterialDTO> criar(@Valid @RequestBody MaterialDTO materialDTO) {
        return new ResponseEntity<>(materialService.criar(materialDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar material", 
        description = "Atualiza um material existente", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MaterialDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MaterialDTO materialDTO) {
        return ResponseEntity.ok(materialService.atualizar(id, materialDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Excluir material", 
        description = "Exclui um material do sistema", 
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        materialService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
