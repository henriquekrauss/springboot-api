CREATE TABLE `medico` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(100) NOT NULL,
    `senha` VARCHAR(255) NOT NULL,
    `especialidade` VARCHAR(255) NOT NULL,
    `cpf` VARCHAR(11) NOT NULL,
    `data_nascimento` DATETIME NOT NULL,
    `telefone` VARCHAR(11) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `paciente` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `nome` VARCHAR(255) NOT NULL,
    `cpf` VARCHAR(11) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `atendimento` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `data_atendimento` DATETIME NOT NULL,
    `id_medico` INT NOT NULL,
    `id_paciente` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`id_medico`) REFERENCES `medico` (`id`),
    FOREIGN KEY (`id_paciente`) REFERENCES `paciente` (`id`)
);