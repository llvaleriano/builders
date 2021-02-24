--liquibase formatted sql
--changeset Leonardo Valeriano:2
alter table cliente add column sexo varchar(4);
