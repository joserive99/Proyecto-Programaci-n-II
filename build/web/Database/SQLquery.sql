
CREATE DATABASE Torneos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE Torneos;

CREATE TABLE Usuario (
    UsuarioID INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Correo VARCHAR(100) NOT NULL UNIQUE,
    Contrasena VARCHAR(100) NOT NULL,
    Rol ENUM('ADMIN','USUARIO') NOT NULL DEFAULT 'USUARIO'
);

INSERT INTO Usuario (Nombre,Correo,Contrasena,Rol) VALUES
('Administrador de torneos','admin@torneos.com','admin1234','ADMIN'),
('user','user@torneos.com','user1234','USUARIO');


CREATE TABLE Equipo (
    id_equipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    escudo VARCHAR(500) NOT NULL,
    telefono VARCHAR(30) NOT NULL,
    UsuarioID INT NOT NULL,
    CONSTRAINT fk_equipo_usuario FOREIGN KEY (UsuarioID) REFERENCES Usuario(UsuarioID) ON UPDATE CASCADE ON DELETE RESTRICT
);

SET @usuario_id = (SELECT UsuarioID FROM Usuario WHERE Correo = 'user@torneos.com');

INSERT INTO Equipo (nombre,escudo,telefono,UsuarioID) VALUES
('Deportivo Saprissa','https://upload.wikimedia.org/wikipedia/commons/4/4d/Escudo_del_Deportivo_Saprissa.png','8888-1001',@usuario_id),
('Alajuelense','https://upload.wikimedia.org/wikipedia/commons/b/b0/Escudo_de_la_Liga_Deportiva_Alajuelense.png','8888-1002',@usuario_id),
('Cartaginés','https://upload.wikimedia.org/wikipedia/commons/5/54/Escudo_del_Club_Sport_Cartagin%C3%A9s.png','8888-1003',@usuario_id),
('Herediano','https://upload.wikimedia.org/wikipedia/commons/c/c9/Escudo_del_Club_Sport_Herediano.svg','8888-1004',@usuario_id),
('Pérez Zeledón','https://upload.wikimedia.org/wikipedia/commons/3/31/Escudo_de_la_Asociaci%C3%B3n_Deportiva_Municipal_de_P%C3%A9rez_Zeled%C3%B3n.png','8888-1005',@usuario_id),
('Santos','https://upload.wikimedia.org/wikipedia/commons/a/a7/Escudo_del_Santos_de_Gu%C3%A1piles.svg','8888-1006',@usuario_id),
('Grecia','https://images.seeklogo.com/logo-png/0/1/ad-municipal-grecia-logo-png_seeklogo-3084.png','8888-1007',@usuario_id),
('Liberia','https://images.seeklogo.com/logo-png/9/1/municipal-liberia-logo-png_seeklogo-96088.png','8888-1008',@usuario_id),
('San Carlos','https://upload.wikimedia.org/wikipedia/commons/6/6a/Escudo_de_Asociaci%C3%B3n_Deportiva_San_Carlos_pre-2018.svg','8888-1009',@usuario_id),
('Guanacasteca','https://images.seeklogo.com/logo-png/66/1/guanacasteca-logo-png_seeklogo-665244.png','8888-1010',@usuario_id),
('Puntarenas','https://pub-3bd35431294c47068cbf31a95d572166.r2.dev/logos/puntarenas-fc/puntarenas-fc-logo-footylogos.png','8888-1011',@usuario_id),
('Cariari','https://images.seeklogo.com/logo-png/26/1/ad-cariari-pococi-logo-png_seeklogo-265341.png','8888-1012',@usuario_id),
('Jicaral','https://images.seeklogo.com/logo-png/48/1/jicaral-sercoba-logo-png_seeklogo-486548.png','8888-1013',@usuario_id),
('Limón','https://upload.wikimedia.org/wikipedia/commons/9/90/Lim%C3%B3n_F%C3%BAtbol_Club.png','8888-1014',@usuario_id),
('Uruguay de Coronado','https://images.seeklogo.com/logo-png/26/1/club-sport-uruguay-de-coronado-logo-png_seeklogo-261970.png','8888-1015',@usuario_id),
('Municipal Grecia','https://images.seeklogo.com/logo-png/0/1/ad-municipal-grecia-logo-png_seeklogo-3084.png','8888-1016',@usuario_id);


CREATE TABLE Torneo (
    torneo_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    imagen VARCHAR(500) NOT NULL,
    fechaInicio DATE NOT NULL,
    fechaFinal DATE NOT NULL,
    premio DECIMAL(12,2) NOT NULL,
    estado ENUM('PENDIENTE','ACTIVO','FINALIZADO') NOT NULL DEFAULT 'ACTIVO',
    campeon_id INT,
    activo_unico TINYINT GENERATED ALWAYS AS (CASE WHEN estado = 'ACTIVO' THEN 1 ELSE NULL END) STORED,
    CONSTRAINT uq_unico_torneo_activo UNIQUE (activo_unico),
    CONSTRAINT chk_fechas_torneo CHECK (fechaFinal >= fechaInicio),
    CONSTRAINT chk_premio_torneo CHECK (premio >= 0),
    CONSTRAINT fk_torneo_campeon FOREIGN KEY (campeon_id) REFERENCES Equipo(id_equipo) ON UPDATE CASCADE ON DELETE SET NULL
);


CREATE TABLE TorneoEquipo (
    torneo_id INT NOT NULL,
    equipo_id INT NOT NULL,
    PRIMARY KEY (torneo_id,equipo_id),
    CONSTRAINT fk_torneo_equipo_torneo FOREIGN KEY (torneo_id) REFERENCES Torneo(torneo_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_torneo_equipo_equipo FOREIGN KEY (equipo_id) REFERENCES Equipo(id_equipo) ON UPDATE CASCADE ON DELETE RESTRICT
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
    estado ENUM('PENDIENTE','FINALIZADO') NOT NULL DEFAULT 'PENDIENTE',
    ganador_id INT,
    siguiente_partido_id INT,
    posicion_siguiente_local_visita ENUM('LOCAL','VISITA'),
    CONSTRAINT uq_partido_posicion UNIQUE (torneo_id,ronda,posicion_llave),
    CONSTRAINT chk_ronda CHECK (ronda BETWEEN 1 AND 4),
    CONSTRAINT chk_marcador_local CHECK (marcador_local IS NULL OR marcador_local >= 0),
    CONSTRAINT chk_marcador_visita CHECK (marcador_visita IS NULL OR marcador_visita >= 0),
    CONSTRAINT fk_partido_torneo FOREIGN KEY (torneo_id) REFERENCES Torneo(torneo_id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_partido_local FOREIGN KEY (equipo_local_id) REFERENCES Equipo(id_equipo) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_partido_visita FOREIGN KEY (equipo_visita_id) REFERENCES Equipo(id_equipo) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_partido_ganador FOREIGN KEY (ganador_id) REFERENCES Equipo(id_equipo) ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_partido_siguiente FOREIGN KEY (siguiente_partido_id) REFERENCES Partidos(id_partidos) ON UPDATE CASCADE ON DELETE SET NULL
);


SELECT * FROM Usuario;

SELECT id_equipo,nombre,escudo,telefono,UsuarioID FROM Equipo ORDER BY nombre;

SELECT COUNT(*) AS total_equipos FROM Equipo;

SELECT * FROM Torneo;

SELECT * FROM TorneoEquipo;

SELECT * FROM Partidos;