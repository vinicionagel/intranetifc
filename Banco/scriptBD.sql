CREATE TABLE tipo_configuracao (
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT "tipo_configuracao_pkey" PRIMARY KEY (codigo)
);

CREATE TABLE prioridade (
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT "prioridade_pkey" PRIMARY KEY (codigo)
);

CREATE TABLE status (
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT "status_pkey" PRIMARY KEY (codigo)
);

CREATE TABLE fabricante (
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT "fabricante_pkey" PRIMARY KEY (codigo)
);

CREATE TABLE campus (
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT "campus_pkey" PRIMARY KEY (codigo)
);

CREATE TABLE funcao (
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(45) NOT NULL,
  CONSTRAINT "funcao_pkey" PRIMARY KEY (codigo)
);

CREATE TABLE tipo_patrimonio (
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT "tipo_patrimonio_pkey" PRIMARY KEY (codigo)
);

CREATE TABLE tipo_permissao (
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT "tipo_permissao_pkey" PRIMARY KEY (codigo)
);

CREATE TABLE configuracao_banco (
    codigo SERIAL NOT NULL,
    tipo_configuracao_codigo INTEGER NOT NULL,
    endereco CHARACTER VARYING(255) NOT NULL,
    nome_banco CHARACTER VARYING(255) NOT NULL,
    porta CHARACTER VARYING(10) NOT NULL,
    usuario CHARACTER VARYING(20) NOT NULL,
    senha CHARACTER VARYING(20) NOT NULL,
    CONSTRAINT "configuracao_banco_pkey" PRIMARY KEY (codigo),
    CONSTRAINT "ref_configuracao_banco_to_tipo_configuracao" FOREIGN KEY (tipo_configuracao_codigo)
      REFERENCES tipo_configuracao (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE configuracao (
  codigo SERIAL NOT NULL,
  tipo_configuracao_codigo INTEGER NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT "configuracao_pkey" PRIMARY KEY (codigo),
  CONSTRAINT "ref_configuracao_to_tipo_configuracao" FOREIGN KEY (tipo_configuracao_codigo)
      REFERENCES tipo_configuracao (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE localizacao (
  codigo SERIAL NOT NULL,
  campus_codigo INTEGER NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT "localizacao_pkey" PRIMARY KEY (codigo),
  CONSTRAINT "ref_localizacao_to_campus" FOREIGN KEY (campus_codigo)
      REFERENCES campus (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE setor (
  codigo SERIAL NOT NULL,
  campus_codigo INTEGER NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  interface_ativo  CHARACTER VARYING(100),
  CONSTRAINT "setor_pkey" PRIMARY KEY (codigo),
  CONSTRAINT "ref_localizacao_to_campus" FOREIGN KEY (campus_codigo)
      REFERENCES campus (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE publicidade (
  codigo SERIAL NOT NULL,
  nome_formato CHARACTER VARYING(255) NOT NULL,
  data_fim_visualizacao TIMESTAMP WITHOUT TIME ZONE,
  data_envio DATE NOT NULL,
  arquivo bytea,
  tipo_publicidade CHARACTER VARYING(2),
  CONSTRAINT "publicidade_pkey" PRIMARY KEY (codigo)
);

CREATE TABLE imagem_login (
  publicidade_codigo INTEGER NOT NULL,
  pode_ser_removido BOOLEAN NOT NULL,
  link_externo CHARACTER VARYING(255),
  CONSTRAINT "imagem_login_pkey" PRIMARY KEY (publicidade_codigo),
  CONSTRAINT "ref_publicidade_to_imagem_login" FOREIGN KEY (publicidade_codigo)
      REFERENCES publicidade (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE banner (
  publicidade_codigo INTEGER NOT NULL,
  mensagem CHARACTER VARYING(255),
  descricao CHARACTER VARYING(255),
  CONSTRAINT "banner_pkey" PRIMARY KEY (publicidade_codigo),
  CONSTRAINT "ref_publicidade_to_banner" FOREIGN KEY (publicidade_codigo)
      REFERENCES publicidade (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE banner_campus(
  publicidade_codigo bigint NOT NULL,
  campus_codigo bigint NOT NULL,
  CONSTRAINT banner_campus_pkey PRIMARY KEY (publicidade_codigo, campus_codigo),
  CONSTRAINT fk_banner_campus_campus_codigo FOREIGN KEY (campus_codigo)
      REFERENCES campus (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_banner_campus_publicidade_codigo FOREIGN KEY (publicidade_codigo)
      REFERENCES publicidade (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE tipo_servico (
  codigo SERIAL NOT NULL,
  setor_codigo INTEGER NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT "tipo_servico_pkey" PRIMARY KEY (codigo),
  CONSTRAINT "ref_tipo_servico_to_setor" FOREIGN KEY (setor_codigo)
      REFERENCES setor (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE servico (
  codigo SERIAL NOT NULL,
  tipo_servico_codigo INTEGER NOT NULL,
  descricao_completa CHARACTER VARYING(255) NOT NULL,
  descricao_curta CHARACTER VARYING(45) NOT NULL,  
  CONSTRAINT "servico_pkey" PRIMARY KEY (codigo),
  CONSTRAINT "ref_servico_to_setor" FOREIGN KEY (tipo_servico_codigo)
      REFERENCES tipo_servico (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE patrimonio (
  codigo SERIAL NOT NULL,  
  setor_codigo INTEGER NOT NULL,
  fabricante_codigo INTEGER NOT NULL,
  localizacao_codigo INTEGER NOT NULL,
  tipo_patrimonio_codigo INTEGER NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  data_aquisicao DATE,
  data_baixa DATE,
  nota_fiscal CHARACTER VARYING(255),
  numero_etiqueta CHARACTER VARYING(255),
  CONSTRAINT "patrimonio_pkey" PRIMARY KEY (codigo),
  CONSTRAINT "ref_patrimonio_to_tipo_patrimonio" FOREIGN KEY (tipo_patrimonio_codigo)
      REFERENCES tipo_patrimonio (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_patrimonio_to_localizacao" FOREIGN KEY (localizacao_codigo)
      REFERENCES localizacao (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_patrimonio_to_marca" FOREIGN KEY (fabricante_codigo)
      REFERENCES fabricante (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_patrimonio_to_setor" FOREIGN KEY (setor_codigo)
      REFERENCES setor (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE usuario (
  codigo SERIAL NOT NULL,
  ultimo_campus_codigo INTEGER,
  nome CHARACTER VARYING(255) NOT NULL,
  email CHARACTER VARYING(255) NOT NULL,
  "login" CHARACTER VARYING(45) NOT NULL,
  senha CHARACTER VARYING(45) NOT NULL,
  telefone CHARACTER VARYING(45),  
  data_nascimento DATE,  
  estado INTEGER,
  verificacao CHARACTER VARYING(45),
  CONSTRAINT "usuario_pkey" PRIMARY KEY (codigo),
  CONSTRAINT unicos UNIQUE (login, email),
  CONSTRAINT "ref_usuario_to_campus" FOREIGN KEY (ultimo_campus_codigo)
      REFERENCES campus (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE usuario_setor (
  setor_codigo INTEGER NOT NULL,
  usuario_codigo INTEGER NOT NULL,
  funcao_codigo INTEGER NOT NULL,
  responsavel_setor INTEGER,
  CONSTRAINT "usuario_setor_pkey" PRIMARY KEY (setor_codigo, usuario_codigo),
  CONSTRAINT "ref_setor_to_usuario_setor" FOREIGN KEY (setor_codigo)
      REFERENCES setor (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_usuario_to_usuario_setor" FOREIGN KEY (usuario_codigo)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_funcao_to_usuario_setor" FOREIGN KEY (funcao_codigo)
      REFERENCES funcao (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION      
);

CREATE TABLE usuario_configuracao (
  usuario_codigo INTEGER NOT NULL,
  configuracao_codigo INTEGER NOT NULL,
  CONSTRAINT "usuario_configuracao_pkey" PRIMARY KEY (configuracao_codigo, usuario_codigo),
  CONSTRAINT "ref_usuario_configuracao_to_configuracao" FOREIGN KEY (configuracao_codigo)
      REFERENCES configuracao (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_usuario_configuracao_to_usuario" FOREIGN KEY (usuario_codigo)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE chamado (
  codigo SERIAL NOT NULL,
  usuario_codigo_autor INTEGER NOT NULL,
  usuario_codigo_atribuido INTEGER,  
  status_codigo INTEGER NOT NULL,
  titulo CHARACTER VARYING(45) NOT NULL,
  descricao TEXT NOT NULL,
  data_hora_abertura TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  data_hora_fechamento TIMESTAMP WITHOUT TIME ZONE,  
  data_prevista DATE,
  prioridade_codigo INTEGER NOT NULL,
  percentual INTEGER NOT NULL,
  tipo_chamado CHARACTER VARYING(2) NOT NULL,
  CONSTRAINT "chamado_pkey" PRIMARY KEY (codigo),  
  CONSTRAINT "ref_chamado_to_autor" FOREIGN KEY (usuario_codigo_autor)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_chamado_to_atribuido" FOREIGN KEY (usuario_codigo_atribuido)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT "ref_chamado_to_prioridade" FOREIGN KEY (prioridade_codigo)
      REFERENCES prioridade (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_andamento_to_status" FOREIGN KEY (status_codigo)
      REFERENCES status (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE chamado_infra (
  chamado_codigo INTEGER NOT NULL,
  patrimonio_codigo INTEGER NOT NULL,
  CONSTRAINT "chamado_infra_pkey" PRIMARY KEY (chamado_codigo), 
  CONSTRAINT "ref_chamado_infra_to_patrimonio" FOREIGN KEY (patrimonio_codigo)
      REFERENCES patrimonio (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_chamado_infra_to_chamado" FOREIGN KEY (chamado_codigo)
      REFERENCES chamado (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE chamado_servico (
  chamado_codigo INTEGER NOT NULL,
  servico_codigo INTEGER NOT NULL,
  CONSTRAINT "chamado_servico_pkey" PRIMARY KEY (chamado_codigo), 
  CONSTRAINT "ref_chamado_servico_to_servico" FOREIGN KEY (servico_codigo)
      REFERENCES servico (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_chamado_servico_to_chamado" FOREIGN KEY (chamado_codigo)
      REFERENCES chamado (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE observador (
  usuario_codigo INTEGER NOT NULL,
  chamado_codigo INTEGER NOT NULL,
  CONSTRAINT "observador_pkey" PRIMARY KEY (chamado_codigo, usuario_codigo),
  CONSTRAINT "ref_observador_to_usuario" FOREIGN KEY (usuario_codigo)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_observador_to_chamado" FOREIGN KEY (chamado_codigo)
      REFERENCES chamado (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE hora (
 codigo SERIAL NOT NULL,
 usuario_codigo INTEGER NOT NULL,
 chamado_codigo INTEGER NOT NULL,
 tempo REAL NOT NULL,
 data_trabalho DATE NOT NULL,
 descricao CHARACTER VARYING(255),
 CONSTRAINT "hora_pkey" PRIMARY KEY (codigo),
 CONSTRAINT "ref_hora_to_usuario" FOREIGN KEY (usuario_codigo)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
 CONSTRAINT "ref_hora_to_chamado" FOREIGN KEY (chamado_codigo)
      REFERENCES chamado (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE arquivo (
  codigo SERIAL NOT NULL,
  chamado_codigo INTEGER NOT NULL,
  arquivo bytea,
  nome CHARACTER VARYING(45),
  CONSTRAINT "arquivo_pkey" PRIMARY KEY (codigo),
  CONSTRAINT "ref_arquivo_to_chamado" FOREIGN KEY (chamado_codigo)
      REFERENCES chamado (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE andamento (
  codigo SERIAL NOT NULL,
  chamado_codigo INTEGER NOT NULL,  
  usuario_codigo INTEGER NOT NULL,
  descricao TEXT,
  "log" CHARACTER VARYING(400),
  data_hora TIMESTAMP WITHOUT TIME ZONE NOT NULL, 
  CONSTRAINT "andamento_pkey" PRIMARY KEY (codigo),
  CONSTRAINT "ref_andamento_to_usuario" FOREIGN KEY (usuario_codigo)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_andamento_to_chamado" FOREIGN KEY (chamado_codigo)
      REFERENCES chamado (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION 
);

CREATE TABLE interface (
  codigo SERIAL NOT NULL,
  interface_codigo INTEGER,
  descricao_completa CHARACTER VARYING(255) NOT NULL,
  descricao_curta CHARACTER VARYING(45) NOT NULL,
  posicao_menu INTEGER NOT NULL, 
  url CHARACTER VARYING(255),
  icone CHARACTER VARYING(255),
  cor CHARACTER VARYING(45),
  CONSTRAINT "interface_pkey" PRIMARY KEY (codigo),
  CONSTRAINT "ref_interface_to_interface" FOREIGN KEY (interface_codigo)
      REFERENCES interface (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE permissao (
  codigo SERIAL NOT NULL,
  tipo_permissao_codigo INTEGER NOT NULL,
  interface_codigo INTEGER NOT NULL,
  descricao CHARACTER VARYING(255),
  CONSTRAINT "permissao_pkey" PRIMARY KEY (codigo),
  CONSTRAINT "ref_permissao_to_tipo_permissao" FOREIGN KEY (tipo_permissao_codigo)
      REFERENCES tipo_permissao (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_permissao_to_interface" FOREIGN KEY (interface_codigo)
      REFERENCES interface (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE usuario_permissao (
  permissao_codigo INTEGER NOT NULL,
  usuario_codigo INTEGER NOT NULL,
  CONSTRAINT "usuario_permissao_pkey" PRIMARY KEY (permissao_codigo, usuario_codigo),
  CONSTRAINT "ref_usuario_permissao_permissao_setor" FOREIGN KEY (permissao_codigo)
      REFERENCES permissao (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_usuario_permissao_to_usuario" FOREIGN KEY (usuario_codigo)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION     
);


CREATE TABLE reserva(
  codigo SERIAL NOT NULL,
  usuario_codigo_autor INTEGER NOT NULL,
  descricao text NOT NULL,
  data_hora_inicial timestamp without time zone NOT NULL,
  data_hora_final timestamp without time zone NOT NULL,
  tipo_reserva CHARACTER VARYING(2) NOT NULL,
  CONSTRAINT reserva_pkey PRIMARY KEY (codigo),
  CONSTRAINT ref_reserva_to_autor FOREIGN KEY (usuario_codigo_autor)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE reserva_patrimonio
(
  reserva_codigo INTEGER NOT NULL,
  patrimonio_codigo INTEGER NOT NULL,
  CONSTRAINT reserva_patrimonio_pkey PRIMARY KEY (reserva_codigo),
  CONSTRAINT ref_reserva_patrimonio_to_reserva FOREIGN KEY (reserva_codigo)
      REFERENCES reserva (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ref_reserva_patrimonio_to_patrimonio FOREIGN KEY (patrimonio_codigo)
      REFERENCES patrimonio (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE reserva_localizacao
(
  reserva_codigo INTEGER NOT NULL,
  localizacao_codigo INTEGER NOT NULL,
  CONSTRAINT reserva_localizacao_pkey PRIMARY KEY (reserva_codigo),
  CONSTRAINT ref_reserva_localizacao_to_reserva FOREIGN KEY (reserva_codigo)
      REFERENCES reserva (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ref_reserva_localizacao_to_localizacao FOREIGN KEY (localizacao_codigo)
      REFERENCES localizacao (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE usuario_permissao_setor
(
  permissao_codigo INTEGER NOT NULL,
  usuario_codigo INTEGER NOT NULL,
  setor_codigo INTEGER NOT NULL,
  CONSTRAINT usuario_permissao_setor_pkey PRIMARY KEY (usuario_codigo, permissao_codigo, setor_codigo),
  CONSTRAINT ref_usuario_permissao_setor_to_setor FOREIGN KEY (setor_codigo)
      REFERENCES setor (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ref_usuario_permissao_setor_to_usuario_permissao FOREIGN KEY (permissao_codigo, usuario_codigo)
      REFERENCES usuario_permissao (permissao_codigo, usuario_codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER DATABASE "intranetifc" SET bytea_output='escape';

INSERT INTO tipo_configuracao (descricao) 
	VALUES ('Email'),
	       ('Banco_siga');

INSERT INTO configuracao_banco(
            tipo_configuracao_codigo, endereco, nome_banco, porta, 
            usuario, senha)
    VALUES (2, '189.8.252.13', 'dbsiga', '5432','postgres', 'postgres');

INSERT INTO prioridade (descricao) 
	VALUES ('Baixa'),
	       ('Normal'),
	       ('Alta');

INSERT INTO status (descricao) 
	VALUES ('Novo'),
	       ('Atribuido'),
	       ('Em Andamento'),
               ('Devolvido'),
	       ('Cancelado'),
	       ('Fechado'),
	       ('Retirado/Manutenção');
INSERT INTO fabricante (descricao) 
	VALUES ('Pauta'),
	       ('LG'),
	       ('HP'),
	       ('Sony');
INSERT INTO campus (descricao) 
	VALUES ('Rio do Sul - Unidade Urbana'),
	       ('Rio do Sul - Unidade Sede');
INSERT INTO funcao (descricao) 
	VALUES ('Técnico'),
	       ('Assistente de Informática'),
	       ('Peso para Papel'),
	       ('Capinador');
INSERT INTO tipo_patrimonio (descricao) 
	VALUES ('Computador'),
	       ('Retroprojetor'),
	       ('Projeto Multimidia'),
	       ('Impressora'),
	       ('Notebook'),
	       ('Monitor'),
	       ('Estabilizador'),
	       ('Porta');
INSERT INTO tipo_permissao (descricao)
	VALUES ('Pesquisar'), 
	       ('Incluir'), 
	       ('Editar'), 
	       ('Excluir'), 
	       ('Ver Menu'),
               ('Agendar'),
               ('Configurar');
INSERT INTO configuracao (tipo_configuracao_codigo, descricao)
	VALUES (1, 'Chamados que você observa '),
	       (1, 'Chamados atribuidos a você '),
	       (1, 'Chamados que você criou '),
               (1, 'Chamados novos em setores que você é responsável ');
INSERT INTO setor (campus_codigo, descricao, interface_ativo)
	VALUES (1, 'Informática', '''2:1'','),
	       (1, 'Jornalismo', '''2:1'','),
	       (1, 'Direção', '''2:1'','),
	       (1, 'Segurança', '''2:1'','),
	       (1, 'Eletrônica', '''2:1'','),
	       (1, 'Limpeza', '''2:1'','),
               (1, 'Administrador', ''),
               (1, 'Enfermagem', '''2:1'',''22:1'''),
               (1, 'NUPE', '''22:1'','),
               (1, 'CGAE', '''22:1'','),
	       (2, 'Jornalismo', '''2:1'',''22:1'''),
	       (2, 'Segurança', '''2:1'',''22:1'''),
               (2, 'CGAE', '''2:1'',''22:1'''),
	       (2, 'Administrador','');
INSERT INTO tipo_servico (setor_codigo, descricao) 
	VALUES (1, 'Informática'), 
	       (2, 'Jornalismo'), 
	       (3, 'Direção'), 
	       (4, 'Segurança'), 
	       (5, 'Eletrônica'), 
	       (6, 'Limpeza');
INSERT INTO localizacao (campus_codigo, descricao) 
	VALUES (1, 'Laboratório 101'),
	       (1, 'Laboratório 102'),
	       (1, 'Laboratório 103'),
	       (1, 'Reprografia'),
	       (2, 'Reprografia');
INSERT INTO servico (tipo_servico_codigo, descricao_completa, descricao_curta)
	VALUES (1, 'Configurar a Impressora no linux', 'Configurar Impressora'),
               (5, 'Arrumar a Impressora que pegou fogo', 'Arrumar Impressora'),
               (1, 'Rede não funcionando', 'Configurar Proxy'),
               (1, 'Computador não liga, parece queimado', 'Computador não liga'),
               (5, 'Notebook não liga, parece queimado', 'Notebook não liga');
INSERT INTO patrimonio (setor_codigo, localizacao_codigo, fabricante_codigo, tipo_patrimonio_codigo, descricao, data_aquisicao, data_baixa, nota_fiscal, numero_etiqueta) 
	VALUES (1, 1, 1, 1, 'MICRO01', '2011-12-06', null, '123.456', 'IFC123'), 
	       (1, 1, 1, 1, 'MICRO02', '2011-12-06', null, '123.456', 'IFC124'), 
	       (1, 2, 1, 1, 'MICRO03', '2011-12-06', null, '123.456', 'IFC125'), 
	       (1, 3, 1, 5, 'NOTE01', '2011-12-06', null, '123.456', 'IFC127'), 
	       (1, 1, 4, 5, 'NOTE02', '2011-12-06', null, '123.456', 'IFC128'), 
	       (5, 3, 2, 6, 'MONI01', '2011-12-06', null, '123.456', 'IFC131'), 
     	       (5, 3, 3, 3, 'MULT01', '2011-12-06', '2012-12-07', '123.456', 'IFC150'), 
	       (5, 3, 4, 3, 'MULT02', '2011-12-06', null, '123.456', 'IFC151'), 
	       (4, 4, 1, 8, 'POT03', '2011-12-06', '2012-12-07', '123.456', 'IFC254');
INSERT INTO usuario (nome, email, "login", senha, telefone, data_nascimento, estado, verificacao, ultimo_campus_codigo) 
	VALUES ('Administrador da Silva', 'adm.silva@ifc.com', 'admin', '21232f297a57a5a743894a0e4a801fc3', '(47) 3523-0648', '1983-08-03', 1, 'dASKlA@d#', 1),
	       ('Usuario da Silva', 'usu.silva@ifc.com', 'usuario', '202cb962ac59075b964b07152d234b70', '(47) 3523-0649', '1980-01-12', 1, 'dASKlA@d#', 1),
	       ('Usuario de Souza', 'usu.souza@ifc.com', 'usu', '202cb962ac59075b964b07152d234b70', '(47) 3523-0649', '1980-01-12', 1, 'dASKlA@d#a', 1);
INSERT INTO usuario_setor (usuario_codigo, setor_codigo, funcao_codigo) 
	VALUES (1, 1, 1), (1, 2, 1), (1, 3, 1), (1, 4, 1), 
	       (1, 5, 1), (1, 6, 1), (1, 7, 1), (1, 8, 1), 
	       (2, 1, 2), (2, 5, 1), (3, 8, 3), (3, 4, 1),
	       (1, 9, 1), (1, 10, 1);	
INSERT INTO chamado (usuario_codigo_autor, usuario_codigo_atribuido, status_codigo, titulo, descricao, data_hora_abertura, data_hora_fechamento, data_prevista, prioridade_codigo, percentual, tipo_chamado) 
	VALUES (1, 1, 1, 'Chamado 1', 'Projetor Estragado', '2011-12-06 01:03:20', null, '2011-12-07', 2, 20, '1'), 
	       (1, 1, 2, 'Chamado 2', 'Computador Estragado', '2011-12-06 01:03:20', null, '2011-12-07', 1, 30, '1'), 
	       (1, 1, 3, 'Chamado 3', 'Computador Estragado', '2011-12-06 01:03:20', null, '2011-12-07', 2, 30, '1'),  
	       (1, 1, 3, 'Chamado 4', 'Computador Estragado', '2011-12-06 01:03:20', null, '2011-12-07', 3, 20, '1'),  
	       (1, 1, 6, 'Chamado 5', 'Computador Estragado', '2011-12-06 01:03:20', '2011-12-07 23:03:20', '2011-12-07', 1, 100,'1'),  
	       (1, 1, 6, 'Chamado 6', 'Computador Estragado', '2011-12-06 01:03:20', '2011-12-07 23:03:20', '2011-12-07', 1, 100, '1'),  
	       (1, 2, 7, 'Chamado 7', 'Computador Estragado', '2011-12-06 01:03:20', null, '2011-12-07', 2, 80, '1'), 
	       (1, 2, 4, 'Chamado 8', 'NoteBook Estragado', '2011-12-06 01:03:20', null, '2011-12-07', 1, 10, '1'), 
	       (1, 2, 5, 'Chamado 9', 'Problema Impressora 1', '2011-12-06 01:03:20', null, '2011-12-07', 1, 40, '2'), 
	       (1, 2, 7, 'Chamado 10', 'Problema Impressora 2', '2011-12-06 01:03:20', null, '2011-12-07', 2, 100, '2'), 
	       (1, 2, 3, 'Chamado 11', 'Problema Rede', '2011-12-06 01:03:20', null, '2011-12-07', 3, 80, '2'), 
	       (2, 2, 1, 'Chamado 12', 'Problema PC', '2011-12-06 01:03:20', null, '2011-12-07', 2, 80, '2'), 
	       (2, 2, 1, 'Chamado 13', 'Problema Note', '2011-12-06 01:03:20', null, '2011-12-07', 2, 80, '2'), 
	       (1, 3, 1, 'Chamado 14', 'Problema Impressora 3', '2011-12-06 01:03:20', null, '2011-12-07', 3, 10, '2');
INSERT INTO chamado_infra (chamado_codigo, patrimonio_codigo)
	VALUES (1, 7), (2, 1), (3, 1), (4, 1), 
	       (5, 1), (6, 1), (7, 1), (8, 4);
INSERT INTO chamado_servico (chamado_codigo, servico_codigo)
	VALUES (9, 1), (10, 2), (11, 3), 
               (12, 4), (13, 5), (14, 1);
INSERT INTO observador (usuario_codigo, chamado_codigo) 
	VALUES (1, 1), (1, 2), (1, 3), (1, 4),
	       (2, 1), (2, 2), (2, 4), (2, 5);
INSERT INTO hora (usuario_codigo, chamado_codigo, data_trabalho, tempo, descricao)
	VALUES (1, 1, '2011-12-07', 4, 'trabalhando no chamado'),
	       (1, 1, '2011-12-07', 2, 'finalizando o chamado'),
	       (1, 1, '2011-12-07', 2.4,'reabrindo chamado'),
	       (1, 1, '2011-12-07', 3.1,'fechando chamado definitivamente'),
	       (1, 2, '2011-12-07', 7,'trabalhando no chamado'),
	       (1, 2, '2011-12-07', 1.3,'finalizando o chamado'),
	       (1, 3, '2011-12-07', 5.4,'trabalhando no chamado'),
	       (1, 3, '2011-12-07', 1,'finalizando o chamado'),
	       (1, 4, '2011-12-07', 2,'trabalhando no chamado'),
	       (1, 4, '2011-12-07', 3,'finalizando o chamado'),
	       (2, 5, '2011-12-07', 4.4,'trabalhando no chamado'),
	       (2, 5, '2011-12-07', 0.6,'finalizando o chamado'),
	       (2, 6, '2011-12-07', 9.1,'trabalhando no chamado'),
	       (2, 6, '2011-12-07', 0.2,'finalizando o chamado');
INSERT INTO andamento (chamado_codigo, usuario_codigo, descricao, data_hora, "log") 
	VALUES (1, 1, 'Em Execução', '2011-12-07 18:03:20', null),
	       (1, 2, 'Aguardando resposta', '2011-12-07 18:03:20', null),
               (2, 1, 'Em Execução', '2011-12-07 18:03:20', null),
               (2, 1, 'Realizando testes', '2011-12-07 18:03:20', null),
               (3, 1, 'Em Execução', '2011-12-07 18:03:20', null),
               (3, 1, 'Problema não encontrado', '2011-12-07 18:03:20', null),
               (4, 1, 'Em Execução', '2011-12-07 18:03:20', null),
               (4, 2, 'Não tive tempo de fazer nada', '2011-12-07 18:03:20', null),
               (5, 1, 'Em Execução', '2011-12-07 18:03:20', null),
               (5, 2, 'Problema confirmado', '2011-12-07 18:03:20', null),
               (6, 1, 'Em Execução', '2011-12-07 18:03:20', null),
               (7, 1, 'Em Execução', '2011-12-07 18:03:20', null),
               (8, 1, 'Em Execução', '2011-12-07 18:03:20', null),
               (9, 1, 'Em Execução', '2011-12-07 18:03:20', null),
               (10, 1, 'Em Execução', '2011-12-07 18:03:20', null),
               (11, 1, 'Em Execução', '2011-12-07 18:03:20', null),
               (12, 1, 'Em Execução', '2011-12-07 18:03:20', null);
INSERT INTO interface (interface_codigo, descricao_completa, descricao_curta, posicao_menu, url, icone, cor) 
	VALUES (null, 'Infraestrutura', 'InfraGeral', 1, '', '', '#000000'),	
	       (18, 'Chamado', 'chamado', 3, 'chamadoPesquisar', '/imagens/img3', '#000000'), 
               (1, 'Unidade', 'campus', 4, 'campusPesquisar', '/imagens/img3', '#000000'), 
	       (1, 'Fabricante', 'fabricante', 5, 'fabricantePesquisar', '/imagens/img3', '#000000'), 
	       (18, 'Função', 'funcao', 6, 'funcaoPesquisar','/imagens/img3', '#000000'), 
	       (1, 'Localização', 'localizacao', 7, 'localizacaoPesquisar','/imagens/img3', '#000000'), 
	       (1, 'Patrimônio', 'patrimonio', 8, 'patrimonioPesquisar','/imagens/img3', '#000000'), 
	       (18, 'Permissão', 'permissao', 9, 'permissaoPesquisar','/imagens/img3', '#000000'), 
	       (1, 'Setor', 'setor', 10, 'setorPesquisar','/imagens/img3', '#000000'), 
	       (1, 'Tipo Patrimônio', 'tipoPatrimonio', 11, 'tipoPatrimonioPesquisar','/imagens/img3', '#000000'),      
	       (1, 'Usuário', 'usuario', 12, 'usuarioPesquisar', '/imagens/img3', '#000000'), 
	       (18, 'Serviço', 'servico', 13, 'servicoPesquisar', '/imagens/img3', '#000000'),
	       (18, 'Tipo Serviço', 'tipoServico', 14, 'tipoServicoPesquisar', '/imagens/img3', '#000000'),
	       (18, 'Víncular Usuário', 'vincularUsuarioSetor', 15, 'vincularUsuarioSetorPesquisar', '/imagens/img3', '#000000'),
	       (18, 'Gráfico', 'grafico', 16, 'graficoPesquisar', '/imagens/img3', '#000000'),
	       (18, 'Relatórios', 'relatorio', 17, 'relatorioPesquisar', '/imagens/img3', '#000000'),
	       (1,'Publicidade', 'publicidade',18, 'publicidadePesquisar','/imagens/img3', '#000000'),
	       (null,'Controle de Manutenção','CONTM',2,'','','#000000'),
	       (18,'Reserva de Recursos', 'reservaRecurso', 19, 'reservaRecursoPesquisar', '/images/img3', '#000000'),
               (null,'Controle dos Internos', 'CONTI', 20, '', '/images/img3', '#000000'),
               (20,'Sincronização de Dados', 'migracao', 21, 'sincronizacao', '/images/img3', '#000000'),
               (20,'Ocorrência', 'ocorrencia', 22, 'ocorrenciaPesquisar', '/images/img3', '#000000'),
               (20,'Ação Disciplinar', 'acaoDisciplinar', 23, 'acaoDisciplinarPesquisar', '/images/img3', '#000000');
INSERT INTO permissao (interface_codigo, tipo_permissao_codigo, descricao) 
	VALUES (1, 5, 'Vêr menu'),
	(2, 1, 'Pesquisar'),
	(3, 1, 'Pesquisar'), (3, 2, 'Incluir'),(3, 3, 'Alterar'),(3, 4, 'Excluir'),
	(4, 1, 'Pesquisar'), (4, 2, 'Incluir'),(4, 3, 'Alterar'),(4, 4, 'Excluir'), 
	(5, 1, 'Pesquisar'), (5, 2, 'Incluir'),(5, 3, 'Alterar'),(5, 4, 'Excluir'), 
	(6, 1, 'Pesquisar'), (6, 2, 'Incluir'),(6, 3, 'Alterar'),(6, 4, 'Excluir'),
	(7, 1, 'Pesquisar'), (7, 2, 'Incluir'),(7, 3, 'Alterar'),(7, 4, 'Excluir'),
	(8, 1, 'Pesquisar'), (8, 2, 'Incluir'),(8, 3, 'Alterar'),(8, 4, 'Excluir'),
	(9, 1, 'Pesquisar'), (9, 2, 'Incluir'),(9, 3, 'Alterar'),(9, 4, 'Excluir'),(9, 7, 'Adicionar setores para outras unidades'),
	(10, 1, 'Pesquisar'), (10, 2, 'Incluir'),(10, 3, 'Alterar'),(10, 4, 'Excluir'),
	(11, 1, 'Pesquisar'), (11, 2, 'Incluir'),(11, 3, 'Alterar'),
	(12, 1, 'Pesquisar'), (12, 2, 'Incluir'),(12, 3, 'Alterar'),(12, 4, 'Excluir'),
	(13, 1, 'Pesquisar'), (13, 2, 'Incluir'),(13, 3, 'Alterar'),(13, 4, 'Excluir'),
	(14, 1, 'Pesquisar'), (14, 2, 'Incluir'),(14, 3, 'Alterar'),(14, 4, 'Excluir'),
        (15, 1, 'Pesquisar'),
	(16, 1, 'Pesquisar'),
	(17, 1, 'Pesquisar'), (17, 2, 'Incluir'),(17, 3, 'Alterar'),(17, 4, 'Excluir'),
	(18, 5,'Vêr Menu'),
	(19, 1,'Pesquisar'),(19, 4,'Excluir'),
        (20, 5,'Vêr Menu'),
        (21, 1,'Sincronizacão'),(21, 6,'Agendar Sincronizacão'),(21, 7,'Configurar Base de Dados'),
        (22, 1,'Pesquisar'),(22, 2,'Incluir'),(22, 3,'Alterar'),
        (2, 2,'Incluir'),
        (23, 1,'Pesquisar'),(23, 2,'Incluir'),(23, 3,'Alterar'), (23,4,'Excluir');
INSERT INTO usuario_permissao (permissao_codigo, usuario_codigo)
	VALUES (1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1), (8, 1),
	       (9, 1), (10, 1), (11, 1), (12, 1), (13, 1), (14, 1), (15, 1), 
	       (16, 1),(17, 1), (18, 1), (19, 1), (20, 1), (21, 1), (22, 1), (23, 1), (24, 1),
	       (25, 1), (26, 1), (27, 1), (28, 1), (29, 1), (30, 1), (31, 1), (32, 1),
               (33, 1), (34, 1), (35, 1), (36, 1), (37, 1), (38, 1), (39, 1), (40, 1),
               (41, 1), (42, 1), (43, 1), (44, 1), (45, 1), (46, 1), (47, 1), (48, 1),
               (49, 1), (50, 1), (51, 1), (52, 1), (53, 1), (54, 1),(55, 1),(56, 1),(57, 1),(58,1),
               (59,1),(60,1), (61,1), (62,1), (63,1), (64,1), (65,1),(66,1),(67,1),(68,1),(69,1),(70,1),(71,1),
               (15, 2), (13, 2), (14, 2), (66,2),
  	       (16, 2), (17, 2), (18, 3), (19, 3), (20, 3), (21, 3), (22, 3),(1,2),(1,3),(66,3);

-- Database: intranetifc


--SCRIPT ADICIONAIS V3.0

CREATE TABLE curso(
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT curso_pkey PRIMARY KEY (codigo)
);

create table tipo_ocorrencia(
  codigo serial NOT NULL,
   descricao character varying(100) NOT NULL,
  CONSTRAINT tipo_ocorrencia_pkey PRIMARY KEY (codigo)
);

CREATE TABLE acao_disciplinar(
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  cor_html CHARACTER VARYING(45),
  tipo_ocorrencia_codigo INTEGER NOT NULL,
  CONSTRAINT acao_disciplinar_pkey PRIMARY KEY (codigo),
  CONSTRAINT ref_tipo_ocorrencia FOREIGN KEY (tipo_ocorrencia_codigo)
      REFERENCES tipo_ocorrencia(codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

  
CREATE TABLE status_ocorrencia(
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(255) NOT NULL,
  CONSTRAINT status_ocorrencia_pkey PRIMARY KEY (codigo)
);


CREATE TABLE tipo_telefone(
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(200) NOT NULL,
  CONSTRAINT tipo_telefone_pkey PRIMARY KEY (codigo)
);


CREATE TABLE unidade_federativa(
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(200) NOT NULL,
  sigla CHARACTER VARYING(100)NOT NULL,
  CONSTRAINT unidade_federativa_pkey PRIMARY KEY (codigo)
);

CREATE TABLE municipio(
  codigo SERIAL NOT NULL,
  unidade_federativa_codigo INTEGER NOT NULL,
  descricao CHARACTER VARYING(200) NOT NULL,
  CONSTRAINT municipio_pkey PRIMARY KEY (codigo)
);

CREATE TABLE tipo_logradouro(
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(200) NOT NULL,
  CONSTRAINT tipo_logradouro_pkey PRIMARY KEY (codigo)
);

CREATE TABLE dia_semana(
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(50) NOT NULL,
  sigla CHARACTER VARYING(50) NOT NULL,
  CONSTRAINT dia_semana_pkey PRIMARY KEY (codigo)
);

CREATE TABLE agendamento(
  codigo SERIAL NOT NULL,
  horario time without time zone NOT NULL,
  dia_semana_codigo INTEGER NOT NULL,
  CONSTRAINT agendamento_pkey PRIMARY KEY (codigo),
  CONSTRAINT dia_semana_fk FOREIGN KEY (dia_semana_codigo)
      REFERENCES dia_semana (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE endereco(
  codigo SERIAL NOT NULL,
  tipo_logradouro_codigo INTEGER NOT NULL,
  municipio_codigo INTEGER NOT NULL,
  nome_logradouro CHARACTER VARYING(200) NOT NULL,
  complemento CHARACTER VARYING(200),
  caixa_postal CHARACTER VARYING(200),
  bairro CHARACTER VARYING(200) NOT NULL,
  cep INTEGER NOT NULL,
  numero CHARACTER VARYING(10)NOT NULL,
   CONSTRAINT endereco_pkey PRIMARY KEY (codigo),
  CONSTRAINT municipio_fk FOREIGN KEY (municipio_codigo)
      REFERENCES municipio (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT tipo_logradouro_fk FOREIGN KEY (tipo_logradouro_codigo)
      REFERENCES tipo_logradouro (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE sexo(
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(200)NOT NULL,
  sigla CHARACTER VARYING(50)NOT NULL,
  CONSTRAINT sexo_pkey PRIMARY KEY (codigo)
);

CREATE TABLE tipo_sanguineo(
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(100) NOT NULL,
  CONSTRAINT tipo_sanguineo_pkey PRIMARY KEY (codigo)
);


CREATE TABLE necessidade_especial(
  codigo SERIAL NOT NULL,
  descricao CHARACTER VARYING(200) NOT NULL,
  CONSTRAINT necessidade_especial_pkey PRIMARY KEY (codigo)
);


CREATE TABLE aluno(
  codigo SERIAL NOT NULL,
  data_nascimento date NOT NULL,
  nome_pai CHARACTER VARYING(200),
  nome_mae CHARACTER VARYING(200),
  email CHARACTER VARYING(255),
  sexo_codigo INTEGER NOT NULL,
  tipo_sanguineo_codigo INTEGER,
  endereco_codigo INTEGER NOT NULL,
  nome CHARACTER VARYING(200) NOT NULL,
  id_siga INTEGER NOT NULL,
  foto bytea,
  CONSTRAINT aluno_pkey PRIMARY KEY (codigo),
  CONSTRAINT endereco_aluno_fk FOREIGN KEY (endereco_codigo)
      REFERENCES endereco (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT sexo_aluno_fk FOREIGN KEY (sexo_codigo)
      REFERENCES sexo (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT tipo_sanguineo_aluno_fk FOREIGN KEY (tipo_sanguineo_codigo)
      REFERENCES tipo_sanguineo (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE aluno_necessidade_especial(
  aluno_codigo INTEGER NOT NULL,
  necessidade_especial_codigo INTEGER NOT NULL,
  CONSTRAINT aluno_necessidade_especial_pkey PRIMARY KEY (aluno_codigo, necessidade_especial_codigo),
  CONSTRAINT necessidade_especial_aluno_fk FOREIGN KEY (necessidade_especial_codigo)
      REFERENCES necessidade_especial (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT aluno_necessidade_especial_fk FOREIGN KEY (aluno_codigo)
      REFERENCES aluno (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE ocorrencia(
  codigo SERIAL NOT NULL,
  setor_codigo_autor INTEGER NOT NULL,
  setor_codigo_atribuido INTEGER,
  status_ocorrencia_codigo INTEGER NOT NULL,
  descricao text NOT NULL,
  data_hora_abertura timestamp without time zone,
  data_hora_fechamento timestamp without time zone,
  data_prevista date,
  prioridade_codigo INTEGER NOT NULL,
  percentual INTEGER NOT NULL,
  aluno_codigo INTEGER NOT NULL,
  acao_disciplinar_codigo INTEGER,
  titulo CHARACTER VARYING(45) NOT NULL,
  CONSTRAINT ocorrencia_pkey PRIMARY KEY (codigo),
  CONSTRAINT ref_ocorrencia_to_atribuido FOREIGN KEY (setor_codigo_atribuido)
      REFERENCES setor (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ref_ocorrencia_to_autor FOREIGN KEY (setor_codigo_autor)
      REFERENCES setor (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ref_ocorrencia_to_status_ocorrencia FOREIGN KEY (status_ocorrencia_codigo)
      REFERENCES status_ocorrencia (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT "ref_ocorrencia_to_prioridade" FOREIGN KEY (prioridade_codigo)
      REFERENCES prioridade (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ref_ocorrencia_to_aluno FOREIGN KEY (aluno_codigo)
      REFERENCES aluno (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT ref_acao_disciplinar FOREIGN KEY (acao_disciplinar_codigo)
	REFERENCES acao_disciplinar (codigo) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE andamento_ocorrencia(
  codigo SERIAL NOT NULL,
  ocorrencia_codigo INTEGER NOT NULL,
  descricao text,
  usuario_codigo INTEGER NOT NULL,
  log CHARACTER VARYING(400),
  data_hora_ocorrencia timestamp without time zone NOT NULL,
  CONSTRAINT andamento_ocorrencia_pkey PRIMARY KEY (codigo),
  CONSTRAINT "ref_andamento_ocorrencia_to_usuario" FOREIGN KEY (usuario_codigo)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ref_andamento_ocorrencia_to_ocorrencia FOREIGN KEY (ocorrencia_codigo)
      REFERENCES ocorrencia (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE arquivo_ocorrencia(
  codigo SERIAL NOT NULL,
  ocorrencia_codigo INTEGER NOT NULL,
  arquivo_ocorrencia bytea,
  nome CHARACTER VARYING(45),
  CONSTRAINT arquivo_ocorrencia_pkey PRIMARY KEY (codigo),
  CONSTRAINT ref_arquivo_ocorrencia_to_ocorrencia FOREIGN KEY (ocorrencia_codigo)
      REFERENCES ocorrencia (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE observador_setor(
  ocorrencia_codigo INTEGER NOT NULL,
  setor_codigo INTEGER NOT NULL,
  CONSTRAINT observador_setor_pkey PRIMARY KEY (ocorrencia_codigo, setor_codigo),
  CONSTRAINT ref__setor_ocorrencia_to_setor FOREIGN KEY (setor_codigo)
      REFERENCES setor (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT ref_ocorrencia_setor_to_ocorrencia FOREIGN KEY (ocorrencia_codigo)
      REFERENCES ocorrencia (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE telefone(
  codigo SERIAL NOT NULL,
  tipo_telefone_codigo INTEGER NOT NULL,
  codigo_area INTEGER NOT NULL,
  numero INTEGER NOT NULL,
  ramal INTEGER,
  aluno_codigo INTEGER NOT NULL,
  CONSTRAINT telefone_pkey PRIMARY KEY (codigo),
  CONSTRAINT telefone_fk FOREIGN KEY (tipo_telefone_codigo)
      REFERENCES tipo_telefone (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT aluno_fk FOREIGN KEY (aluno_codigo)
  REFERENCES aluno (codigo)  MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
  );

CREATE TABLE aluno_curso(
  aluno_codigo INTEGER NOT NULL,
  curso_codigo INTEGER NOT NULL,
  CONSTRAINT aluno_curso_pkey PRIMARY KEY (aluno_codigo, curso_codigo),
  CONSTRAINT curso_aluno_fk FOREIGN KEY (curso_codigo)
      REFERENCES curso (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT aluno_curso_fk FOREIGN KEY (aluno_codigo)
      REFERENCES aluno (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

INSERT INTO sexo (descricao, sigla)
	VALUES 
	('Feminino', 'F'), 
	('Masculino', 'M'),
	('Não declarado', 'Não declarado');

--Copiando dados dos arquivos
COPY unidade_federativa FROM '/home/vinicio/NetBeansProjects/versao3.0/Banco/unidade_federativa.csv' using delimiters ',';
COPY municipio FROM '/home/vinicio/NetBeansProjects/versao3.0/Banco/municipio.csv' using delimiters ',';
COPY necessidade_especial FROM '/home/vinicio/NetBeansProjects/versao3.0/Banco/necessidade_especial.csv' using delimiters ',';
COPY tipo_logradouro FROM '/home/vinicio/NetBeansProjects/versao3.0/Banco/tipo_logradouro.csv' using delimiters ',';
COPY tipo_sanguineo FROM '/home/vinicio/NetBeansProjects/versao3.0/Banco/tipo_sanguineo.csv' using delimiters ',';
COPY tipo_telefone FROM '/home/vinicio/NetBeansProjects/versao3.0/Banco/tipo_telefone.csv' using delimiters ',';

INSERT INTO dia_semana(
            descricao, sigla)
    VALUES ('DOMINGO', 'D'),
	   ('SEGUNDA', 'S'),
	   ('TERÇA-FEIRA', 'T'),
	   ('QUARTA-FEIRA', 'Q'),
	   ('QUINTA-FEIRA', 'Q'),
	   ('SEXTA-FEIRA', 'S'),
	   ('SÁBADO', 'S');

INSERT INTO agendamento(
            horario, dia_semana_codigo)
    VALUES ('00:00', 1),
           ('00:00', 2);
	
	
INSERT INTO tipo_ocorrencia(
            descricao)
    VALUES ('Normal'),
           ('Disciplinar');
           
INSERT INTO acao_disciplinar (descricao, cor_html,tipo_ocorrencia_codigo) 
	VALUES 	('Leve', '00FF00',2), 	
	('Media', 'FFFF00',2), 
	('Grave', 'FFFF00', 2),	
	('Gravíssimo', 'FF0000',2),
	('Acompanhamento ao aluno', '00FF00',1),
	('Reforço ao aluno', '00FF00',1),
	('Saúde', '00FF00', 1);

INSERT INTO status_ocorrencia (descricao)
       VALUES ('Aberto'),
       ('Andamento'),
       ('Arquivado'),
       ('Fechado');

--SCRIPT ADICIONAIS V3.1

INSERT INTO interface(
            codigo, interface_codigo, descricao_completa, descricao_curta, 
            posicao_menu, url, icone, cor)
    VALUES (24, 20, 'Relatório Ocorrencia', 'relatorioOcorrencia', 
            24, 'relatorioOcorrencia', '/images/img3', '#000000');

INSERT INTO permissao(
             tipo_permissao_codigo, interface_codigo, descricao)
    VALUES (1, 24, 'Emitir Relatório');

INSERT INTO usuario_permissao (permissao_codigo, usuario_codigo)
    VALUES (72,1);

/*Inserts de Teste
INSERT INTO unidade_federativa (descricao, sigla) 
	VALUES 
	('Santa Catarina', 'SC'), 
	('Rio de Janeiro', 'RJ'), 
	('Rio Grande do Sul', 'RG');
INSERT INTO municipio (unidade_federativa_codigo, descricao) 
	VALUES 
	(1, 'Rio do Sul'), 
	(1, 'Ituporanga'), 
	(1, 'Joinvlille'), 
	(1, 'Florianópolis');
INSERT INTO necessidade_especial (descricao) 
	VALUES ('Cegueira'), 
	('Baixa Visão');
INSERT INTO tipo_logradouro (descricao) 
	values ('Beco'), 
	('Avenida');
INSERT INTO endereco (tipo_logradouro_codigo, municipio_codigo, nome_logradouro, complemento, caixa_postal, bairro, cep, numero) 
	VALUES
	(1,1,'Presidente Kenedy', 'Casa', null, 'Jardim America', 89160000, 686), 
	(1,1, 'Oscar Barcelos', 'Apartamento', null, 'Centro', 89160000, 123);
INSERT INTO aluno (data_nascimento, nome_pai, nome_mae, email, sexo_codigo, tipo_sanguineo_codigo, endereco_codigo, nome, foto)
	VALUES 
	('1991-02-02', 'Marcelo Antonio', 'Claudineia Pereira', 'nao@tenho.com', 1, 1, 1, 'José serra', 'cafe'),
        ('1988-02-02', 'Edson', 'Ileane', 'milaenz@gmail.com', 1, 1, 1, 'Camila', 'cafea');

 
INSERT INTO ocorrencia (setor_codigo_autor, setor_codigo_atribuido, status_ocorrencia_codigo, acao_disciplinar_codigo, descricao, data_hora_abertura, data_hora_fechamento, data_prevista_fechamento, prioridade_codigo, percentual,aluno_codigo, tipo_ocorrencia, título)
	VALUES 
	(1,2,3,2,'aluno caiu e precisa de ajuda medica', '2013-12-06', null, '2014-12-06 01:03:20', 1,1, 1,1, "queda"),
	(2,1,2,1,'O aluno cometeu falta gravissima', '2014-12-06 01:03:20', null, '2013-11-27',1, 1, 2,2, "bate boca"); 
INSERT INTO arquivo_ocorrencia ( ocorrencia_codigo, arquivo_ocorrencia, nome) 
	VALUES (1,null, 'infracao aluno x');
INSERT INTO observador_setor (ocorrencia_codigo, setor_codigo) 
	VALUES 
	(2,1);
INSERT INTO telefone (tipo_telefone_codigo, codigo_area, numero, ramal, aluno_codigo) 
	VALUES (1, 47, 35250220, null,1),
	(2, 47, 99585858, null,2), 
	(3, 47, 35212531, null,1);
INSERT INTO curso (descricao) 
	VALUES ('CIENCIA DA COMPUTACAO'),
        ('ENGENHARIA AGRONOMICA (ENXADA BOYS)');
INSERT INTO aluno_curso (aluno_codigo, curso_codigo)
        VALUES (1,1), (1,2);*/
