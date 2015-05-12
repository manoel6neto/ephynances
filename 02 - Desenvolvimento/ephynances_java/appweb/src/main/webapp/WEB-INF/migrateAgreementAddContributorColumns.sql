ALTER TABLE `agreement`
ADD COLUMN `contributor_email` varchar(200) DEFAULT NULL,
ADD COLUMN `contributor_name` varchar(200) DEFAULT NULL,
ADD COLUMN `contributor_position` varchar(200) DEFAULT NULL;

