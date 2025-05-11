package br.com.fiap.ecotrack.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_PONTO_COLETA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String endereco;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String cep;

    @Column
    private String latitude;

    @Column
    private String longitude;

    @Column(nullable = false)
    private boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "pontoColeta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ColetorPonto> coletores = new ArrayList<>();

    @OneToMany(mappedBy = "pontoColeta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialPonto> materiaisAceitos = new ArrayList<>();

    @OneToMany(mappedBy = "pontoColeta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroDescarte> registrosDescarte = new ArrayList<>();
}
