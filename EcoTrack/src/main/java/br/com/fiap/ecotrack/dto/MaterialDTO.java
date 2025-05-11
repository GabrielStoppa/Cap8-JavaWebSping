package br.com.fiap.ecotrack.dto;

import br.com.fiap.ecotrack.model.Material;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDTO {
    
    private Long id;
    
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres")
    private String nome;
    
    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres")
    private String descricao;
    
    @NotBlank(message = "O ícone é obrigatório")
    private String icone;
    
    private String cor;
    
    @Size(max = 1000, message = "As dicas de descarte devem ter no máximo 1000 caracteres")
    private String dicasDescarte;
    
    @NotNull(message = "O limite de alerta é obrigatório")
    @Positive(message = "O limite de alerta deve ser maior que zero")
    private Double limiteAlerta;
    
    public static MaterialDTO fromEntity(Material material) {
        MaterialDTO dto = new MaterialDTO();
        dto.setId(material.getId());
        dto.setNome(material.getNome());
        dto.setDescricao(material.getDescricao());
        dto.setIcone(material.getIcone());
        dto.setCor(material.getCor());
        dto.setDicasDescarte(material.getDicasDescarte());
        dto.setLimiteAlerta(material.getLimiteAlerta());
        return dto;
    }
}
