package br.com.fiap.ecotrack.dto;

import br.com.fiap.ecotrack.model.PontoColeta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PontoColetaDTO {
    
    private Long id;
    
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    
    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;
    
    @NotBlank(message = "A cidade é obrigatória")
    private String cidade;
    
    @NotBlank(message = "O estado é obrigatório")
    private String estado;
    
    @NotBlank(message = "O CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP inválido")
    private String cep;
    
    private String latitude;
    
    private String longitude;
    
    private boolean ativo;
    
    private Long usuarioId;
    
    private List<MaterialPontoDTO> materiaisAceitos;
    
    public static PontoColetaDTO fromEntity(PontoColeta pontoColeta) {
        PontoColetaDTO dto = new PontoColetaDTO();
        dto.setId(pontoColeta.getId());
        dto.setNome(pontoColeta.getNome());
        dto.setEndereco(pontoColeta.getEndereco());
        dto.setCidade(pontoColeta.getCidade());
        dto.setEstado(pontoColeta.getEstado());
        dto.setCep(pontoColeta.getCep());
        dto.setLatitude(pontoColeta.getLatitude());
        dto.setLongitude(pontoColeta.getLongitude());
        dto.setAtivo(pontoColeta.isAtivo());
        
        if (pontoColeta.getUsuario() != null) {
            dto.setUsuarioId(pontoColeta.getUsuario().getId());
        }
        
        return dto;
    }
}
