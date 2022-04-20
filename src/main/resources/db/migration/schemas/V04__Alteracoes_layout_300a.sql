alter table mdfe drop column indcanalverde;
alter table mdfe drop column indcarregaposterior;

alter table mdfe add indcanalverde boolean NOT NULL DEFAULT false;
alter table mdfe add indcarregaposterior boolean NOT NULL DEFAULT false;
