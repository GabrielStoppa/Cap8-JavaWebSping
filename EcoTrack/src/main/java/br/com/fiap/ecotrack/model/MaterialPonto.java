package br.com.fiap.ecotrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "TB_MATERIAL_PONTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialPonto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_coleta_id", nullable = false)
    private PontoColeta pontoColeta;

    @Column(name = "capacidade_maxima")
    private Double capacidadeMaxima;

    @Column(name = "nivel_atual")
    private Double nivelAtual = 0.0;

    @Column(name = "unidade_medida")
    private String unidadeMedida;
    
    /**
     * Verifica se o nível atual está próximo da capacidade máxima
     * @return true se estiver acima do limite de alerta
     */
    public boolean proximoLimite() {
        if (capacidadeMaxima == null || nivelAtual == null || material.getLimiteAlerta() == null) {
            return false;
        }
        
        double percentual = (nivelAtual / capacidadeMaxima) * 100;
        return percentual >= material.getLimiteAlerta();
    }
}
