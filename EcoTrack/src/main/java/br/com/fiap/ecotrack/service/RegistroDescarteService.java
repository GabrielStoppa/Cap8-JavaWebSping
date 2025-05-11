package br.com.fiap.ecotrack.service;

import br.com.fiap.ecotrack.dto.RegistroDescarteDTO;
import br.com.fiap.ecotrack.exception.AppException;
import br.com.fiap.ecotrack.model.*;
import br.com.fiap.ecotrack.repository.AlertaRepository;
import br.com.fiap.ecotrack.repository.MaterialRepository;
import br.com.fiap.ecotrack.repository.PontoColetaRepository;
import br.com.fiap.ecotrack.repository.RegistroDescarteRepository;
import br.com.fiap.ecotrack.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistroDescarteService {

    @Autowired
    private RegistroDescarteRepository registroDescarteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PontoColetaRepository pontoColetaRepository;

    @Autowired
    private MaterialRepository materialRepository;
    
    @Autowired
    private AlertaRepository alertaRepository;
    
    @Autowired
    private NotificacaoService notificacaoService;

    public List<RegistroDescarteDTO> listarTodos() {
        return registroDescarteRepository.findAll().stream()
                .map(RegistroDescarteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public RegistroDescarteDTO buscarPorId(Long id) {
        RegistroDescarte registroDescarte = registroDescarteRepository.findById(id)
                .orElseThrow(() -> new AppException("Registro de descarte não encontrado", HttpStatus.NOT_FOUND));
        return RegistroDescarteDTO.fromEntity(registroDescarte);
    }

    public List<RegistroDescarteDTO> buscarPorUsuario(Long usuarioId) {
        return registroDescarteRepository.findByUsuarioId(usuarioId).stream()
                .map(RegistroDescarteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RegistroDescarteDTO> buscarPorPontoColeta(Long pontoColetaId) {
        return registroDescarteRepository.findByPontoColetaId(pontoColetaId).stream()
                .map(RegistroDescarteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RegistroDescarteDTO> buscarPorMaterial(Long materialId) {
        return registroDescarteRepository.findByMaterialId(materialId).stream()
                .map(RegistroDescarteDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public RegistroDescarteDTO registrarDescarte(RegistroDescarteDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new AppException("Usuário não encontrado", HttpStatus.NOT_FOUND));

        PontoColeta pontoColeta = pontoColetaRepository.findById(dto.getPontoColetaId())
                .orElseThrow(() -> new AppException("Ponto de coleta não encontrado", HttpStatus.NOT_FOUND));

        Material material = materialRepository.findById(dto.getMaterialId())
                .orElseThrow(() -> new AppException("Material não encontrado", HttpStatus.NOT_FOUND));

        // Verificar se o ponto de coleta aceita este material
        boolean materialAceito = pontoColeta.getMateriaisAceitos().stream()
                .anyMatch(mp -> mp.getMaterial().getId().equals(material.getId()));

        if (!materialAceito) {
            throw new AppException("Este ponto de coleta não aceita o material selecionado", HttpStatus.BAD_REQUEST);
        }

        RegistroDescarte registroDescarte = new RegistroDescarte();
        registroDescarte.setUsuario(usuario);
        registroDescarte.setPontoColeta(pontoColeta);
        registroDescarte.setMaterial(material);
        registroDescarte.setDataDescarte(LocalDateTime.now());
        registroDescarte.setQuantidade(dto.getQuantidade());
        registroDescarte.setUnidadeMedida(dto.getUnidadeMedida());
        registroDescarte.setObservacao(dto.getObservacao());
        registroDescarte.setValidado(false);

        RegistroDescarte saved = registroDescarteRepository.save(registroDescarte);
        
        // Encontrar o materialPonto relacionado e atualizar o nível atual
        Optional<MaterialPonto> materialPontoOpt = pontoColeta.getMateriaisAceitos().stream()
                .filter(mp -> mp.getMaterial().getId().equals(material.getId()))
                .findFirst();
                
        if (materialPontoOpt.isPresent()) {
            MaterialPonto materialPonto = materialPontoOpt.get();
            Double nivelAtual = materialPonto.getNivelAtual() + dto.getQuantidade();
            materialPonto.setNivelAtual(nivelAtual);
            
            // Verificar se está próximo do limite
            if (materialPonto.proximoLimite()) {
                criarAlertaNivelCritico(pontoColeta, material, materialPonto);
            }
            
            pontoColetaRepository.save(pontoColeta);
        }
        
        // Enviar notificação de confirmação para o usuário
        notificacaoService.criarNotificacaoUsuario(
            usuario.getId(),
            "Descarte registrado com sucesso",
            "Seu descarte de " + dto.getQuantidade() + " " + dto.getUnidadeMedida() + 
            " de " + material.getNome() + " no ponto de coleta " + pontoColeta.getNome() + 
            " foi registrado com sucesso.",
            Notificacao.TipoNotificacao.CONFIRMACAO_DESCARTE,
            saved.getId(),
            "descarte"
        );

        return RegistroDescarteDTO.fromEntity(saved);
    }

    @Transactional
    public RegistroDescarteDTO validarDescarte(Long id, Long usuarioValidacaoId) {
        RegistroDescarte registroDescarte = registroDescarteRepository.findById(id)
                .orElseThrow(() -> new AppException("Registro de descarte não encontrado", HttpStatus.NOT_FOUND));

        Usuario usuarioValidacao = usuarioRepository.findById(usuarioValidacaoId)
                .orElseThrow(() -> new AppException("Usuário validador não encontrado", HttpStatus.NOT_FOUND));

        if (!usuarioValidacao.getTipo().equals(Usuario.TipoUsuario.ADMIN) && 
            !usuarioValidacao.getTipo().equals(Usuario.TipoUsuario.COLETOR)) {
            throw new AppException("Apenas administradores e coletores podem validar descartes", HttpStatus.FORBIDDEN);
        }

        registroDescarte.setValidado(true);
        registroDescarte.setDataValidacao(LocalDateTime.now());
        registroDescarte.setUsuarioValidacaoId(usuarioValidacaoId);

        RegistroDescarte saved = registroDescarteRepository.save(registroDescarte);
        
        // Notificar o usuário que fez o descarte
        notificacaoService.criarNotificacaoUsuario(
            registroDescarte.getUsuario().getId(),
            "Descarte validado",
            "Seu descarte de " + registroDescarte.getQuantidade() + " " + registroDescarte.getUnidadeMedida() + 
            " de " + registroDescarte.getMaterial().getNome() + " foi validado.",
            Notificacao.TipoNotificacao.CONFIRMACAO_DESCARTE,
            saved.getId(),
            "descarte"
        );

        return RegistroDescarteDTO.fromEntity(saved);
    }

    private void criarAlertaNivelCritico(PontoColeta pontoColeta, Material material, MaterialPonto materialPonto) {
        Alerta alerta = new Alerta();
        alerta.setPontoColeta(pontoColeta);
        alerta.setMaterial(material);
        alerta.setTitulo("Nível crítico de " + material.getNome());
        alerta.setMensagem("O nível de " + material.getNome() + " atingiu " + 
                          materialPonto.getNivelAtual() + " " + materialPonto.getUnidadeMedida() + 
                          " de " + materialPonto.getCapacidadeMaxima() + " " + 
                          materialPonto.getUnidadeMedida() + ". É necessário realizar a coleta.");
        alerta.setDataCriacao(LocalDateTime.now());
        alerta.setNivelAtual(materialPonto.getNivelAtual());
        alerta.setCapacidadeMaxima(materialPonto.getCapacidadeMaxima());
        alerta.setTipo(Alerta.TipoAlerta.NIVEL_CRITICO);
        alerta.setLido(false);
        alerta.setResolvido(false);
        
        alertaRepository.save(alerta);
    }
}
