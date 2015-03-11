CREATE DATABASE `physis_ephynances`;

ALTER DATABASE `physis_ephynances` CHARACTER SET utf8 COLLATE utf8_unicode_ci;

create table proponente_siconv(
	id_proponente_siconv integer auto_increment not null primary key,
	cnpj varchar(18) not null,
	nome varchar(255) not null,
	esfera_administrativa varchar(255) not null,
	codigo_municipio varchar(40) not null,
	municipio varchar(255) not null,
	municipio_uf_sigla varchar(2) not null,
	municipio_uf_nome varchar(60) not null,
	municipio_uf_regiao varchar(2) not null,
	endereco varchar(255) not null,
	cep varchar(8) not null,
	nome_responsavel varchar(255) not null,
	telefone varchar(30) not null,
	fax varchar(30) not null,
	natureza_juridica varchar(255) not null,
	inscricao_estadual varchar(255) not null,
	inscricao_municipal varchar(255) not null
);

ALTER TABLE `proponente_siconv` 
ADD COLUMN `situacao` VARCHAR(255) NULL AFTER `inscricao_municipal`;

ALTER TABLE `proponente_siconv` 
ADD INDEX `idx_municipio` (`codigo_municipio` ASC),
ADD INDEX `idx_municipio_uf_sigla` (`municipio_uf_sigla` ASC),
ADD INDEX `idx_esfera_administrativa` (`esfera_administrativa` ASC),
ADD INDEX `idx_cnpj` (`cnpj` ASC);
