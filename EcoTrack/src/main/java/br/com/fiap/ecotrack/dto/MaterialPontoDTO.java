package br.com.fiap.ecotrack.dto;

import br.com.fiap.ecotrack.model.MaterialPonto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialPontoDTO {
    
    private Long id;
    
    @NotNull(message = "O ID do material é obrigatório")
    private Long materialId;
    
    private String nomeMaterial;
    
    private String descricaoMaterial;
    
    private String iconeMaterial;
    
    private String corMaterial;
    
    @Positive(message = "A capacidade máxima deve ser maior que zero")
    private Double capacidadeMaxima;
    
    private Double nivelAtual;
    
    private String unidadeMedida;
    
    private boolean proximoLimite;
    
    public static MaterialPontoDTO fromEntity(MaterialPonto materialPonto) {
        MaterialPontoDTO dto = new MaterialPontoDTO();
        dto.setId(materialPonto.getId());
        
        if (materialPonto.getMaterial() != null) {
            dto.setMaterialId(materialPonto.getMaterial().getId());
            dto.setNomeMaterial(materialPonto.getMaterial().getNome());
            dto.setDescricaoMaterial(materialPonto.getMaterial().getDescricao());
            dto.setIconeMaterial(materialPonto.getMaterial().getIcone());
            dto.setCorMaterial(materialPonto.getMaterial().getCor());
        }
        
        dto.setCapacidadeMaxima(materialPonto.getCapacidadeMaxima());
        dto.setNivelAtual(materialPonto.getNivelAtual());
        dto.setUnidadeMedida(materialPonto.getUnidadeMedida());
        dto.setProximoLimite(materialPonto.proximoLimite());
        
        return dto;
    }
}
