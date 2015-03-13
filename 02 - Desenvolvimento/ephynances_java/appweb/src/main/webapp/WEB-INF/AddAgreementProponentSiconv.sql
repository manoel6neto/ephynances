ALTER TABLE `proponente_siconv`
ADD `user_id` bigint(20) DEFAULT NULL,
ADD KEY `FK_proponente_siconv_user_id` (`user_id`),
ADD CONSTRAINT `FK_proponente_siconv_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

