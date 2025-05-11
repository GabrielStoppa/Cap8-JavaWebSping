package br.com.fiap.ecotrack.service;

import br.com.fiap.ecotrack.dto.MaterialPontoDTO;
import br.com.fiap.ecotrack.dto.PontoColetaDTO;
import br.com.fiap.ecotrack.exception.AppException;
import br.com.fiap.ecotrack.model.Material;
import br.com.fiap.ecotrack.model.MaterialPonto;
import br.com.fiap.ecotrack.model.PontoColeta;
import br.com.fiap.ecotrack.model.Usuario;
import br.com.fiap.ecotrack.repository.MaterialRepository;
import br.com.fiap.ecotrack.repository.PontoColetaRepository;
import br.com.fiap.ecotrack.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PontoColetaService {

    @Autowired
    private PontoColetaRepository pontoColetaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MaterialRepository materialRepository;

    public List<PontoColetaDTO> listarTodos() {
        return pontoColetaRepository.findByAtivoTrue().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public PontoColetaDTO buscarPorId(Long id) {
        PontoColeta pontoColeta = pontoColetaRepository.findById(id)
                .orElseThrow(() -> new AppException("Ponto de coleta não encontrado", HttpStatus.NOT_FOUND));
        return converterParaDTO(pontoColeta);
    }

    public List<PontoColetaDTO> buscarPorMaterial(Long materialId) {
        return pontoColetaRepository.findByMaterialId(materialId).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<PontoColetaDTO> buscarPorCidade(String cidade) {
        return pontoColetaRepository.findByCidade(cidade).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<PontoColetaDTO> buscarProximos(String latitude, String longitude, double raioKm) {
        return pontoColetaRepository.findNearby(latitude, longitude, raioKm).stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PontoColetaDTO criar(PontoColetaDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new AppException("Usuário não encontrado", HttpStatus.NOT_FOUND));

        PontoColeta pontoColeta = new PontoColeta();
        pontoColeta.setNome(dto.getNome());
        pontoColeta.setEndereco(dto.getEndereco());
        pontoColeta.setCidade(dto.getCidade());
        pontoColeta.setEstado(dto.getEstado());
        pontoColeta.setCep(dto.getCep());
        pontoColeta.setLatitude(dto.getLatitude());
        pontoColeta.setLongitude(dto.getLongitude());
        pontoColeta.setAtivo(true);
        pontoColeta.setUsuario(usuario);

        PontoColeta saved = pontoColetaRepository.save(pontoColeta);
        
        return converterParaDTO(saved);
    }

    @Transactional
    public PontoColetaDTO atualizar(Long id, PontoColetaDTO dto) {
        PontoColeta pontoColeta = pontoColetaRepository.findById(id)
                .orElseThrow(() -> new AppException("Ponto de coleta não encontrado", HttpStatus.NOT_FOUND));

        pontoColeta.setNome(dto.getNome());
        pontoColeta.setEndereco(dto.getEndereco());
        pontoColeta.setCidade(dto.getCidade());
        pontoColeta.setEstado(dto.getEstado());
        pontoColeta.setCep(dto.getCep());
        pontoColeta.setLatitude(dto.getLatitude());
        pontoColeta.setLongitude(dto.getLongitude());

        PontoColeta saved = pontoColetaRepository.save(pontoColeta);
        return converterParaDTO(saved);
    }

    @Transactional
    public void excluir(Long id) {
        PontoColeta pontoColeta = pontoColetaRepository.findById(id)
                .orElseThrow(() -> new AppException("Ponto de coleta não encontrado", HttpStatus.NOT_FOUND));

        pontoColeta.setAtivo(false);
        pontoColetaRepository.save(pontoColeta);
    }

    @Transactional
    public PontoColetaDTO adicionarMaterial(Long pontoId, MaterialPontoDTO materialPontoDTO) {
        PontoColeta pontoColeta = pontoColetaRepository.findById(pontoId)
                .orElseThrow(() -> new AppException("Ponto de coleta não encontrado", HttpStatus.NOT_FOUND));

        Material material = materialRepository.findById(materialPontoDTO.getMaterialId())
                .orElseThrow(() -> new AppException("Material não encontrado", HttpStatus.NOT_FOUND));

        MaterialPonto materialPonto = new MaterialPonto();
        materialPonto.setPontoColeta(pontoColeta);
        materialPonto.setMaterial(material);
        materialPonto.setCapacidadeMaxima(materialPontoDTO.getCapacidadeMaxima());
        materialPonto.setNivelAtual(0.0);
        materialPonto.setUnidadeMedida(materialPontoDTO.getUnidadeMedida());

        pontoColeta.getMateriaisAceitos().add(materialPonto);
        PontoColeta saved = pontoColetaRepository.save(pontoColeta);

        return converterParaDTO(saved);
    }

    @Transactional
    public PontoColetaDTO atualizarNivelMaterial(Long pontoId, Long materialId, Double novoNivel) {
        PontoColeta pontoColeta = pontoColetaRepository.findById(pontoId)
                .orElseThrow(() -> new AppException("Ponto de coleta não encontrado", HttpStatus.NOT_FOUND));

        MaterialPonto materialPonto = pontoColeta.getMateriaisAceitos().stream()
                .filter(mp -> mp.getMaterial().getId().equals(materialId))
                .findFirst()
                .orElseThrow(() -> new AppException("Material não associado a este ponto de coleta", HttpStatus.NOT_FOUND));

        materialPonto.setNivelAtual(novoNivel);
        PontoColeta saved = pontoColetaRepository.save(pontoColeta);

        return converterParaDTO(saved);
    }

    private PontoColetaDTO converterParaDTO(PontoColeta pontoColeta) {
        PontoColetaDTO dto = PontoColetaDTO.fromEntity(pontoColeta);

        // Converter materiais aceitos para DTOs
        if (pontoColeta.getMateriaisAceitos() != null) {
            List<MaterialPontoDTO> materiaisDTO = pontoColeta.getMateriaisAceitos().stream()
                    .map(MaterialPontoDTO::fromEntity)
                    .collect(Collectors.toList());
            dto.setMateriaisAceitos(materiaisDTO);
        } else {
            dto.setMateriaisAceitos(new ArrayList<>());
        }

        return dto;
    }
}
