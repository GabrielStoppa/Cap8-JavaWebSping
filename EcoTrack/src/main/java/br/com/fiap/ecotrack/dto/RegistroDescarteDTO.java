package br.com.fiap.ecotrack.dto;

import br.com.fiap.ecotrack.model.RegistroDescarte;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDescarteDTO {
    
    private Long id;
    
    @NotNull(message = "O ID do usuário é obrigatório")
    private Long usuarioId;
    
    private String nomeUsuario;
    
    @NotNull(message = "O ID do ponto de coleta é obrigatório")
    private Long pontoColetaId;
    
    private String nomePontoColeta;
    
    @NotNull(message = "O ID do material é obrigatório")
    private Long materialId;
    
    private String nomeMaterial;
    
    private LocalDateTime dataDescarte;
    
    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser maior que zero")
    private Double quantidade;
    
    @NotNull(message = "A unidade de medida é obrigatória")
    private String unidadeMedida;
    
    private String observacao;
    
    private Boolean validado;
    
    private LocalDateTime dataValidacao;
    
    private Long usuarioValidacaoId;
    
    public static RegistroDescarteDTO fromEntity(RegistroDescarte registro) {
        RegistroDescarteDTO dto = new RegistroDescarteDTO();
        dto.setId(registro.getId());
        
        if (registro.getUsuario() != null) {
            dto.setUsuarioId(registro.getUsuario().getId());
            dto.setNomeUsuario(registro.getUsuario().getNome());
        }
        
        if (registro.getPontoColeta() != null) {
            dto.setPontoColetaId(registro.getPontoColeta().getId());
            dto.setNomePontoColeta(registro.getPontoColeta().getNome());
        }
        
        if (registro.getMaterial() != null) {
            dto.setMaterialId(registro.getMaterial().getId());
            dto.setNomeMaterial(registro.getMaterial().getNome());
        }
        
        dto.setDataDescarte(registro.getDataDescarte());
        dto.setQuantidade(registro.getQuantidade());
        dto.setUnidadeMedida(registro.getUnidadeMedida());
        dto.setObservacao(registro.getObservacao());
        dto.setValidado(registro.getValidado());
        dto.setDataValidacao(registro.getDataValidacao());
        dto.setUsuarioValidacaoId(registro.getUsuarioValidacaoId());
        
        return dto;
    }
}
