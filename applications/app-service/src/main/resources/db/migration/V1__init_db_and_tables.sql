-- Create target database if not exists (uses Flyway placeholder ${db})
CREATE DATABASE IF NOT EXISTS `${db}` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create roles table
CREATE TABLE IF NOT EXISTS `${db}`.`roles` (
  `rol_id` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `descripcion` VARCHAR(255) NULL,
  PRIMARY KEY (`rol_id`)
) ENGINE=InnoDB;

-- Create usuarios table
CREATE TABLE IF NOT EXISTS `${db}`.`usuarios` (
  `id_usuario` BIGINT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido` VARCHAR(100) NOT NULL,
  `fecha_nacimiento` VARCHAR(20) NULL,
  `direccion` VARCHAR(200) NULL,
  `telefono` VARCHAR(50) NULL,
  `correo_electronico` VARCHAR(150) NULL,
  `salario_base` DECIMAL(15,2) NULL,
  `documento_identidad` VARCHAR(50) NULL,
  `rol_id` BIGINT NULL,
  PRIMARY KEY (`id_usuario`),
  CONSTRAINT `fk_usuarios_roles` FOREIGN KEY (`rol_id`) REFERENCES `${db}`.`roles`(`rol_id`)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB;

-- Helpful index for FK
CREATE INDEX `idx_usuarios_rol_id` ON `${db}`.`usuarios` (`rol_id`);
