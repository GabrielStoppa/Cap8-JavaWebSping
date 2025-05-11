package br.com.fiap.ecotrack.service;

import br.com.fiap.ecotrack.dto.AlertaDTO;
import br.com.fiap.ecotrack.exception.AppException;
import br.com.fiap.ecotrack.model.Alerta;
import br.com.fiap.ecotrack.model.ColetorPonto;
import br.com.fiap.ecotrack.model.Notificacao;
import br.com.fiap.ecotrack.model.PontoColeta;
import br.com.fiap.ecotrack.repository.AlertaRepository;
import br.com.fiap.ecotrack.repository.PontoColetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private PontoColetaRepository pontoColetaRepository;
    
    @Autowired
    private NotificacaoService notificacaoService;

    public List<AlertaDTO> listarTodos() {
        return alertaRepository.findAll().stream()
                .map(AlertaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public AlertaDTO buscarPorId(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new AppException("Alerta não encontrado", HttpStatus.NOT_FOUND));
        return AlertaDTO.fromEntity(alerta);
    }

    public List<AlertaDTO> buscarPorPontoColeta(Long pontoColetaId) {
        return alertaRepository.findByPontoColetaId(pontoColetaId).stream()
                .map(AlertaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AlertaDTO> buscarAlertasAtivos() {
        return alertaRepository.findByResolvidoFalse().stream()
                .map(AlertaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AlertaDTO> buscarAlertasNaoLidos() {
        return alertaRepository.findByLidoFalse().stream()
                .map(AlertaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public AlertaDTO criarAlerta(Alerta alerta) {
        Alerta saved = alertaRepository.save(alerta);
        
        // Notificar os coletores associados ao ponto de coleta
        notificarColetoresSobreAlerta(saved);
        
        return AlertaDTO.fromEntity(saved);
    }

    @Transactional
    public AlertaDTO marcarComoLido(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new AppException("Alerta não encontrado", HttpStatus.NOT_FOUND));

        alerta.setLido(true);
        alerta.setDataLeitura(LocalDateTime.now());

        Alerta saved = alertaRepository.save(alerta);
        return AlertaDTO.fromEntity(saved);
    }

    @Transactional
    public AlertaDTO marcarComoResolvido(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new AppException("Alerta não encontrado", HttpStatus.NOT_FOUND));

        alerta.setResolvido(true);
        alerta.setDataResolucao(LocalDateTime.now());

        Alerta saved = alertaRepository.save(alerta);
        return AlertaDTO.fromEntity(saved);
    }

    private void notificarColetoresSobreAlerta(Alerta alerta) {
        PontoColeta pontoColeta = alerta.getPontoColeta();
        
        // Obter todos os coletores associados ao ponto de coleta
        List<Long> coletoresIds = pontoColeta.getColetores().stream()
                .filter(ColetorPonto::isAtivo)
                .map(cp -> cp.getColetor().getId())
                .collect(Collectors.toList());
        
        // Enviar notificação para cada coletor
        coletoresIds.forEach(coletorId -> {
            notificacaoService.criarNotificacaoUsuario(
                coletorId,
                alerta.getTitulo(),
                alerta.getMensagem(),
                Notificacao.TipoNotificacao.ALERTA_COLETA,
                alerta.getId(),
                "alerta"
            );
        });
    }
    
    @Scheduled(cron = "0 0 * * * *") // Executar a cada hora
    @Transactional
    public void verificarNiveisCriticos() {
        List<PontoColeta> pontosAtivos = pontoColetaRepository.findByAtivoTrue();
        
        for (PontoColeta ponto : pontosAtivos) {
            ponto.getMateriaisAceitos().forEach(materialPonto -> {
                if (materialPonto.proximoLimite()) {
                    // Verificar se já existe um alerta não resolvido para este material e ponto
                    List<Alerta> alertasAtivos = alertaRepository.findAtivosByPontoColetaId(ponto.getId()).stream()
                            .filter(a -> a.getMaterial().getId().equals(materialPonto.getMaterial().getId()))
                            .filter(a -> !a.isResolvido())
                            .collect(Collectors.toList());
                    
                    if (alertasAtivos.isEmpty()) {
                        // Criar novo alerta se não existir
                        Alerta alerta = new Alerta();
                        alerta.setPontoColeta(ponto);
                        alerta.setMaterial(materialPonto.getMaterial());
                        alerta.setTitulo("Nível crítico de " + materialPonto.getMaterial().getNome());
                        alerta.setMensagem("O nível de " + materialPonto.getMaterial().getNome() + 
                                          " atingiu " + materialPonto.getNivelAtual() + " " + materialPonto.getUnidadeMedida() + 
                                          " de " + materialPonto.getCapacidadeMaxima() + " " + materialPonto.getUnidadeMedida() + 
                                          ". É necessário realizar a coleta.");
                        alerta.setDataCriacao(LocalDateTime.now());
                        alerta.setNivelAtual(materialPonto.getNivelAtual());
                        alerta.setCapacidadeMaxima(materialPonto.getCapacidadeMaxima());
                        alerta.setTipo(Alerta.TipoAlerta.NIVEL_CRITICO);
                        alerta.setLido(false);
                        alerta.setResolvido(false);
                        
                        alertaRepository.save(alerta);
                        
                        // Notificar os coletores
                        notificarColetoresSobreAlerta(alerta);
                    }
                }
            });
        }
    }
}
