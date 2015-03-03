SET FOREIGN_KEY_CHECKS=0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

USE `agility_db`;


-- Padrão --
insert into `user`(email, name, password, profile_rule, delete_date) 
values ('admin@ipsoftbrasil.com.br', 'admin', '43878f448874a92faccfc79cef39b669', 'ADMIN', null),
('user@ipsoftbrasil.com.br', 'user', '43878f448874a92faccfc79cef39b669', 'USER', null);

-- Padrão --
insert into `agility_inventory_machine_group`(name) 
values ('Sem grupo');

/* -------- Configuration --------- */
insert into configuration(smtp_server, user_name, password, smtp_port, email)
values ('smtp.gmail.com', 'ipsoftilheus', 'arroz1010', 587, 'ipsoftilheus@gmail.com');

--
-- Database: `agility_db`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_ALERT_INS`(
        IN machine_id BIGINT, IN action_status INTEGER(11), IN component VARCHAR(250)
    )
BEGIN
    INSERT INTO agility_inventory_alert (machine_id, action_status, component, insert_date)
    VALUES (machine_id, action_status, component, NOW());
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_audio
	WHERE agility_inventory_audio.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_audio
	WHERE agility_inventory_audio.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_HISTORY_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_audio_history
	WHERE agility_inventory_audio_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_HISTORY_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_audio_history
	WHERE agility_inventory_audio_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_HISTORY_INS`(
        IN machine_id BIGINT, IN action_status INTEGER(11), IN model VARCHAR(250),
        IN manufacturer VARCHAR(100))
BEGIN
    INSERT INTO agility_inventory_audio_history (machine_id, action_status, model,
     manufacturer, insert_date)
    VALUES (machine_id, action_status, model, manufacturer, NOW());
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_HISTORY_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_audio_history.ID,
        agility_inventory_audio_history.machine_id,
        agility_inventory_audio_history.action_status,
        agility_inventory_audio_history.manufacturer,
        agility_inventory_audio_history.model,
        agility_inventory_audio_history.insert_date
    FROM agility_inventory_audio_history
    WHERE agility_inventory_audio_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_HISTORY_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_audio_history.machine_id,
        agility_inventory_audio_history.action_status,
        agility_inventory_audio_history.manufacturer,
        agility_inventory_audio_history.model,
        agility_inventory_audio_history.insert_date
    FROM agility_inventory_audio_history
    WHERE agility_inventory_audio_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_HISTORY_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100))
BEGIN
    UPDATE agility_inventory_audio_history
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer,
        insert_date = NOW()
    WHERE agility_inventory_audio_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_INS`(
        IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100))
BEGIN
    INSERT INTO agility_inventory_audio (machine_id, model, manufacturer)
    VALUES (machine_id, model, manufacturer);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_audio.ID,
        agility_inventory_audio.machine_id,
        agility_inventory_audio.manufacturer,
        agility_inventory_audio.model
    FROM agility_inventory_audio
    WHERE agility_inventory_audio.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_audio.machine_id,
        agility_inventory_audio.manufacturer,
        agility_inventory_audio.model
    FROM agility_inventory_audio
    WHERE agility_inventory_audio.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_AUDIO_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100))
BEGIN
    UPDATE agility_inventory_audio
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer
    WHERE agility_inventory_audio.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_machine
	WHERE agility_inventory_machine.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_disk
	WHERE agility_inventory_disk.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_disk
	WHERE agility_inventory_disk.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_HISTORY_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_disk_history
	WHERE agility_inventory_disk_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_HISTORY_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_disk_history
	WHERE agility_inventory_disk_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_HISTORY_INS`(
        IN machine_id BIGINT, IN action_status INTEGER(11), IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN serial VARCHAR(150), IN size_disk FLOAT)
BEGIN
    INSERT INTO agility_inventory_disk_history (machine_id, action_status, model, manufacturer
    , serial, size_disk, insert_date)
    VALUES (machine_id, action_status, model, manufacturer, serial, size_disk, NOW());
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_HISTORY_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_disk_history.ID,
        agility_inventory_disk_history.machine_id,
        agility_inventory_disk_history.action_status,
        agility_inventory_disk_history.model,
        agility_inventory_disk_history.manufacturer,
        agility_inventory_disk_history.serial,
        agility_inventory_disk.size_disk,
        agility_inventory_disk_history.insert_date
    FROM agility_inventory_disk_history
    WHERE agility_inventory_disk_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_HISTORY_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_disk_history.machine_id,
        agility_inventory_disk_history.action_status,
        agility_inventory_disk_history.model,
        agility_inventory_disk_history.manufacturer,
        agility_inventory_disk_history.serial,
        agility_inventory_disk.size_disk,
        agility_inventory_disk_history.insert_date
    FROM agility_inventory_disk_history
    WHERE agility_inventory_disk_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_HISTORY_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN serial VARCHAR(150), IN size_disk FLOAT)
BEGIN
    UPDATE agility_inventory_disk_history
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer,
        serial = serial,
        size_disk = size_disk,
        insert_date = NOW()
    WHERE agility_inventory_disk_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_INS`(
        IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN serial VARCHAR(150), IN size_disk FLOAT)
BEGIN
    INSERT INTO agility_inventory_disk (machine_id, model, manufacturer, serial
    , size_disk)
    VALUES (machine_id, model, manufacturer, serial, size_disk);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_disk.ID,
        agility_inventory_disk.machine_id,
        agility_inventory_disk.model,
        agility_inventory_disk.manufacturer,
        agility_inventory_disk.serial,
        agility_inventory_disk.size_disk
    FROM agility_inventory_disk
    WHERE agility_inventory_disk.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_disk.machine_id,
        agility_inventory_disk.model,
        agility_inventory_disk.manufacturer,
        agility_inventory_disk.serial,
        agility_inventory_disk.size_disk
    FROM agility_inventory_disk
    WHERE agility_inventory_disk.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_DISK_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN serial VARCHAR(150), IN size_disk FLOAT)
BEGIN
    UPDATE agility_inventory_disk
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer,
        serial = serial,
        size_disk = size_disk
    WHERE agility_inventory_disk.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_gpu
	WHERE agility_inventory_gpu.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_gpu
	WHERE agility_inventory_gpu.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_HISTORY_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_gpu_history
	WHERE agility_inventory_gpu_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_HISTORY_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_gpu_history
	WHERE agility_inventory_gpu_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_HISTORY_INS`(
        IN machine_id BIGINT, IN action_status INTEGER(11), IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN version VARCHAR(150))
BEGIN
    INSERT INTO agility_inventory_gpu_history (machine_id, action_status, model, manufacturer,
     version, insert_date)
    VALUES (machine_id, action_status, model, manufacturer, version, NOW());
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_HISTORY_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_gpu_history.ID,
        agility_inventory_gpu_history.machine_id,
        agility_inventory_gpu_history.action_status,
        agility_inventory_gpu_history.model,
        agility_inventory_gpu_history.manufacturer,
        agility_inventory_gpu_history.version,
        agility_inventory_gpu_history.insert_date
    FROM agility_inventory_gpu_history
    WHERE agility_inventory_gpu_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_HISTORY_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_gpu_history.machine_id,
        agility_inventory_gpu_history.action_status,
        agility_inventory_gpu_history.model,
        agility_inventory_gpu_history.manufacturer,
        agility_inventory_gpu_history.version,
        agility_inventory_gpu_history.insert_date
    FROM agility_inventory_gpu_history
    WHERE agility_inventory_gpu_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_HISTORY_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN version VARCHAR(150))
BEGIN
    UPDATE agility_inventory_gpu_history
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer,
        version = version,
        insert_date = NOW()
    WHERE agility_inventory_gpu_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_INS`(
        IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN version VARCHAR(150))
BEGIN
    INSERT INTO agility_inventory_gpu (machine_id, model, manufacturer, version)
    VALUES (machine_id, model, manufacturer, version);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_gpu.ID,
        agility_inventory_gpu.machine_id,
        agility_inventory_gpu.model,
        agility_inventory_gpu.manufacturer,
        agility_inventory_gpu.version
    FROM agility_inventory_gpu
    WHERE agility_inventory_gpu.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_gpu.machine_id,
        agility_inventory_gpu.model,
        agility_inventory_gpu.manufacturer,
        agility_inventory_gpu.version
    FROM agility_inventory_gpu
    WHERE agility_inventory_gpu.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GPU_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN version VARCHAR(150))
BEGIN
    UPDATE agility_inventory_gpu
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer,
        version = version
    WHERE agility_inventory_gpu.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_GROUP_INS_MACHINE`(
        IN machine_id BIGINT, IN group_id BIGINT
    )
BEGIN
    INSERT INTO machine_group (machine_id, group_id)
    VALUES (machine_id, group_id);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_INS`(
        IN hostname VARCHAR(100), IN os VARCHAR(100),
        IN os_version VARCHAR(250), IN status TINYINT,
        IN group_id INTEGER(11))
BEGIN
    INSERT INTO agility_inventory_machine (hostname, os, os_version, status, group_id)
    VALUES (hostname, os, os_version, status, group_id);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_LOG_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_log_testes
	WHERE agility_inventory_log_testes.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_LOG_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_log_testes
	WHERE agility_inventory_log_testes.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_LOG_INS`(
        IN machine_id BIGINT, IN log LONGTEXT)
BEGIN
    INSERT INTO agility_inventory_log_testes (machine_id, log, insert_date)
    VALUES (machine_id, log, NOW());
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_LOG_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_log_testes.ID,
        agility_inventory_log_testes.machine_id,
        agility_inventory_log_testes.log,
        agility_inventory_log_testes.insert_date
    FROM agility_inventory_log_testes
    WHERE agility_inventory_log_testes.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_LOG_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_log_testes.machine_id,                
        agility_inventory_log_testes.log,
        agility_inventory_log_testes.insert_date
    FROM agility_inventory_log_testes
    WHERE agility_inventory_log_testes.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_LOG_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN log LONGTEXT)
BEGIN
    UPDATE agility_inventory_log_testes
    SET
        machine_id = machine_id,
        log = log,
        insert_date = NOW()
    WHERE agility_inventory_log_testes.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MAC_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_machine_mac
	WHERE agility_inventory_machine_mac.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MAC_DEL_MAC`(
        IN mac VARCHAR(100))
BEGIN
	DELETE
	FROM agility_inventory_machine_mac
	WHERE agility_inventory_machine_mac.mac = mac;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MAC_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_machine_mac
	WHERE agility_inventory_machine_mac.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MAC_INS`(
        IN machine_id BIGINT, IN mac VARCHAR(100))
BEGIN
    INSERT INTO agility_inventory_machine_mac (machine_id, mac)
    VALUES (machine_id, mac);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MAC_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_machine_mac.ID,
        agility_inventory_machine_mac.machine_id,
        agility_inventory_machine_mac.mac
    FROM agility_inventory_machine_mac
    WHERE agility_inventory_machine_mac.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_memory
	WHERE agility_inventory_memory.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_memory
	WHERE agility_inventory_memory.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_HISTORY_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_memory_history
	WHERE agility_inventory_memory_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_HISTORY_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_memory_history
	WHERE agility_inventory_memory_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_HISTORY_INS`(
        IN machine_id BIGINT, IN action_status INTEGER(11), IN tecnology VARCHAR(100),
        IN manufacturer VARCHAR(100), IN clock FLOAT, IN size_memory FLOAT)
BEGIN
    INSERT INTO agility_inventory_memory_history (machine_id, action_status, tecnology,
     manufacturer, clock, size_memory, insert_date)
    VALUES (machine_id, action_status, tecnology, manufacturer, clock, size_memory, NOW());
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_HISTORY_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_memory_history.ID,
        agility_inventory_memory_history.machine_id,
        agility_inventory_memory_history.action_status,
        agility_inventory_memory_history.tecnology,
        agility_inventory_memory_history.manufacturer,
        agility_inventory_memory_history.clock,
        agility_inventory_memory_history.size_memory,
        agility_inventory_memory_history.insert_date
    FROM agility_inventory_memory_history
    WHERE agility_inventory_memory_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_HISTORY_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_memory_history.machine_id,
        agility_inventory_memory_history.action_status,
        agility_inventory_memory_history.tecnology,
        agility_inventory_memory_history.manufacturer,
        agility_inventory_memory_history.clock,
        agility_inventory_memory_history.size_memory,
        agility_inventory_memory_history.insert_date
    FROM agility_inventory_memory_history
    WHERE agility_inventory_memory_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_HISTORY_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN tecnology VARCHAR(100),
        IN manufacturer VARCHAR(100), IN clock FLOAT, IN size_memory FLOAT)
BEGIN
    UPDATE agility_inventory_memory_history
    SET
        machine_id = machine_id,
        tecnology = tecnology,
        manufacturer = manufacturer,
        clock = clock,
        size_memory = size_memory,
        insert_date = NOW()
    WHERE agility_inventory_memory_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_INS`(
        IN machine_id BIGINT, IN tecnology VARCHAR(100),
        IN manufacturer VARCHAR(100), IN clock FLOAT, IN size_memory FLOAT)
BEGIN
    INSERT INTO agility_inventory_memory (machine_id, tecnology, manufacturer,
     clock, size_memory)
    VALUES (machine_id, tecnology, manufacturer, clock, size_memory);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_memory.ID,
        agility_inventory_memory.machine_id,
        agility_inventory_memory.tecnology,
        agility_inventory_memory.manufacturer,
        agility_inventory_memory.clock,
        agility_inventory_memory.size_memory
    FROM agility_inventory_memory
    WHERE agility_inventory_memory.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_memory.machine_id,
        agility_inventory_memory.tecnology,
        agility_inventory_memory.manufacturer,
        agility_inventory_memory.clock,
        agility_inventory_memory.size_memory
    FROM agility_inventory_memory
    WHERE agility_inventory_memory.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MEMORY_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN tecnology VARCHAR(100),
        IN manufacturer VARCHAR(100), IN clock FLOAT, IN size_memory FLOAT)
BEGIN
    UPDATE agility_inventory_memory
    SET
        machine_id = machine_id,
        tecnology = tecnology,
        manufacturer = manufacturer,
        clock = clock,
        size_memory = size_memory
    WHERE agility_inventory_memory.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_motherboard
	WHERE agility_inventory_motherboard.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_motherboard
	WHERE agility_inventory_motherboard.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_HISTORY_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_motherboard_history
	WHERE agility_inventory_motherboard_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_HISTORY_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_motherboard_history
	WHERE agility_inventory_motherboard_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_HISTORY_INS`(
        IN machine_id BIGINT, IN action_status INTEGER(11), IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN version VARCHAR(150))
BEGIN
    INSERT INTO agility_inventory_motherboard_history (machine_id, action_status, model,
     manufacturer, version, insert_date)
    VALUES (machine_id, action_status, model, manufacturer, version, NOW());
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_HISTORY_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_motherboard_history.ID,
        agility_inventory_motherboard_history.machine_id,
        agility_inventory_motherboard_history.action_status,
        agility_inventory_motherboard_history.model,
        agility_inventory_motherboard_history.manufacturer,
        agility_inventory_motherboard_history.version,
        agility_inventory_motherboard_history.insert_date
    FROM agility_inventory_motherboard_history
    WHERE agility_inventory_motherboard_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_HISTORY_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_motherboard_history.machine_id,
        agility_inventory_motherboard_history.action_status,
        agility_inventory_motherboard_history.model,
        agility_inventory_motherboard_history.manufacturer,
        agility_inventory_motherboard_history.version,
        agility_inventory_motherboard_history.insert_date
    FROM agility_inventory_motherboard_history
    WHERE agility_inventory_motherboard_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_HISTORY_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN version VARCHAR(150))
BEGIN
    UPDATE agility_inventory_motherboard_history
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer,
        version = version,
        insert_date = NOW()
    WHERE agility_inventory_motherboard_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_INS`(
        IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN version VARCHAR(150))
BEGIN
    INSERT INTO agility_inventory_motherboard (machine_id, model, manufacturer,
     version)
    VALUES (machine_id, model, manufacturer, version);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_motherboard.ID,
        agility_inventory_motherboard.machine_id,
        agility_inventory_motherboard.model,
        agility_inventory_motherboard.manufacturer,
        agility_inventory_motherboard.version
    FROM agility_inventory_motherboard
    WHERE agility_inventory_motherboard.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_motherboard.machine_id,
        agility_inventory_motherboard.model,
        agility_inventory_motherboard.manufacturer,
        agility_inventory_motherboard.version
    FROM agility_inventory_motherboard
    WHERE agility_inventory_motherboard.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_MOTHERBOARD_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN version VARCHAR(150))
BEGIN
    UPDATE agility_inventory_motherboard
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer,
        version = version
    WHERE agility_inventory_motherboard.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_network
	WHERE agility_inventory_network.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_network
	WHERE agility_inventory_network.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_HISTORY_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_network_history
	WHERE agility_inventory_network_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_HISTORY_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_network_history
	WHERE agility_inventory_network_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_HISTORY_INS`(
        IN machine_id BIGINT, IN action_status INTEGER(11), IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN mac VARCHAR(50),
        IN speed INTEGER(11), IN type VARCHAR(50), IN ipv4 VARCHAR(50),
        IN ipv6 VARCHAR(100), IN status BOOLEAN)
BEGIN
    INSERT INTO agility_inventory_network_history (machine_id, action_status, model,
     manufacturer, mac, speed, type, ipv4, ipv6, status, insert_date)
    VALUES (machine_id, action_status, model, manufacturer, mac, speed, type, ipv4, ipv6,
     status, NOW());
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_HISTORY_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_network_history.ID,
        agility_inventory_network_history.machine_id,
        agility_inventory_network_history.action_status,
        agility_inventory_network_history.model,
        agility_inventory_network_history.manufacturer,
        agility_inventory_network_history.mac,
        agility_inventory_network_history.speed,
        agility_inventory_network_history.type,
        agility_inventory_network_history.ipv4,
        agility_inventory_network_history.ipv6,
        agility_inventory_network_history.status,
        agility_inventory_network_history.insert_date
    FROM agility_inventory_network_history
    WHERE agility_inventory_network_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_HISTORY_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_network_history.machine_id,
        agility_inventory_network_history.action_status,
        agility_inventory_network_history.model,
        agility_inventory_network_history.manufacturer,
        agility_inventory_network_history.mac,
        agility_inventory_network_history.speed,
        agility_inventory_network_history.type,
        agility_inventory_network_history.ipv4,
        agility_inventory_network_history.ipv6,
        agility_inventory_network_history.status,
        agility_inventory_network_history.insert_date
    FROM agility_inventory_network_history
    WHERE agility_inventory_network_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_HISTORY_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN mac VARCHAR(50),
        IN speed INTEGER(11), IN type VARCHAR(50), IN ipv4 VARCHAR(50),
        IN ipv6 VARCHAR(100), IN status BOOLEAN)
BEGIN
    UPDATE agility_inventory_network_history
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer,
        mac = mac,
        speed = speed,
        type = type,
        ipv4 = ipv4,
        ipv6 = ipv6,
        status = status,
        insert_date = NOW()
    WHERE agility_inventory_network_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_INS`(
        IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN mac VARCHAR(50),
        IN speed INTEGER(11), IN type VARCHAR(50), IN ipv4 VARCHAR(50),
        IN ipv6 VARCHAR(100), IN status BOOLEAN)
BEGIN
    INSERT INTO agility_inventory_network (machine_id, model, manufacturer, mac
    , speed, type, ipv4, ipv6, status)
    VALUES (machine_id, model, manufacturer, mac, speed, type, ipv4, ipv6,
     status);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_network.ID,
        agility_inventory_network.machine_id,
        agility_inventory_network.model,
        agility_inventory_network.manufacturer,
        agility_inventory_network.mac,
        agility_inventory_network.speed,
        agility_inventory_network.type,
        agility_inventory_network.ipv4,
        agility_inventory_network.ipv6,
        agility_inventory_network.status
    FROM agility_inventory_network
    WHERE agility_inventory_network.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_network.machine_id,
        agility_inventory_network.model,
        agility_inventory_network.manufacturer,
        agility_inventory_network.mac,
        agility_inventory_network.speed,
        agility_inventory_network.type,
        agility_inventory_network.ipv4,
        agility_inventory_network.ipv6,
        agility_inventory_network.status
    FROM agility_inventory_network
    WHERE agility_inventory_network.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_NETWORK_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN mac VARCHAR(50),
        IN speed INTEGER(11), IN type VARCHAR(50), IN ipv4 VARCHAR(50),
        IN ipv6 VARCHAR(100), IN status BOOLEAN)
BEGIN
    UPDATE agility_inventory_network
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer,
        mac = mac,
        speed = speed,
        type = type,
        ipv4 = ipv4,
        ipv6 = ipv6,
        status = status
    WHERE agility_inventory_network.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_processor
	WHERE agility_inventory_processor.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_processor
	WHERE agility_inventory_processor.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_HISTORY_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_processor_history
	WHERE agility_inventory_processor_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_HISTORY_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_processor_history
	WHERE agility_inventory_processor_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_HISTORY_INS`(
        IN machine_id BIGINT, IN action_status INTEGER(11), IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN clock FLOAT,
        IN cores INTEGER(11), IN architecture VARCHAR(100))
BEGIN
    INSERT INTO agility_inventory_processor_history (machine_id, action_status, model,
     manufacturer, clock, cores, architecture, insert_date)
    VALUES (machine_id, action_status, model, manufacturer, clock, cores, architecture, NOW());
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_HISTORY_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_processor_history.ID,
        agility_inventory_processor_history.machine_id,
        agility_inventory_processor_history.action_status,
        agility_inventory_processor_history.model,
        agility_inventory_processor_history.manufacturer,
        agility_inventory_processor_history.clock,
        agility_inventory_processor_history.cores,
        agility_inventory_processor_history.architecture,
        agility_inventory_processor_history.insert_date
    FROM agility_inventory_processor_history
    WHERE agility_inventory_processor_history.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_HISTORY_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_processor_history.machine_id,
        agility_inventory_processor_history.action_status,
        agility_inventory_processor_history.model,
        agility_inventory_processor_history.manufacturer,
        agility_inventory_processor_history.clock,
        agility_inventory_processor_history.cores,
        agility_inventory_processor_history.architecture,
        agility_inventory_processor_history.insert_date
    FROM agility_inventory_processor_history
    WHERE agility_inventory_processor_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_HISTORY_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN clock FLOAT,
        IN cores INTEGER(11), IN architecture VARCHAR(100))
BEGIN
    UPDATE agility_inventory_processor_history
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer,
        clock = clock,
        cores = cores,
        architecture = architecture,
        insert_date = NOW()
    WHERE agility_inventory_processor_history.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_INS`(
        IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN clock FLOAT,
        IN cores INTEGER(11), IN architecture VARCHAR(100))
BEGIN
    INSERT INTO agility_inventory_processor (machine_id, model, manufacturer,
     clock, cores, architecture)
    VALUES (machine_id, model, manufacturer, clock, cores, architecture);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_processor.ID,
        agility_inventory_processor.machine_id,
        agility_inventory_processor.model,
        agility_inventory_processor.manufacturer,
        agility_inventory_processor.clock,
        agility_inventory_processor.cores,
        agility_inventory_processor.architecture
    FROM agility_inventory_processor
    WHERE agility_inventory_processor.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_processor.machine_id,
        agility_inventory_processor.model,
        agility_inventory_processor.manufacturer,
        agility_inventory_processor.clock,
        agility_inventory_processor.cores,
        agility_inventory_processor.architecture
    FROM agility_inventory_processor
    WHERE agility_inventory_processor.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_PROCESSOR_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN model VARCHAR(250),
        IN manufacturer VARCHAR(100), IN clock FLOAT,
        IN cores INTEGER(11), IN architecture VARCHAR(100))
BEGIN
    UPDATE agility_inventory_processor
    SET
        machine_id = machine_id,
        model = model,
        manufacturer = manufacturer,
        clock = clock,
        cores = cores,
        architecture = architecture
    WHERE agility_inventory_processor.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_SEL`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_machine.ID,
        agility_inventory_machine.hostname,
        agility_inventory_machine.os,
        agility_inventory_machine.os_version,
        agility_inventory_machine.status
    FROM agility_inventory_machine
    WHERE agility_inventory_machine.`ID` = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_SEL_ALL`()
BEGIN
    SELECT
        agility_inventory_machine.ID,
        agility_inventory_machine.hostname,
        agility_inventory_machine.os,
        agility_inventory_machine.os_version,
        agility_inventory_machine.status
    FROM agility_inventory_machine;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_SEL_MAC`(
        IN mac VARCHAR(100)
    )
BEGIN
    SELECT
        agility_inventory_machine_mac.machine_id,
        agility_inventory_machine.hostname,
        agility_inventory_machine.os,
        agility_inventory_machine.os_version,
        agility_inventory_machine.status
    FROM agility_inventory_machine, agility_inventory_machine_mac
    WHERE agility_inventory_machine_mac.mac = mac AND agility_inventory_machine_mac.machine_id = agility_inventory_machine.ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_SOFTWARE_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_software
	WHERE agility_inventory_software.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_SOFTWARE_DEL_MACHINE`(
        IN machine_id BIGINT
    )
BEGIN
	DELETE
	FROM agility_inventory_software
	WHERE agility_inventory_software.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_SOFTWARE_INS`(
        IN machine_id BIGINT, IN nome VARCHAR(250))
BEGIN
    INSERT INTO agility_inventory_software (machine_id, nome)
    VALUES (machine_id, nome);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_SOFTWARE_SEL`(
        IN machine_id BIGINT)
BEGIN
    SELECT
        agility_inventory_software.ID,
        agility_inventory_software.machine_id,
        agility_inventory_software.nome
    FROM agility_inventory_software
    WHERE agility_inventory_software.machine_id = machine_id;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_SOFTWARE_SEL_ID`(
        IN ID BIGINT)
BEGIN
    SELECT
        agility_inventory_software.machine_id,
        agility_inventory_software.nome
    FROM agility_inventory_software
    WHERE agility_inventory_software.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_SOFTWARE_UP`(
        IN ID BIGINT, IN machine_id BIGINT, IN nome VARCHAR(250))
BEGIN
    UPDATE agility_inventory_software
    SET
        machine_id = machine_id,
        nome = nome
    WHERE agility_inventory_software.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_UP`(
        IN ID BIGINT, IN hostname VARCHAR(100), IN os VARCHAR(100),
        IN os_version VARCHAR(250), IN status TINYINT)
BEGIN
    UPDATE agility_inventory_machine
    SET
        hostname = hostname,
        os = os,
        os_version = os_version,
        status = status,
        vnc_status = 0
    WHERE agility_inventory_machine.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_UP_STATUS`(
        IN ID BIGINT, IN status TINYINT)
BEGIN
    UPDATE agility_inventory_machine
    SET       
        status = status
    WHERE agility_inventory_machine.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_LOG_TASK_DEL`(
        IN ID BIGINT
    )
BEGIN
    DELETE
    FROM agility_inventory_log_tasks
    WHERE agility_inventory_log_tasks.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_LOG_TASK_INS`(
        IN log longtext, IN machine_id BIGINT(20)
    )
BEGIN
    INSERT INTO agility_inventory_log_tasks (insert_date, log, machine_id)
    VALUES (NOW(), log, machine_id);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_GET_MACHINE_ID_FROM_IPV4`(
        IN ipv4 VARCHAR(50))
BEGIN
    SELECT
        agility_inventory_network.machine_id
    FROM agility_inventory_network
    WHERE agility_inventory_network.ipv4 = ipv4;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_GET_MACHINE_ID_FROM_MAC`(
        IN mac VARCHAR(50))
BEGIN
    SELECT
        agility_inventory_network.machine_id
    FROM agility_inventory_network
    WHERE agility_inventory_network.mac = mac;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_SCHEDULED_TASK_DEL`(
        IN ID BIGINT
    )
BEGIN
	DELETE
	FROM agility_scheduled_task
	WHERE agility_scheduled_task.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_SCHEDULED_TASK_INS`(
        IN command VARCHAR(250), IN scheduled_datetime DATETIME,
        IN status INTEGER(11), IN scheduled_parameter_1 longtext,
        IN scheduled_parameter_2 longtext, IN so VARCHAR(100), IN task VARCHAR(100)
    )
BEGIN
    INSERT INTO agility_scheduled_task (command, scheduled_datetime, status, scheduled_parameter_1, scheduled_parameter_2, so, task)
    VALUES (command, scheduled_datetime, status, scheduled_parameter_1, scheduled_parameter_2, so, task);
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_SCHEDULED_TASK_SEL_WAITING`()
BEGIN
    SELECT
        agility_scheduled_task.ID,
        agility_scheduled_task.command,
        agility_scheduled_task.scheduled_datetime,
        agility_scheduled_task.scheduled_parameter_1,
        agility_scheduled_task.scheduled_parameter_2,
        agility_scheduled_task.so,
        agility_scheduled_task.status,
        agility_scheduled_task.task        
    FROM agility_scheduled_task
    WHERE agility_scheduled_task.status = 0;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_SCHEDULED_TASK_SEL_WAITING_FROM_DATETIME`(
    IN scheduled_datetime DATETIME
    )
BEGIN
    SELECT
        agility_scheduled_task.ID,
        agility_scheduled_task.command,
        agility_scheduled_task.scheduled_datetime,
        agility_scheduled_task.scheduled_parameter_1,
        agility_scheduled_task.scheduled_parameter_2,
        agility_scheduled_task.so,
        agility_scheduled_task.status,
        agility_scheduled_task.task        
    FROM agility_scheduled_task
    WHERE agility_scheduled_task.status = 0 AND agility_scheduled_task.scheduled_datetime <= scheduled_datetime;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_SCHEDULED_TASK_UP`(
        IN ID BIGINT, IN command VARCHAR(250), IN scheduled_datetime DATETIME,
        IN status INTEGER(11), IN scheduled_parameter_1 longtext,
        IN scheduled_parameter_2 longtext, IN so VARCHAR(100), IN task VARCHAR(100)
        )
BEGIN
    UPDATE agility_scheduled_task
    SET
        command = command,
        scheduled_datetime = scheduled_datetime,        
        status = status,
        scheduled_parameter_1 = scheduled_parameter_1,
        scheduled_parameter_2 = scheduled_parameter_2,
        so = so,
        task = task
    WHERE agility_scheduled_task.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_SCHEDULED_TASK_UP_STATUS`(
        IN ID BIGINT, IN status INTEGER(11)
        )
BEGIN
    UPDATE agility_scheduled_task
    SET
        status = status
    WHERE agility_scheduled_task.ID = ID;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_IMAGE_SEL`(
        IN imageId INTEGER(11))
BEGIN
    SELECT
        upload_image.id,
        upload_image.description,
        upload_image.name,
        upload_image.os
    FROM upload_image
    WHERE upload_image.`id` = imageId;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_EMAIL_CONFIG_SEL`()
BEGIN
    SELECT
        configuration.id,
        configuration.email,
        configuration.password,
        configuration.smtp_port,
        configuration.smtp_server,
        configuration.user_name
    FROM configuration;
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_ADMIN_USERS_SEL`()
BEGIN
    SELECT
        user.email
    FROM user
    WHERE user.profile_rule = "ADMIN";
END$$

CREATE DEFINER=`root`@`%` PROCEDURE `SPD_MACHINE_UP_VNC_STATUS`(
        IN ID BIGINT, IN vnc_status TINYINT)
BEGIN
    UPDATE agility_inventory_machine
    SET
        vnc_status = vnc_status
    WHERE agility_inventory_machine.ID = ID;
END$$

DELIMITER ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
