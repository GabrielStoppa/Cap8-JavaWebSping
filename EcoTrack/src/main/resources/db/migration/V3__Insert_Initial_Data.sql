-- Inserção de dados iniciais para testes do sistema EcoTrack

-- Inserção de usuários (senha padrão: 123456)
INSERT INTO TB_USUARIO (email, nome, senha, telefone, tipo) 
VALUES ('admin@ecotrack.com', 'Administrador', '$2a$10$qRzc5JCXZn5RyXwrZj1mXOiA5Y1IvYAHzyy8Q/VsPSbP.HzDklsW6', '11999998888', 'ADMIN');

INSERT INTO TB_USUARIO (email, nome, senha, telefone, tipo) 
VALUES ('coletor@ecotrack.com', 'Coletor Teste', '$2a$10$qRzc5JCXZn5RyXwrZj1mXOiA5Y1IvYAHzyy8Q/VsPSbP.HzDklsW6', '11999997777', 'COLETOR');

INSERT INTO TB_USUARIO (email, nome, senha, telefone, tipo) 
VALUES ('cidadao@ecotrack.com', 'Cidadão Teste', '$2a$10$qRzc5JCXZn5RyXwrZj1mXOiA5Y1IvYAHzyy8Q/VsPSbP.HzDklsW6', '11999996666', 'CIDADAO');

INSERT INTO TB_USUARIO (email, nome, senha, telefone, tipo) 
VALUES ('empresa@ecotrack.com', 'Empresa Sustentável', '$2a$10$qRzc5JCXZn5RyXwrZj1mXOiA5Y1IvYAHzyy8Q/VsPSbP.HzDklsW6', '11988887777', 'EMPRESA');

-- Inserção de materiais recicláveis
INSERT INTO TB_MATERIAL (nome, descricao, icone, cor, dicas_descarte, limite_alerta) 
VALUES ('Papel', 'Papéis limpos e secos como jornais, revistas, cadernos e caixas', 'paper-icon', '#0000FF', 'Remova grampos, clipes e fitas adesivas. Papéis muito sujos ou engordurados não devem ser reciclados.', 75);

INSERT INTO TB_MATERIAL (nome, descricao, icone, cor, dicas_descarte, limite_alerta) 
VALUES ('Plástico', 'Embalagens, garrafas PET, potes, sacos e sacolas plásticas', 'plastic-icon', '#FF0000', 'Limpe as embalagens antes de descartar. Retire tampas e rótulos se possível.', 70);

INSERT INTO TB_MATERIAL (nome, descricao, icone, cor, dicas_descarte, limite_alerta) 
VALUES ('Vidro', 'Garrafas, potes, frascos e cacos de vidro', 'glass-icon', '#00FF00', 'Enxágue para remover resíduos. Vidros quebrados devem ser embalados em papel jornal.', 80);

INSERT INTO TB_MATERIAL (nome, descricao, icone, cor, dicas_descarte, limite_alerta) 
VALUES ('Metal', 'Latas de alumínio, embalagens metálicas, tampas e panelas sem cabo', 'metal-icon', '#FFFF00', 'Amasse as latas para ocupar menos espaço. Enxágue para remover resíduos.', 75);

INSERT INTO TB_MATERIAL (nome, descricao, icone, cor, dicas_descarte, limite_alerta) 
VALUES ('Eletrônicos', 'Equipamentos eletrônicos, baterias e celulares', 'electronic-icon', '#FFA500', 'Nunca descarte no lixo comum. Procure pontos específicos para descarte de eletrônicos.', 60);

-- Inserção de pontos de coleta
INSERT INTO TB_PONTO_COLETA (nome, endereco, cidade, estado, cep, latitude, longitude, ativo, usuario_id) 
VALUES ('EcoPonto Central', 'Av. Paulista, 1000', 'São Paulo', 'SP', '01310-100', '-23.5616', '-46.6558', 1, 1);

INSERT INTO TB_PONTO_COLETA (nome, endereco, cidade, estado, cep, latitude, longitude, ativo, usuario_id) 
VALUES ('Reciclagem Verde', 'Rua Augusta, 500', 'São Paulo', 'SP', '01304-000', '-23.5530', '-46.6448', 1, 4);

INSERT INTO TB_PONTO_COLETA (nome, endereco, cidade, estado, cep, latitude, longitude, ativo, usuario_id) 
VALUES ('Descarte Consciente', 'Av. Rebouças, 200', 'São Paulo', 'SP', '05402-000', '-23.5663', '-46.6767', 1, 4);

-- Associação de coletores a pontos de coleta
INSERT INTO TB_COLETOR_PONTO (usuario_id, ponto_coleta_id, data_associacao, ativo) 
VALUES (2, 1, CURRENT_TIMESTAMP, 1);

INSERT INTO TB_COLETOR_PONTO (usuario_id, ponto_coleta_id, data_associacao, ativo) 
VALUES (2, 2, CURRENT_TIMESTAMP, 1);

-- Associação de materiais a pontos de coleta
INSERT INTO TB_MATERIAL_PONTO (material_id, ponto_coleta_id, capacidade_maxima, nivel_atual, unidade_medida) 
VALUES (1, 1, 500, 125, 'kg');

INSERT INTO TB_MATERIAL_PONTO (material_id, ponto_coleta_id, capacidade_maxima, nivel_atual, unidade_medida) 
VALUES (2, 1, 300, 75, 'kg');

INSERT INTO TB_MATERIAL_PONTO (material_id, ponto_coleta_id, capacidade_maxima, nivel_atual, unidade_medida) 
VALUES (3, 1, 400, 100, 'kg');

INSERT INTO TB_MATERIAL_PONTO (material_id, ponto_coleta_id, capacidade_maxima, nivel_atual, unidade_medida) 
VALUES (4, 1, 200, 50, 'kg');

INSERT INTO TB_MATERIAL_PONTO (material_id, ponto_coleta_id, capacidade_maxima, nivel_atual, unidade_medida) 
VALUES (1, 2, 300, 50, 'kg');

INSERT INTO TB_MATERIAL_PONTO (material_id, ponto_coleta_id, capacidade_maxima, nivel_atual, unidade_medida) 
VALUES (2, 2, 200, 140, 'kg');

INSERT INTO TB_MATERIAL_PONTO (material_id, ponto_coleta_id, capacidade_maxima, nivel_atual, unidade_medida) 
VALUES (5, 2, 100, 30, 'kg');

INSERT INTO TB_MATERIAL_PONTO (material_id, ponto_coleta_id, capacidade_maxima, nivel_atual, unidade_medida) 
VALUES (3, 3, 300, 240, 'kg');

INSERT INTO TB_MATERIAL_PONTO (material_id, ponto_coleta_id, capacidade_maxima, nivel_atual, unidade_medida) 
VALUES (4, 3, 250, 75, 'kg');

-- Inserção de alguns registros de descarte
INSERT INTO TB_REGISTRO_DESCARTE (usuario_id, ponto_coleta_id, material_id, data_descarte, quantidade, unidade_medida, observacao, validado, data_validacao, usuario_validacao_id) 
VALUES (3, 1, 1, CURRENT_TIMESTAMP - INTERVAL '2' DAY, 5, 'kg', 'Caixas de papelão limpas', 1, CURRENT_TIMESTAMP - INTERVAL '1' DAY, 2);

INSERT INTO TB_REGISTRO_DESCARTE (usuario_id, ponto_coleta_id, material_id, data_descarte, quantidade, unidade_medida, observacao, validado, data_validacao, usuario_validacao_id) 
VALUES (3, 1, 2, CURRENT_TIMESTAMP - INTERVAL '1' DAY, 3, 'kg', 'Garrafas PET', 1, CURRENT_TIMESTAMP, 2);

INSERT INTO TB_REGISTRO_DESCARTE (usuario_id, ponto_coleta_id, material_id, data_descarte, quantidade, unidade_medida, observacao, validado) 
VALUES (3, 2, 3, CURRENT_TIMESTAMP, 2, 'kg', 'Potes de vidro', 0);

-- Inserção de alertas
INSERT INTO TB_ALERTA (ponto_coleta_id, material_id, titulo, mensagem, data_criacao, nivel_atual, capacidade_maxima, tipo, lido, resolvido) 
VALUES (2, 2, 'Nível crítico de Plástico', 'O nível de Plástico atingiu 140 kg de 200 kg. É necessário realizar a coleta.', CURRENT_TIMESTAMP - INTERVAL '1' DAY, 140, 200, 'NIVEL_CRITICO', 1, 0);

INSERT INTO TB_ALERTA (ponto_coleta_id, material_id, titulo, mensagem, data_criacao, nivel_atual, capacidade_maxima, tipo, lido, resolvido) 
VALUES (3, 3, 'Nível crítico de Vidro', 'O nível de Vidro atingiu 240 kg de 300 kg. É necessário realizar a coleta.', CURRENT_TIMESTAMP, 240, 300, 'NIVEL_CRITICO', 0, 0);

-- Inserção de notificações
INSERT INTO TB_NOTIFICACAO (usuario_id, titulo, conteudo, data_envio, lida, tipo, dado_referencia_id, tipo_referencia) 
VALUES (3, 'Descarte registrado com sucesso', 'Seu descarte de 5 kg de Papel no ponto de coleta EcoPonto Central foi registrado com sucesso.', CURRENT_TIMESTAMP - INTERVAL '2' DAY, 1, 'CONFIRMACAO_DESCARTE', 1, 'descarte');

INSERT INTO TB_NOTIFICACAO (usuario_id, titulo, conteudo, data_envio, lida, tipo, dado_referencia_id, tipo_referencia) 
VALUES (3, 'Descarte validado', 'Seu descarte de 5 kg de Papel foi validado.', CURRENT_TIMESTAMP - INTERVAL '1' DAY, 1, 'CONFIRMACAO_DESCARTE', 1, 'descarte');

INSERT INTO TB_NOTIFICACAO (usuario_id, titulo, conteudo, data_envio, lida, tipo, dado_referencia_id, tipo_referencia) 
VALUES (3, 'Dica de descarte correto', 'Limpe as embalagens antes de descartar. Retire tampas e rótulos se possível.', CURRENT_TIMESTAMP, 0, 'DICA_DESCARTE', 2, 'material');

INSERT INTO TB_NOTIFICACAO (usuario_id, titulo, conteudo, data_envio, lida, tipo, dado_referencia_id, tipo_referencia) 
VALUES (2, 'Nível crítico de Plástico', 'O nível de Plástico atingiu 140 kg de 200 kg. É necessário realizar a coleta.', CURRENT_TIMESTAMP - INTERVAL '1' DAY, 1, 'ALERTA_COLETA', 1, 'alerta');

INSERT INTO TB_NOTIFICACAO (usuario_id, titulo, conteudo, data_envio, lida, tipo, dado_referencia_id, tipo_referencia) 
VALUES (2, 'Nível crítico de Vidro', 'O nível de Vidro atingiu 240 kg de 300 kg. É necessário realizar a coleta.', CURRENT_TIMESTAMP, 0, 'ALERTA_COLETA', 2, 'alerta');
