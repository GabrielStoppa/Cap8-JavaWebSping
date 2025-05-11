-- Criação das tabelas para rastreamento de coleta seletiva, alertas e notificações

-- Tabela de Registros de Descarte
CREATE TABLE TB_REGISTRO_DESCARTE (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id NUMBER NOT NULL,
    ponto_coleta_id NUMBER NOT NULL,
    material_id NUMBER NOT NULL,
    data_descarte TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    quantidade NUMBER(10,2) NOT NULL,
    unidade_medida VARCHAR2(20) NOT NULL,
    observacao VARCHAR2(500),
    validado NUMBER(1) DEFAULT 0 NOT NULL,
    data_validacao TIMESTAMP,
    usuario_validacao_id NUMBER,
    CONSTRAINT fk_rd_usuario FOREIGN KEY (usuario_id) REFERENCES TB_USUARIO(id),
    CONSTRAINT fk_rd_ponto_coleta FOREIGN KEY (ponto_coleta_id) REFERENCES TB_PONTO_COLETA(id),
    CONSTRAINT fk_rd_material FOREIGN KEY (material_id) REFERENCES TB_MATERIAL(id),
    CONSTRAINT fk_rd_usuario_validacao FOREIGN KEY (usuario_validacao_id) REFERENCES TB_USUARIO(id)
);

-- Tabela de Alertas
CREATE TABLE TB_ALERTA (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ponto_coleta_id NUMBER NOT NULL,
    material_id NUMBER NOT NULL,
    titulo VARCHAR2(100) NOT NULL,
    mensagem VARCHAR2(500) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    nivel_atual NUMBER(10,2),
    capacidade_maxima NUMBER(10,2),
    tipo VARCHAR2(20) NOT NULL CHECK (tipo IN ('NIVEL_CRITICO', 'COLETA_AGENDADA', 'MANUTENCAO', 'INFORMATIVO')),
    lido NUMBER(1) DEFAULT 0 NOT NULL,
    data_leitura TIMESTAMP,
    resolvido NUMBER(1) DEFAULT 0 NOT NULL,
    data_resolucao TIMESTAMP,
    CONSTRAINT fk_al_ponto_coleta FOREIGN KEY (ponto_coleta_id) REFERENCES TB_PONTO_COLETA(id),
    CONSTRAINT fk_al_material FOREIGN KEY (material_id) REFERENCES TB_MATERIAL(id)
);

-- Tabela de Notificações
CREATE TABLE TB_NOTIFICACAO (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id NUMBER NOT NULL,
    titulo VARCHAR2(100) NOT NULL,
    conteudo VARCHAR2(1000) NOT NULL,
    data_envio TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    lida NUMBER(1) DEFAULT 0 NOT NULL,
    data_leitura TIMESTAMP,
    tipo VARCHAR2(30) NOT NULL CHECK (tipo IN ('DICA_DESCARTE', 'ALERTA_COLETA', 'PONTO_PROXIMO', 'CONFIRMACAO_DESCARTE', 'SISTEMA')),
    link_acao VARCHAR2(255),
    dado_referencia_id NUMBER,
    tipo_referencia VARCHAR2(50),
    CONSTRAINT fk_not_usuario FOREIGN KEY (usuario_id) REFERENCES TB_USUARIO(id)
);

-- Índices para melhorar a performance
CREATE INDEX idx_registro_descarte_usuario ON TB_REGISTRO_DESCARTE(usuario_id);
CREATE INDEX idx_registro_descarte_ponto ON TB_REGISTRO_DESCARTE(ponto_coleta_id);
CREATE INDEX idx_registro_descarte_material ON TB_REGISTRO_DESCARTE(material_id);
CREATE INDEX idx_registro_descarte_data ON TB_REGISTRO_DESCARTE(data_descarte);
CREATE INDEX idx_registro_descarte_validado ON TB_REGISTRO_DESCARTE(validado);

CREATE INDEX idx_alerta_ponto ON TB_ALERTA(ponto_coleta_id);
CREATE INDEX idx_alerta_material ON TB_ALERTA(material_id);
CREATE INDEX idx_alerta_lido ON TB_ALERTA(lido);
CREATE INDEX idx_alerta_resolvido ON TB_ALERTA(resolvido);
CREATE INDEX idx_alerta_tipo ON TB_ALERTA(tipo);

CREATE INDEX idx_notificacao_usuario ON TB_NOTIFICACAO(usuario_id);
CREATE INDEX idx_notificacao_lida ON TB_NOTIFICACAO(lida);
CREATE INDEX idx_notificacao_tipo ON TB_NOTIFICACAO(tipo);

-- Comentários nas tabelas
COMMENT ON TABLE TB_REGISTRO_DESCARTE IS 'Tabela para armazenar registros de descarte de resíduos';
COMMENT ON TABLE TB_ALERTA IS 'Tabela para armazenar alertas automáticos para coleta de materiais recicláveis';
COMMENT ON TABLE TB_NOTIFICACAO IS 'Tabela para armazenar notificações para usuários sobre destinação correta de resíduos';
