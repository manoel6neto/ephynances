ALTER TABLE `proponente_siconv`
ADD COLUMN `order` INTEGER(11) DEFAULT NULL;

ALTER TABLE `proponente_siconv`
ADD INDEX `idx_order` (`order` ASC);
