-- INSERT INTO mdfe.pessoa (tenantid, idempresapadrao, nome, email, tipousuario, cpf, senhausuario, flagativo, flagdel)
-- VALUES ('mdfe',1,'Administrador','suporte@3gbrasil.com.br','OPERADOR  ','04660621928','$2a$10$XtIdHRsPH723f9rrkeO5KursLsFI8MqvgYY8Ix3WmiCRq.CM1Rh.y',TRUE,FALSE);

-- INSERT INTO public.pessoa (tenantid, idempresapadrao, nome, email, tipousuario, cpf, senhausuario, flagativo, flagdel, idpessoatenant)
-- VALUES ('mdfe',1,'Administrador','suporte@3gbrasil.com.br','OPERADOR  ','04660621928','$2a$10$XtIdHRsPH723f9rrkeO5KursLsFI8MqvgYY8Ix3WmiCRq.CM1Rh.y',TRUE,FALSE,1);

-- insert into mdfe.pessoa_grupo (idpessoa, idgrupo) values (1,1);

CREATE SEQUENCE empresa_idempresa_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

create table empresa(
	idempresa bigint NOT NULL DEFAULT nextval('empresa_idempresa_seq'::regclass),
	cnpj character varying(14) NOT NULL,
	inscricaoestadual character varying(20) NOT NULL,
	razaosocial character varying(80) NOT NULL,
	fantasia character varying(80) NOT NULL,
	email character varying(200) NOT NULL,
	fone character varying(15),
	inscricaoisenta boolean,
	tipoambiente character varying(25) NOT NULL,
	tp_transp character varying(5),
	tpemis character varying(15),
	rntrc bigint,
	tipoemitente character varying(40),
	cep character varying(12),
	uf character varying(2),
	logradouro character varying(80),
	complemento character varying(80),
	pais character varying(40),
	municipio character varying(60),
	numero character varying(8),
	bairro character varying(60),
	observacao character varying(200),
	flagdel boolean NOT NULL,
	codigomunicipio bigint NOT NULL,
	flagcanalverde boolean NOT NULL DEFAULT FALSE,
	CONSTRAINT "PK_EMPRESA_IDEMPRESA" PRIMARY KEY (idempresa)
);

CREATE SEQUENCE pessoa_idpessoa_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE pessoa
(
  idpessoa bigint NOT NULL DEFAULT nextval('pessoa_idpessoa_seq'::regclass),
  tenantid character varying(25) NOT NULL,
  idempresapadrao int NOT NULL,
  email character varying(200) NOT NULL,
  cpf character varying(11),
  fone character varying(15),
  tipousuario character varying(40) NOT NULL,
  nome character varying(60) NOT NULL,
  senhausuario character varying(100),
  flagativo boolean,
  flagdel boolean,
  CONSTRAINT pessoa_pkey PRIMARY KEY (idpessoa),
  CONSTRAINT pessoa_ukey UNIQUE (email)
);

create table pessoa_empresa(
  idpessoa bigint,
  idempresa bigint,
  flagpadrao boolean,
  CONSTRAINT fk_pessoa_idpessoa FOREIGN KEY (idpessoa)
  REFERENCES pessoa (idpessoa) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_empresa_idempresa FOREIGN KEY (idempresa)
  REFERENCES empresa (idempresa) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE grupo_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

create table grupo(
  idgrupo bigint NOT NULL DEFAULT nextval('grupo_seq'::regclass),
  nome varchar(40) NOT NULL,
  flagdel boolean,
  CONSTRAINT grupo_pk PRIMARY KEY (idgrupo)
);

CREATE SEQUENCE permissao_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

create table permissao(
  idpermissao bigint NOT NULL DEFAULT nextval('permissao_seq'::regclass),
  nome varchar(40) NOT NULL,
  descricao varchar(100) NOT NULL,
  grupo varchar(40) NOT NULL,
  flagvisivel boolean NOT NULL,
  CONSTRAINT permissao_pk PRIMARY KEY (idpermissao)
);

create table grupo_permissao(
  idgrupo bigint,
  idpermissao bigint,
  CONSTRAINT ukey_grupo_permissao UNIQUE (idgrupo, idpermissao),
  CONSTRAINT fk_grupo_permissao_idgrupo FOREIGN KEY (idgrupo)
  REFERENCES grupo (idgrupo) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_grupo_permissao_idpermissao FOREIGN KEY (idpermissao)
  REFERENCES permissao (idpermissao) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

create table pessoa_grupo(
  idpessoa bigint,
  idgrupo bigint,
  CONSTRAINT fk_pessoa_grupo_idpessoa FOREIGN KEY (idpessoa)
  REFERENCES pessoa (idpessoa) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_grupo_permissao_idgrupo FOREIGN KEY (idgrupo)
  REFERENCES grupo (idgrupo) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE motorista_idmotorista_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

create table motorista(
	idmotorista bigint NOT NULL DEFAULT nextval('motorista_idmotorista_seq'::regclass),
	nome character varying(60) NOT NULL,
	cpf character varying(11) NOT NULL,
	senhamotorista character varying(100),
	rntrc character varying(8),
	telefone character varying(20),
	observacao character varying(200),
	flagativo boolean NOT NULL,
	flagdel boolean NOT NULL,
	uf character varying(2) DEFAULT NULL,
	CONSTRAINT "PK_MOTORISTA_IDMOTORISTA" PRIMARY KEY (idmotorista)
);

CREATE SEQUENCE proprietario_idproprietario_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE proprietario
(
  idproprietario bigint NOT NULL DEFAULT nextval('proprietario_idproprietario_seq'::regclass),
  idmotorista bigint,
  cpfcnpj character varying(14),
  rntrc character varying(8),
  nome character varying(60),
  ie character varying(14),
  uf character varying(2),
  tp_prop character varying(25),
  flagdel boolean NOT NULL,
  CONSTRAINT "pk_proprietario_idproprietario" PRIMARY KEY (idproprietario),
  CONSTRAINT fk_proprietario_motorista_idmotorista FOREIGN KEY (idmotorista)
  REFERENCES motorista (idmotorista) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE veiculo_idveiculo_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE veiculo
(
  idveiculo bigint NOT NULL DEFAULT nextval('veiculo_idveiculo_seq'::regclass),
  idproprietario bigint,
  placa character varying(7) NOT NULL,
  renavam character varying(20) NOT NULL,
  capacidadem3 bigint,
  capacidadekg bigint,
  tara bigint,
  veiculotipo character varying(40),
  tiporodado character varying(40),
  tipocarroceria character varying(40),
  tipopropriedade character varying(40),
  uf character varying(2),
  observacao character varying(200),
  flagdel boolean NOT NULL,
  CONSTRAINT "PK_VEICULO_IDVEICULO" PRIMARY KEY (idveiculo),
  CONSTRAINT fk_veiculo_proprietario_idproprietario FOREIGN KEY (idproprietario)
  REFERENCES proprietario (idproprietario) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

insert into grupo (nome,flagdel) values ('3GBRASIL',false);
insert into grupo (nome,flagdel) values ('ADMINISTRADORES',false);
insert into grupo (nome,flagdel) values ('MOTORISTAS',false);
insert into grupo (nome,flagdel) values ('OPERADORES',false);

INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_ATUALIZAR_CACERTS', 'Atualizar cacerts.', 'UTILITARIO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CADASTRAR_CERTIFICADO', 'Cadastrar certificado.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_CERTIFICADO', 'Consultar certificado.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CADASTRAR_CIOT', 'Cadastrar CIOT.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_CIOT', 'Consultar CIOT.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_DAMDFE', 'Gerar os bytes da DAMDFE.', 'MOVIMENTACAO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CADASTRAR_EMPRESA', 'Cadastrar empresa.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_EMPRESA', 'Consultar empresa.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CADASTRAR_GRUPO', 'Cadastrar grupo.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_GRUPO', 'Consultar grupo.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_SINCRONIZAR_IBGE', 'Sincronizar o serviço do IBGE, atualizar todas as cidades.', 'UTILITARIO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CADASTRAR_MDFE', 'Cadastrar manifesto.', 'MOVIMENTACAO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_MDFE', 'Consultar manifesto.', 'MOVIMENTACAO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CADASTRAR_MOTORISTA', 'Cadastrar motorista.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_MOTORISTA', 'Consultar motorista.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_PERMISSAO', 'Consultar permissão.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CADASTRAR_PESSOA', 'Cadastrar pessoa.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_PESSOA', 'Consultar pessoa.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CADASTRAR_SEGURADORA', 'Cadastrar seguradora.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_SEGURADORA', 'Consultar seguradora.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CADASTRAR_USUARIO', 'Cadastrar usuário.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_USUARIO', 'Consultar usuário.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CADASTRAR_VEICULO', 'Cadastrar veículo.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_VEICULO', 'Consultar veículo.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_PAINEL_ADM', 'Acesso ao painel Administrativo.', 'PAINELADM', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CADASTRAR_CONFIGURACAO', 'Cadastrar configuração.', 'CADASTRO', true);
INSERT INTO PERMISSAO (IDPERMISSAO, NOME, DESCRICAO, GRUPO, FLAGVISIVEL) VALUES (DEFAULT, 'ROLE_CONSULTAR_CONFIGURACAO', 'Consultar configuração.', 'CADASTRO', true);

--Permissoes do grupo G3Brasil
insert into grupo_permissao (idgrupo, idpermissao) values (1,1);
insert into grupo_permissao (idgrupo, idpermissao) values (1,2);
insert into grupo_permissao (idgrupo, idpermissao) values (1,3);
insert into grupo_permissao (idgrupo, idpermissao) values (1,4);
insert into grupo_permissao (idgrupo, idpermissao) values (1,5);
insert into grupo_permissao (idgrupo, idpermissao) values (1,6);
insert into grupo_permissao (idgrupo, idpermissao) values (1,7);
insert into grupo_permissao (idgrupo, idpermissao) values (1,8);
insert into grupo_permissao (idgrupo, idpermissao) values (1,9);
insert into grupo_permissao (idgrupo, idpermissao) values (1,10);
insert into grupo_permissao (idgrupo, idpermissao) values (1,11);
insert into grupo_permissao (idgrupo, idpermissao) values (1,12);
insert into grupo_permissao (idgrupo, idpermissao) values (1,13);
insert into grupo_permissao (idgrupo, idpermissao) values (1,14);
insert into grupo_permissao (idgrupo, idpermissao) values (1,15);
insert into grupo_permissao (idgrupo, idpermissao) values (1,16);
insert into grupo_permissao (idgrupo, idpermissao) values (1,17);
insert into grupo_permissao (idgrupo, idpermissao) values (1,18);
insert into grupo_permissao (idgrupo, idpermissao) values (1,19);
insert into grupo_permissao (idgrupo, idpermissao) values (1,20);
insert into grupo_permissao (idgrupo, idpermissao) values (1,21);
insert into grupo_permissao (idgrupo, idpermissao) values (1,22);
insert into grupo_permissao (idgrupo, idpermissao) values (1,23);
insert into grupo_permissao (idgrupo, idpermissao) values (1,24);
insert into grupo_permissao (idgrupo, idpermissao) values (1,25);

--Permissoes do grupo Administrador
insert into grupo_permissao (idgrupo, idpermissao) values (2,1);
insert into grupo_permissao (idgrupo, idpermissao) values (2,2);
insert into grupo_permissao (idgrupo, idpermissao) values (2,3);
insert into grupo_permissao (idgrupo, idpermissao) values (2,4);
insert into grupo_permissao (idgrupo, idpermissao) values (2,5);
insert into grupo_permissao (idgrupo, idpermissao) values (2,6);
insert into grupo_permissao (idgrupo, idpermissao) values (2,7);
insert into grupo_permissao (idgrupo, idpermissao) values (2,8);
insert into grupo_permissao (idgrupo, idpermissao) values (2,9);
insert into grupo_permissao (idgrupo, idpermissao) values (2,10);
insert into grupo_permissao (idgrupo, idpermissao) values (2,11);
insert into grupo_permissao (idgrupo, idpermissao) values (2,12);
insert into grupo_permissao (idgrupo, idpermissao) values (2,13);
insert into grupo_permissao (idgrupo, idpermissao) values (2,14);
insert into grupo_permissao (idgrupo, idpermissao) values (2,15);
insert into grupo_permissao (idgrupo, idpermissao) values (2,16);
insert into grupo_permissao (idgrupo, idpermissao) values (2,17);
insert into grupo_permissao (idgrupo, idpermissao) values (2,18);
insert into grupo_permissao (idgrupo, idpermissao) values (2,19);
insert into grupo_permissao (idgrupo, idpermissao) values (2,20);
insert into grupo_permissao (idgrupo, idpermissao) values (2,21);
insert into grupo_permissao (idgrupo, idpermissao) values (2,22);
insert into grupo_permissao (idgrupo, idpermissao) values (2,23);
insert into grupo_permissao (idgrupo, idpermissao) values (2,24);
insert into grupo_permissao (idgrupo, idpermissao) values (2,26);
insert into grupo_permissao (idgrupo, idpermissao) values (2,27);

--Permissoes do grupo motorista
insert into grupo_permissao (idgrupo, idpermissao) values (3,6);
insert into grupo_permissao (idgrupo, idpermissao) values (3,12);
insert into grupo_permissao (idgrupo, idpermissao) values (3,13);

-- Permissões do grupo operador.
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,4);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,5);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,6);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,8);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,12);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,13);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,14);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,15);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,18);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,19);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,20);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,22);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,23);
INSERT INTO GRUPO_PERMISSAO (IDGRUPO, IDPERMISSAO) VALUES (4,24);

CREATE SEQUENCE seguradora_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

create table seguradora(
  idseguradora bigint NOT NULL DEFAULT nextval('seguradora_seq'::regclass),
  cpfcnpjresponsavel character varying(14),
  nomeseguradora character varying(80),
  numeroapolice character varying(35),
  responsavel character varying(35),
  cnpjseguradora character varying(14),
  flagdel boolean,
  CONSTRAINT seguradora_pk PRIMARY KEY (idseguradora)
);


CREATE SEQUENCE mdfe_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE mdfe
(
    idmdfe bigint NOT NULL DEFAULT nextval('mdfe_seq'::regclass),
    idempresa bigint,
    idlote character varying(20),
    tpemis character varying(15),
    chave character varying(48),
    numrecibo character varying(30),
    numprotocolo character varying(30),
    ambienteenvio character varying (30),
    statusenvio character varying(10),
    motivoenvio character varying(50),
    ambienterecibo character varying (30),
    statusrecibo character varying(10),
    motivorecibo character varying(50),
    retornorecibo character varying (255),
    serie integer,
    nmdf character varying(9),
    cmdf character varying(8),
    cdv character varying(1),
    dhemi timestamp without time zone,
    dhiniviagem timestamp without time zone,
    uffim character varying(2),
    ufini character varying(2),
    xml bytea,
    versaomodal character varying(4),
    versaolayout character varying(4),
    infadfisco character varying(2000),
    infcpl character varying(5000),
    idveiculotracao bigint NOT NULL,
    situacao character varying(50),
    statusmdfe character varying(50),
    dtultimaconsultarecibo timestamp without time zone,
    CONSTRAINT mdfe_pkey PRIMARY KEY (idmdfe),
    CONSTRAINT fk_mdfe_x_veiculo FOREIGN KEY (idveiculotracao)
        REFERENCES veiculo (idveiculo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE SEQUENCE inf_mun_carrega_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE inf_mun_carrega
(
    idinfmuncarrega bigint NOT NULL DEFAULT nextval('inf_mun_carrega_seq'::regclass),
    idmdfe bigint NOT NULL,
    idempresa bigint,
    cmuncarrega character varying(7),
    xmuncarrega character varying(60),
    CONSTRAINT inf_mun_carrega_pkey PRIMARY KEY (idinfmuncarrega),
    CONSTRAINT fk_infmun_carrega_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE SEQUENCE inf_percurso_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE inf_percurso
(
    idinfpercurso bigint NOT NULL DEFAULT nextval('inf_percurso_seq'::regclass),
    idmdfe bigint NOT NULL,
    idempresa bigint,
    ufper character varying(2),
    CONSTRAINT inf_percurso_pkey PRIMARY KEY (idinfpercurso),
    CONSTRAINT fk_inf_percurso_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE SEQUENCE inf_mun_descarga_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE inf_mun_descarga
(
    idinfmundescarga bigint NOT NULL DEFAULT nextval('inf_mun_descarga_seq'::regclass),
    idmdfe bigint NOT NULL,
    idempresa bigint,
    cmundescarga character varying(7),
    xmundescarga character varying(60),
    CONSTRAINT inf_mun_descarga_pkey PRIMARY KEY (idinfmundescarga),
    CONSTRAINT fk_inf_mun_descarga_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE inf_doc_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE inf_doc
(
    idinfdoc bigint NOT NULL DEFAULT nextval('inf_doc_seq'::regclass),
    idinfmundescarga bigint NOT NULL,
    idempresa bigint,
    chavedoc character varying(44),
    tipodoc character varying(4), -- nfe ou cte
    segcodbarra character varying(36),
    peso decimal(11,4) not null,
    valor decimal(13,2) not null,
    CONSTRAINT inf_doc_pkey PRIMARY KEY (idinfdoc),
    CONSTRAINT fk_inf_doc_x_inf_mun_descarga FOREIGN KEY (idinfmundescarga)
        REFERENCES inf_mun_descarga (idinfmundescarga) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE inf_unid_transp_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE inf_unid_transp
(
    idinfunidtransp bigint NOT NULL DEFAULT nextval('inf_unid_transp_seq'::regclass),
    idinfdoc bigint,
    idempresa bigint,
    idunidtransp character varying(20),
    tpunidtransp character varying(30),
    qtdrateada numeric(5,2),
    CONSTRAINT inf_unid_transp_pkey PRIMARY KEY (idinfunidtransp),
    CONSTRAINT fk_infunidtransp_x_inf_doc FOREIGN KEY (idinfdoc)
        REFERENCES inf_doc (idinfdoc) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE lacre_unid_transp_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE lacre_unid_transp
(
    idlacreunidtransp bigint NOT NULL DEFAULT nextval('lacre_unid_transp_seq'::regclass),
    idinfunidtransp bigint NOT NULL,
    idempresa bigint,
    nlacre character varying(20),
    CONSTRAINT lac_unid_transp_pkey PRIMARY KEY (idlacreunidtransp),
    CONSTRAINT fk_lacre_unid_transp_x_inf_unid_transp FOREIGN KEY (idinfunidtransp)
        REFERENCES inf_unid_transp (idinfunidtransp) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE inf_unid_carga_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE inf_unid_carga
(
    idinfunidcarga bigint NOT NULL DEFAULT nextval('inf_unid_carga_seq'::regclass),
    idinfunidtransp bigint NOT NULL,
    idempresa bigint,
    tpunidcarga character varying(30),
    idunidcarga character varying(20),
    qtdrateada numeric(5,2),
    CONSTRAINT inf_unid_carga_pkey PRIMARY KEY (idinfunidcarga),
    CONSTRAINT fk_inf_unid_carga_x_inf_unid_transp FOREIGN KEY (idinfunidtransp)
        REFERENCES inf_unid_transp (idinfunidtransp) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE lacre_unid_carga_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE lacre_unid_carga
(
    idlacreunidcarga bigint NOT NULL DEFAULT nextval('lacre_unid_carga_seq'::regclass),
    idinfunidcarga bigint NOT NULL,
    idempresa bigint,
    nlacre character varying(20),
    CONSTRAINT lacre_unid_carga_pkey PRIMARY KEY (idlacreunidcarga),
    CONSTRAINT fk_lacre_unid_carga_x_inf_unid_carga FOREIGN KEY (idinfunidcarga)
        REFERENCES inf_unid_carga (idinfunidcarga) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE SEQUENCE periculosidade_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE periculosidade
(
    idpericulosidade bigint NOT NULL DEFAULT nextval('periculosidade_seq'::regclass),
    idinfdoc bigint NOT NULL,
    idempresa bigint,
    gremb character varying(6),
    nonu character varying(4),
    qtotprod character varying(20),
    qvoltipo character varying(60),
    xclarisco character varying(40),
    xnomeae character varying(150),
    CONSTRAINT periculosidade_pkey PRIMARY KEY (idpericulosidade),
    CONSTRAINT fk_periculosidade_x_inf_doc FOREIGN KEY (idinfdoc)
        REFERENCES inf_doc (idinfdoc) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE seg_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE seg
(
    idseg bigint NOT NULL DEFAULT nextval('seg_seq'::regclass),
    idmdfe bigint NOT NULL,
    idempresa bigint,
    idseguradora bigint NOT NULL,
    CONSTRAINT seg_pkey PRIMARY KEY (idseg),
    CONSTRAINT fk_seg_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_seg_x_seguradora FOREIGN KEY (idseguradora)
        REFERENCES seguradora (idseguradora) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE seg_numero_averbacao_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE seg_numero_averbacao
(
    idsegnumeroaverbacao bigint NOT NULL DEFAULT nextval('seg_numero_averbacao_seq'::regclass),
    idseg bigint NOT NULL,
    idempresa bigint,
    naver character varying(40),
    CONSTRAINT seg_numero_averbacao_pkey PRIMARY KEY (idsegnumeroaverbacao),
    CONSTRAINT fk_seg_numero_averbacao_x_seg FOREIGN KEY (idseg)
        REFERENCES seg (idseg) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE SEQUENCE totalizadores_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE totalizadores
(
    idtotalizadores bigint NOT NULL DEFAULT nextval('totalizadores_seq'::regclass),
    idmdfe bigint NOT null,
    idempresa bigint,
    qcte character varying(7),
    cunid character varying(2),
    qcarga numeric(15,4),
    qmdfe character varying(7),
    qnfe character varying(6),
    vcarga numeric(15,2),
    CONSTRAINT totalizadores_pkey PRIMARY KEY (idtotalizadores),
    CONSTRAINT fk_totalizadores_x_mdfe FOREIGN KEY (idmdfe)
     REFERENCES mdfe (idmdfe) MATCH SIMPLE
     ON UPDATE NO ACTION
     ON DELETE NO ACTION
);

CREATE SEQUENCE lacre_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE lacre
(
    idlacre bigint NOT NULL DEFAULT nextval('lacre_seq'::regclass),
    idmdfe bigint NOT NULL,
    idempresa bigint,
    nlacre character varying(60),
    CONSTRAINT lacre_pkey PRIMARY KEY (idlacre),
    CONSTRAINT fk_lacres_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE SEQUENCE autorizados_xml_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE autorizados_xml
(
    idautorizadosxml bigint NOT NULL DEFAULT nextval('autorizados_xml_seq'::regclass),
    idmdfe bigint NOT NULL,
    idempresa bigint,
    cnpj character varying(14),
    cpf character varying(11),
    CONSTRAINT autorizados_xml_pkey PRIMARY KEY (idautorizadosxml),
    CONSTRAINT fk_autorizados_xml_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE inf_ciot_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE inf_ciot
(
    idinfciot bigint NOT NULL DEFAULT nextval('inf_ciot_seq'::regclass),
    idempresa bigint,
    idmdfe bigint NOT NULL,
    ciot character varying(12),
    cnpj character varying(14),
    cpf character varying(11),
    CONSTRAINT inf_ciot_pkey PRIMARY KEY (idinfciot),
    CONSTRAINT fk_inf_ciot_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE SEQUENCE vale_pedagio_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE vale_pedagio
(
    idvalepedagio bigint NOT NULL DEFAULT nextval('vale_pedagio_seq'::regclass),
    idmdfe bigint NOT NULL,
    idempresa bigint,
    cnpjforn character varying(14),
    cnpjpg character varying(14),
    cpfpg character varying(11),
    ncompra character varying(20),
    vvaleped numeric(15,2),
    CONSTRAINT vale_pedagio_pkey PRIMARY KEY (idvalepedagio),
    CONSTRAINT fk_vale_pedagio_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE inf_contratante_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE inf_contratante
(
    idinfcontratante bigint NOT NULL DEFAULT nextval('inf_contratante_seq'::regclass),
    idmdfe bigint NOT NULL,
    idempresa bigint,
    cnpj character varying(14),
    cpf character varying(11),
    CONSTRAINT inf_contratante_pkey PRIMARY KEY (idinfcontratante),
    CONSTRAINT fk_inf_contratante_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


CREATE SEQUENCE condutor_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE condutor
(
    idcondutor bigint NOT NULL DEFAULT nextval('condutor_seq'::regclass),
    idmdfe bigint NOT NULL,
    idempresa bigint,
    idmotorista bigint NOT NULL,
    CONSTRAINT condutor_pkey PRIMARY KEY (idcondutor),
    CONSTRAINT fk_condutor_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
   CONSTRAINT fk_condutor_x_motorista FOREIGN KEY (idmotorista)
       REFERENCES motorista (idmotorista) MATCH SIMPLE
       ON UPDATE NO ACTION
       ON DELETE NO ACTION
);

CREATE SEQUENCE lacre_rodo_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE lacre_rodoviario
(
    idlacrerodoviario bigint NOT NULL DEFAULT nextval('lacre_rodo_seq'::regclass),
    idmdfe bigint NOT NULL,
    idempresa bigint,
    nlacre character varying(20),
     CONSTRAINT lacre_rodoviario_pkey PRIMARY KEY (idlacrerodoviario),
    CONSTRAINT fk_lacre_rodoviario_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
CREATE SEQUENCE veiculo_reboque_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE veiculo_reboque
(
    idveiculoreboque bigint NOT NULL DEFAULT nextval('veiculo_reboque_seq'::regclass),
    idmdfe bigint NOT NULL,
    idempresa bigint,
    idveiculo bigint NOT NULL,
    CONSTRAINT veiculo_reboque_pkey PRIMARY KEY (idveiculoreboque),
    CONSTRAINT fk_veiculo_reboque_x_mdfe FOREIGN KEY (idmdfe)
        REFERENCES mdfe (idmdfe) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
     CONSTRAINT fk_veiculo_reboque_x_veiculo FOREIGN KEY (idveiculo)
        REFERENCES veiculo (idveiculo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE certificado_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE certificado
(
  idcertificado bigint NOT NULL DEFAULT nextval('certificado_seq'::regclass),
  idempresa bigint NOT NULL,
  arquivo bytea,
  senha character varying(100),
  dtiniciovalidade date not null,
  dtfimvalidade date not null,
  CONSTRAINT certificado_pkey PRIMARY KEY (idcertificado),
  CONSTRAINT fk_empresa FOREIGN KEY (idempresa)
      REFERENCES empresa (idempresa) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE mdfe_idlote_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;


CREATE SEQUENCE ciot_idciot_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

create table ciot(
  idciot bigint NOT NULL DEFAULT nextval('ciot_idciot_seq'::regclass),
  ciot character varying(11),
  idempresa bigint,
  idmotorista bigint,
  flagdel boolean,
  CONSTRAINT ciot_pk PRIMARY KEY (idciot),
  CONSTRAINT fk_empresa FOREIGN KEY (idempresa)
      REFERENCES empresa (idempresa) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_motorista FOREIGN KEY (idmotorista)
      REFERENCES motorista (idmotorista) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE seguradora_empresa (
  idseguradora bigint NOT NULL,
  idempresa bigint NOT NULL,
  CONSTRAINT fk_seguradora_empresa_seg FOREIGN KEY (idseguradora)
    REFERENCES seguradora (idseguradora) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_seguradora_empresa_emp FOREIGN KEY (idempresa)
    REFERENCES empresa (idempresa) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE pessoa_motorista (
  idpessoa bigint NOT NULL,
  idmotorista bigint NOT NULL,
  CONSTRAINT fk_pessoa_motorista_pessoa FOREIGN KEY (idpessoa)
    REFERENCES pessoa (idpessoa) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_pessoa_motorista_motorista FOREIGN KEY (idmotorista)
    REFERENCES motorista (idmotorista) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE sequence_nummdf (
  idsequence character varying(100) NOT NULL,
  idempresa bigint NOT NULL,
  CONSTRAINT fk_idempresa_sequence_nummdf FOREIGN KEY (idempresa)
    REFERENCES empresa (idempresa) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE configuracao_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;


CREATE TABLE configuracao
(
  idconfiguracao bigint NOT NULL DEFAULT nextval('configuracao_seq'::regclass),
  idempresa bigint NOT NULL,
  chave character varying(150),
  valor character varying(300),
  CONSTRAINT configuracao_pkey PRIMARY KEY (idconfiguracao),
  CONSTRAINT fk_empresa FOREIGN KEY (idempresa)
      REFERENCES empresa (idempresa) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

