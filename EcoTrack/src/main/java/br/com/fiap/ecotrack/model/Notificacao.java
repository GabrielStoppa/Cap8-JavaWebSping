package br.com.fiap.ecotrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_NOTIFICACAO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String conteudo;

    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio = LocalDateTime.now();

    @Column
    private boolean lida = false;

    @Column(name = "data_leitura")
    private LocalDateTime dataLeitura;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacao tipo;

    @Column(name = "link_acao")
    private String linkAcao;

    @Column(name = "dado_referencia_id")
    private Long dadoReferenciaId;

    @Column(name = "tipo_referencia")
    private String tipoReferencia;

    public enum TipoNotificacao {
        DICA_DESCARTE, ALERTA_COLETA, PONTO_PROXIMO, CONFIRMACAO_DESCARTE, SISTEMA
    }
}
