CREATE DATABASE IF NOT EXISTS Torneos
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE Torneos;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS Partidos;
DROP TABLE IF EXISTS TorneoEquipo;
DROP TABLE IF EXISTS Torneo;
DROP TABLE IF EXISTS Equipo;
DROP TABLE IF EXISTS Usuario;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE Usuario (
    UsuarioID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Correo VARCHAR(100) NOT NULL UNIQUE,
    Contrasena VARCHAR(100) NOT NULL,
    Rol ENUM('ADMIN', 'USUARIO')
        NOT NULL DEFAULT 'USUARIO'
);

INSERT INTO Usuario (Nombre, Correo,Contrasena,Rol)
VALUES ('Administrador de torneos','admin@torneos.com','admin1234','ADMIN'),
		('user', 'user@torneos.com', 'user1234', 'USUARIO');
;


CREATE TABLE Equipo (
    id_equipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    escudo VARCHAR(250) NOT NULL,
    telefono VARCHAR(30) NOT NULL,
    estado ENUM('PENDIENTE','APROBADO','RECHAZADO') NOT NULL DEFAULT 'PENDIENTE',
    UsuarioID INT NOT NULL,
    INDEX idx_equipo_estado (estado),
    CONSTRAINT fk_equipo_usuario
        FOREIGN KEY (UsuarioID)
        REFERENCES Usuario(UsuarioID)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

SET @usuario_id = (
    SELECT UsuarioID
    FROM Usuario
    WHERE Correo = 'user@torneos.com'
);
SET @administrador_id = (
    SELECT UsuarioID
    FROM Usuario
    WHERE Correo = 'admin@torneos.com'
);

INSERT INTO Equipo (nombre,escudo,telefono,estado,UsuarioID)
VALUES
    ('Deportivo Saprissa','saprissa.png','11111111','APROBADO', @usuario_id),
    ('Alajuelense','alajuelense.png','11111112','APROBADO',@usuario_id),
    ('Cartaginés','cartagines.png','11111113','APROBADO',@usuario_id),
    ('Herediano','herediano.png','11111114','APROBADO',@usuario_id),
    ('Pérez Zeledón','perez_zeledon.png','11111115','APROBADO',@usuario_id),
	('Santos', 'santos.png', '11111116', 'APROBADO', @usuario_id),
	('Grecia', 'grecia.png', '11111117', 'APROBADO', @usuario_id),
	('Liberia', 'liberia.png', '11111118', 'APROBADO', @usuario_id),
	('San Carlos', 'san_carlos.png', '11111119', 'APROBADO', @usuario_id),
	('Guanacasteca', 'guanacasteca.png', '11111120', 'APROBADO', @usuario_id),
	('Puntarenas', 'puntarenas.png', '11111121', 'APROBADO', @usuario_id),
	('Cariari', 'cariari.png', '11111122', 'APROBADO', @usuario_id),
	('Jicaral', 'jicaral.png', '11111123', 'APROBADO', @usuario_id),
	('Limón', 'limon.png', '11111124', 'APROBADO', @usuario_id),
	('Uruguay de Coronado', 'uruguay_coronado.png', '11111125', 'APROBADO', @usuario_id),
	('Municipal Grecia', 'municipal_grecia.png', '11111126', 'APROBADO', @usuario_id);

CREATE TABLE Torneo (
    torneo_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    imagen VARCHAR(255) NOT NULL,
    fechaInicio DATE NOT NULL,
    fechaFinal DATE NOT NULL,
    premio DECIMAL(12,2) NOT NULL,
    estado ENUM('PENDIENTE','ACTIVO','FINALIZADO') NOT NULL DEFAULT 'ACTIVO',
    campeon_id INT,

    /*
     * Cuando el torneo está ACTIVO genera el número 1.
     * Los demás estados generan NULL.
     */
    activo_unico TINYINT
        GENERATED ALWAYS AS (
            CASE
                WHEN estado = 'ACTIVO' THEN 1
                ELSE NULL
            END
        ) STORED,

    CONSTRAINT uq_unico_torneo_activo
        UNIQUE (activo_unico),

    CONSTRAINT chk_fechas_torneo
        CHECK (fechaFinal >= fechaInicio),

    CONSTRAINT chk_premio_torneo
        CHECK (premio >= 0),

    CONSTRAINT fk_torneo_campeon
        FOREIGN KEY (campeon_id)
        REFERENCES Equipo(id_equipo)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE TABLE TorneoEquipo (
    torneo_id INT NOT NULL,
    equipo_id INT NOT NULL,

    PRIMARY KEY (torneo_id, equipo_id),

    CONSTRAINT fk_torneo_equipo_torneo
        FOREIGN KEY (torneo_id)
        REFERENCES Torneo(torneo_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_torneo_equipo_equipo
        FOREIGN KEY (equipo_id)
        REFERENCES Equipo(id_equipo)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE Partidos (
    id_partidos INT AUTO_INCREMENT PRIMARY KEY,
    torneo_id INT NOT NULL,
    ronda INT NOT NULL,
    posicion_llave INT NOT NULL,
    equipo_local_id INT,
    equipo_visita_id INT,
    marcador_local INT,
    marcador_visita INT,
    estado ENUM('PENDIENTE', 'FINALIZADO')
        NOT NULL DEFAULT 'PENDIENTE',
    ganador_id INT,
    siguiente_partido_id INT,
    posicion_siguiente_local_visita
        ENUM('LOCAL', 'VISITA'),

    CONSTRAINT uq_partido_posicion
        UNIQUE (torneo_id, ronda, posicion_llave),

    CONSTRAINT chk_marcador_local
        CHECK (
            marcador_local IS NULL
            OR marcador_local >= 0
        ),

    CONSTRAINT chk_marcador_visita
        CHECK (
            marcador_visita IS NULL
            OR marcador_visita >= 0
        ),

    CONSTRAINT fk_partido_torneo
        FOREIGN KEY (torneo_id)
        REFERENCES Torneo(torneo_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    CONSTRAINT fk_partido_local
        FOREIGN KEY (equipo_local_id)
        REFERENCES Equipo(id_equipo)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_partido_visita
        FOREIGN KEY (equipo_visita_id)
        REFERENCES Equipo(id_equipo)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_partido_ganador
        FOREIGN KEY (ganador_id)
        REFERENCES Equipo(id_equipo)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_partido_siguiente
        FOREIGN KEY (siguiente_partido_id)
        REFERENCES Partidos(id_partidos)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);


SELECT * FROM Usuario;

SELECT * FROM Equipo
ORDER BY nombre;

SELECT COUNT(*) AS equipos_aprobados
FROM Equipo
WHERE estado = 'APROBADO';

SELECT * FROM Torneo;

SELECT * FROM TorneoEquipo;

SELECT * FROM Partidos;