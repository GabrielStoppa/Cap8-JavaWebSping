package br.com.fiap.ecotrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_ALERTA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_coleta_id", nullable = false)
    private PontoColeta pontoColeta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 500)
    private String mensagem;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "nivel_atual")
    private Double nivelAtual;

    @Column(name = "capacidade_maxima")
    private Double capacidadeMaxima;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAlerta tipo;

    @Column
    private boolean lido = false;

    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;

    @Column(name = "resolvido")
    private boolean resolvido = false;

    @Column(name = "data_resolucao")
    private LocalDateTime dataResolucao;

    public enum TipoAlerta {
        NIVEL_CRITICO, COLETA_AGENDADA, MANUTENCAO, INFORMATIVO
    }
}
