CREATE SEQUENCE mensalidade_idmensalidade_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

create table mensalidade(
	idmensalidade bigint NOT NULL DEFAULT nextval('mensalidade_idmensalidade_seq'::regclass),
	valor numeric(15,2) NOT NULL,
	dtcadastro date NOT NULL,
	CONSTRAINT mensalidade_pkey PRIMARY KEY (idmensalidade)
);

CREATE SEQUENCE mdfe_recebimento_idmdferecebimento_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

create table mdfe_recebimento(
	idmdferecebimento bigint NOT NULL DEFAULT nextval('mdfe_recebimento_idmdferecebimento_seq'::regclass),
	idmensalidade bigint not null,
	idpessoa bigint not null,
	qtddiasdelicenca character varying(40) not null,
	dtrecebimento timestamp without time zone not null,
	valor numeric(15,2) NOT NULL,
	flaggerounota boolean,
	chavenfse character varying(44),
	CONSTRAINT mdfe_recebimento_pkey PRIMARY KEY (idmdferecebimento),
  CONSTRAINT mdfe_recebimento_fk_idmensalidade FOREIGN KEY (idmensalidade)
  REFERENCES mensalidade (idmensalidade) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT mdfe_recebimento_fk_idpessoa FOREIGN KEY (idpessoa)
  REFERENCES pessoa (idpessoa) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE SEQUENCE mdfe_licenca_idmdfelicenca_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

create table mdfe_licenca(
	idmdfelicenca bigint NOT NULL DEFAULT nextval('mdfe_licenca_idmdfelicenca_seq'::regclass),
	idmdferecebimento bigint,
	dtinicio timestamp without time zone not null,
	dtfim timestamp without time zone not null,
	CONSTRAINT mdfe_licenca_pkey PRIMARY KEY (idmdfelicenca)
);

INSERT INTO mensalidade (valor, dtcadastro) values (175.00, current_date);

INSERT INTO mdfe_licenca (dtinicio, dtfim) values (CURRENT_DATE ,CURRENT_DATE +15);