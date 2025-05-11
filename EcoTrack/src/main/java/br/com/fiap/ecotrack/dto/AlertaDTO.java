package br.com.fiap.ecotrack.dto;

import br.com.fiap.ecotrack.model.Alerta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaDTO {
    
    private Long id;
    
    private Long pontoColetaId;
    
    private String nomePontoColeta;
    
    private Long materialId;
    
    private String nomeMaterial;
    
    private String titulo;
    
    private String mensagem;
    
    private LocalDateTime dataCriacao;
    
    private Double nivelAtual;
    
    private Double capacidadeMaxima;
    
    private Alerta.TipoAlerta tipo;
    
    private boolean lido;
    
    private LocalDateTime dataLeitura;
    
    private boolean resolvido;
    
    private LocalDateTime dataResolucao;
    
    public static AlertaDTO fromEntity(Alerta alerta) {
        AlertaDTO dto = new AlertaDTO();
        dto.setId(alerta.getId());
        
        if (alerta.getPontoColeta() != null) {
            dto.setPontoColetaId(alerta.getPontoColeta().getId());
            dto.setNomePontoColeta(alerta.getPontoColeta().getNome());
        }
        
        if (alerta.getMaterial() != null) {
            dto.setMaterialId(alerta.getMaterial().getId());
            dto.setNomeMaterial(alerta.getMaterial().getNome());
        }
        
        dto.setTitulo(alerta.getTitulo());
        dto.setMensagem(alerta.getMensagem());
        dto.setDataCriacao(alerta.getDataCriacao());
        dto.setNivelAtual(alerta.getNivelAtual());
        dto.setCapacidadeMaxima(alerta.getCapacidadeMaxima());
        dto.setTipo(alerta.getTipo());
        dto.setLido(alerta.isLido());
        dto.setDataLeitura(alerta.getDataLeitura());
        dto.setResolvido(alerta.isResolvido());
        dto.setDataResolucao(alerta.getDataResolucao());
        
        return dto;
    }
}
