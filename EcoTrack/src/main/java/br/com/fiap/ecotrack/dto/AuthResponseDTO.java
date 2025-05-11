package br.com.fiap.ecotrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    
    private String token;
    private String tipo;
    private Long usuarioId;
    private String nome;
    private String email;
    private String tipoUsuario;
    
}
