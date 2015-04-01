SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

USE `physis_ephynances`;

-- Users --
insert into `user`(email, name, password, cpf, phone, cell_phone, profile_rule, is_verified) 
values ('admin@physisbrasil.com.br', 'Administrador', 'c0859afbb1427c8ead10835ae456db9', '111.111.111-11', '(00) 0000-0000', '(00) 0000-0000', 'Administrador Geral', 1);

-------- Configurations ---------
insert into configuration(smtp_server, user_name, password, smtp_port, email, contract_seed)
values ('smtp.gmail.com', 'manoel.carvalho.neto', 'mxthomasmx', 587, 'manoel.carvalho.neto@gmail.com', 1);

--- Regions ---
insert into region(name, acronym)
values ('Norte', 'N'),
('Nordeste', 'NE'), 
('Sudeste', 'SE'), 
('Centro-Oeste', 'CO'),
('Sul', 'S');

--- States ---
insert into `state`(region_id, name, acronym)
values (1, 'Acre', 'AC'), (1, 'Amapá', 'AP'), (1, 'Amazonas', 'AM'), (1, 'Pará', 'PA'), (1, 'Rondônia', 'RO'), (1, 'Roraima', 'RR'), (1, 'Tocantins', 'TO'),
(2, 'Alagoas', 'AL'), (2, 'Bahia', 'BA'), (2, 'Ceará', 'CE'), (2, 'Maranhão', 'MA'), (2, 'Paraíba', 'PB'), (2, 'Piauí', 'PI'), (2, 'Pernambuco', 'PE'), (2, 'Rio Grande do Norte', 'RN'), (2, 'Sergipe', 'SE'),
(3, 'São Paulo', 'SP'), (3, 'Minas Gerais', 'MG'), (3, 'Rio de Janeiro', 'RJ'),  (3, 'Espírito Santo', 'ES'),
(4, 'Goiás', 'GO'), (4, 'Distrito Federal', 'DF'), (4, 'Mato Grosso', 'MT'), (4, 'Mato Grosso do Sul', 'MS'),
(5, 'Paraná', 'PR'), (5, 'Santa Catarina', 'SC'), (5, 'Rio Grande do Sul', 'RS');

insert into `administrative_sphere`(name)
values ('CONSORCIO PUBLICO'),
('EMPRESA PUBLICA SOCIEDADE ECONOMIA MISTA'),
('ESTADUAL'),
('FEDERAL'),
('MUNICIPAL'),
('ORGANISMO INTERNACIONAL'),
('PRIVADA');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
