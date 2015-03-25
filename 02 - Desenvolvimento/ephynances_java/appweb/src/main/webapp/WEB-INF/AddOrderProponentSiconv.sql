ALTER TABLE `proponente_siconv`
ADD COLUMN `order_visit` INTEGER(11) DEFAULT NULL;

ALTER TABLE `proponente_siconv`
ADD INDEX `idx_order_visit` (`order_visit` ASC);
