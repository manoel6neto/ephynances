ALTER TABLE `proponente_siconv`
ADD `agreement_id` bigint(20) DEFAULT NULL,
ADD KEY `FK_proponente_siconv_agreement_id` (`agreement_id`),
ADD CONSTRAINT `FK_proponente_siconv_agreement_id` FOREIGN KEY (`agreement_id`) REFERENCES `agreement` (`id`);

