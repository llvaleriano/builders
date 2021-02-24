--liquibase formatted sql
--changeset Leonardo Valeriano:1
create table cliente (
    id              serial not null,
    nome            varchar (100) not null,
    email           varchar (100),
    nascimento      date not null,
    logradouro      varchar (50),
    numero_endereco varchar (20),
    complemento     varchar (20),
    bairro          varchar (50),
    cidade          varchar (50),
    uf              varchar (2),
    cep             varchar (8),
    ddd             varchar (3),
    numero_telefone varchar (10),
    primary key (id)
)