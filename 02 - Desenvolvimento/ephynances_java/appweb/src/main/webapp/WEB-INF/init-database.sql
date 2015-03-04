SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

USE `physis_ephynances`;

-- User --
insert into `user`(email, name, password, cpf, phone, cell_phone, max_sales_amount, profile_rule, delete_date) 
values ('admin@physisbrasil.com.br', 'Administrador', 'c0859afbb1427c8ead10835ae456db9', '84093587515', '(73) 9119-2425', '(73) 9119-2425', 0, 'Administrador', null);

-------- Configuration ---------
insert into configuration(smtp_server, user_name, password, smtp_port, email)
values ('smtp.gmail.com', 'manoel.carvalho.neto', 'mxthomasmx', 587, 'manoel.carvalho.neto@gmail.com');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
