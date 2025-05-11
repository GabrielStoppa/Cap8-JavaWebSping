-- Criação das tabelas iniciais do sistema EcoTrack para Gestão de Resíduos e Reciclagem

-- Tabela de Usuários
CREATE TABLE TB_USUARIO (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR2(100) NOT NULL UNIQUE,
    nome VARCHAR2(100) NOT NULL,
    senha VARCHAR2(100) NOT NULL,
    telefone VARCHAR2(20),
    tipo VARCHAR2(20) NOT NULL CHECK (tipo IN ('ADMIN', 'COLETOR', 'CIDADAO', 'EMPRESA'))
);

-- Tabela de Materiais Recicláveis
CREATE TABLE TB_MATERIAL (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(50) NOT NULL UNIQUE,
    descricao VARCHAR2(255) NOT NULL,
    icone VARCHAR2(100) NOT NULL,
    cor VARCHAR2(20),
    dicas_descarte VARCHAR2(1000),
    limite_alerta NUMBER(10, 2)
);

-- Tabela de Pontos de Coleta
CREATE TABLE TB_PONTO_COLETA (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome VARCHAR2(100) NOT NULL,
    endereco VARCHAR2(200) NOT NULL,
    cidade VARCHAR2(100) NOT NULL,
    estado VARCHAR2(50) NOT NULL,
    cep VARCHAR2(10) NOT NULL,
    latitude VARCHAR2(20),
    longitude VARCHAR2(20),
    ativo NUMBER(1) DEFAULT 1 NOT NULL,
    usuario_id NUMBER NOT NULL,
    CONSTRAINT fk_pc_usuario FOREIGN KEY (usuario_id) REFERENCES TB_USUARIO(id)
);

-- Tabela de Relação entre Coletores e Pontos de Coleta
CREATE TABLE TB_COLETOR_PONTO (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id NUMBER NOT NULL,
    ponto_coleta_id NUMBER NOT NULL,
    data_associacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ativo NUMBER(1) DEFAULT 1 NOT NULL,
    CONSTRAINT fk_cp_usuario FOREIGN KEY (usuario_id) REFERENCES TB_USUARIO(id),
    CONSTRAINT fk_cp_ponto_coleta FOREIGN KEY (ponto_coleta_id) REFERENCES TB_PONTO_COLETA(id),
    CONSTRAINT uk_coletor_ponto UNIQUE (usuario_id, ponto_coleta_id)
);

-- Tabela de Relação entre Materiais e Pontos de Coleta
CREATE TABLE TB_MATERIAL_PONTO (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    material_id NUMBER NOT NULL,
    ponto_coleta_id NUMBER NOT NULL,
    capacidade_maxima NUMBER(10,2),
    nivel_atual NUMBER(10,2) DEFAULT 0 NOT NULL,
    unidade_medida VARCHAR2(20),
    CONSTRAINT fk_mp_material FOREIGN KEY (material_id) REFERENCES TB_MATERIAL(id),
    CONSTRAINT fk_mp_ponto_coleta FOREIGN KEY (ponto_coleta_id) REFERENCES TB_PONTO_COLETA(id),
    CONSTRAINT uk_material_ponto UNIQUE (material_id, ponto_coleta_id)
);

-- Índices para melhorar a performance
CREATE INDEX idx_ponto_coleta_cidade ON TB_PONTO_COLETA(cidade);
CREATE INDEX idx_ponto_coleta_ativo ON TB_PONTO_COLETA(ativo);
CREATE INDEX idx_coletor_ponto_ativo ON TB_COLETOR_PONTO(ativo);

-- Comentários nas tabelas
COMMENT ON TABLE TB_USUARIO IS 'Tabela para armazenar dados dos usuários do sistema';
COMMENT ON TABLE TB_MATERIAL IS 'Tabela para armazenar informações de materiais recicláveis';
COMMENT ON TABLE TB_PONTO_COLETA IS 'Tabela para armazenar dados dos pontos de coleta de resíduos';
COMMENT ON TABLE TB_COLETOR_PONTO IS 'Tabela para associar coletores a pontos de coleta';
COMMENT ON TABLE TB_MATERIAL_PONTO IS 'Tabela para associar materiais a pontos de coleta com capacidade máxima e nível atual';
