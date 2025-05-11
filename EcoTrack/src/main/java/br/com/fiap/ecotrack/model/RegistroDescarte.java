package br.com.fiap.ecotrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_REGISTRO_DESCARTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDescarte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ponto_coleta_id", nullable = false)
    private PontoColeta pontoColeta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @Column(name = "data_descarte", nullable = false)
    private LocalDateTime dataDescarte = LocalDateTime.now();

    @Column(nullable = false)
    private Double quantidade;

    @Column(name = "unidade_medida", nullable = false)
    private String unidadeMedida;

    @Column
    private String observacao;

    @Column(name = "validado")
    private Boolean validado = false;

    @Column(name = "data_validacao")
    private LocalDateTime dataValidacao;

    @Column(name = "usuario_validacao_id")
    private Long usuarioValidacaoId;
}
