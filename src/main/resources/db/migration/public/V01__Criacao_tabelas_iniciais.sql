CREATE SEQUENCE public.pessoa_idpessoa_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE public.pessoa
(
  idpessoa bigint NOT NULL DEFAULT nextval('pessoa_idpessoa_seq'::regclass),
  idpessoatenant bigint NOT NULL,
  tenantid character varying(25) NOT NULL,
  email character varying(200) NOT NULL,
  cpf character varying(11),
  tipousuario character varying(40) NOT NULL,
  nome character varying(60) NOT NULL,
  senhausuario character varying(100),
  idempresapadrao int NOT NULL,
  flagdel boolean,
  flagativo boolean,
  CONSTRAINT pessoa_pkey PRIMARY KEY (idpessoa),
  CONSTRAINT pessoa_ukey UNIQUE (email)
);

CREATE SEQUENCE public.recuperacao_senha_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE public.recuperacao_senha
(
  idrecuperacaosenha bigint NOT NULL DEFAULT nextval('recuperacao_senha_seq'::regclass),
  email character varying(200) NOT NULL,
  dthorasolicitacao timestamp without time zone NOT NULL DEFAULT now(),
  dthoravalidade timestamp without time zone,
  tenantid character varying(25) NOT NULL,
  hash character varying(100),
  CONSTRAINT recuperacao_senha_pk PRIMARY KEY (idrecuperacaosenha)
);

CREATE SEQUENCE public.estado_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE public.estado
(
  idestado bigint NOT NULL DEFAULT nextval('estado_seq'::regclass),
  id bigint NOT NULL,
  nome character varying(300) NOT NULL,
  sigla character varying(2) NOT NULL,
  CONSTRAINT estado_pk PRIMARY KEY (idestado)
);

CREATE SEQUENCE public.municipio_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE public.municipio
(
  idmunicipio bigint NOT NULL DEFAULT nextval('municipio_seq'::regclass),
  idestado bigint NOT NULL,
  id bigint NOT NULL,
  nome character varying(300) NOT NULL,
  CONSTRAINT municipio_pk PRIMARY KEY (idmunicipio),
    CONSTRAINT fk_estado FOREIGN KEY (idestado)
    REFERENCES public.estado (idestado) MATCH SIMPLE
    ON UPDATE NO ACTION ON DELETE NO ACTION
);
