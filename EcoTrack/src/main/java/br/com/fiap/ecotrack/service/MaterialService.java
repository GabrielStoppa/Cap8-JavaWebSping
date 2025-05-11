package br.com.fiap.ecotrack.service;

import br.com.fiap.ecotrack.dto.MaterialDTO;
import br.com.fiap.ecotrack.exception.AppException;
import br.com.fiap.ecotrack.model.Material;
import br.com.fiap.ecotrack.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public List<MaterialDTO> listarTodos() {
        return materialRepository.findAll().stream()
                .map(MaterialDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public MaterialDTO buscarPorId(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new AppException("Material não encontrado", HttpStatus.NOT_FOUND));
        return MaterialDTO.fromEntity(material);
    }

    public List<MaterialDTO> buscarPorPontoColeta(Long pontoColetaId) {
        return materialRepository.findByPontoColetaId(pontoColetaId).stream()
                .map(MaterialDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public MaterialDTO criar(MaterialDTO dto) {
        // Verificar se já existe material com o mesmo nome
        if (materialRepository.findByNome(dto.getNome()).isPresent()) {
            throw new AppException("Já existe um material com este nome", HttpStatus.BAD_REQUEST);
        }

        Material material = new Material();
        material.setNome(dto.getNome());
        material.setDescricao(dto.getDescricao());
        material.setIcone(dto.getIcone());
        material.setCor(dto.getCor());
        material.setDicasDescarte(dto.getDicasDescarte());
        material.setLimiteAlerta(dto.getLimiteAlerta());

        Material saved = materialRepository.save(material);
        return MaterialDTO.fromEntity(saved);
    }

    @Transactional
    public MaterialDTO atualizar(Long id, MaterialDTO dto) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new AppException("Material não encontrado", HttpStatus.NOT_FOUND));

        // Verificar se já existe outro material com o mesmo nome
        materialRepository.findByNome(dto.getNome())
                .ifPresent(m -> {
                    if (!m.getId().equals(id)) {
                        throw new AppException("Já existe outro material com este nome", HttpStatus.BAD_REQUEST);
                    }
                });

        material.setNome(dto.getNome());
        material.setDescricao(dto.getDescricao());
        material.setIcone(dto.getIcone());
        material.setCor(dto.getCor());
        material.setDicasDescarte(dto.getDicasDescarte());
        material.setLimiteAlerta(dto.getLimiteAlerta());

        Material saved = materialRepository.save(material);
        return MaterialDTO.fromEntity(saved);
    }

    @Transactional
    public void excluir(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new AppException("Material não encontrado", HttpStatus.NOT_FOUND));

        // Verificar se o material está sendo usado antes de excluir
        if (!material.getPontosColeta().isEmpty() || !material.getRegistrosDescarte().isEmpty()) {
            throw new AppException("Material não pode ser excluído porque está sendo usado", HttpStatus.BAD_REQUEST);
        }

        materialRepository.delete(material);
    }
}
