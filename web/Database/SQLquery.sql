CREATE DATABASE Torneos;
USE Torneos;

CREATE TABLE Usuario (
	UsuarioID INT AUTO_INCREMENT PRIMARY KEY,
	Nombre VARCHAR(100) NOT NULL,
	Correo VARCHAR(100) NOT NULL UNIQUE,
	Contrasena VARCHAR(100) NOT NULL
);

SELECT * FROM Usuario;
SELECT * FROM Equipo;

INSERT INTO Usuario (Nombre, Correo, Contrasena)
VALUES ('Daniela Jimenez', 'daniela@gmail.com', 'admin1234'),
 ('Administrador de torneos', 'admin@torneos.com', 'admin1234');    # 16


ALTER TABLE Usuario													# 19-23
ADD COLUMN Rol ENUM('ADMIN','USUARIO') NOT NULL DEFAULT 'USUARIO';
UPDATE Usuario
SET Rol='ADMIN'
WHERE Correo='admin@torneos.com';

CREATE TABLE Equipo (
    id_equipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    escudo VARCHAR(250) NOT NULL,
    telefono VARCHAR(30) NOT NULL,
    UsuarioID INT NOT NULL,

    CONSTRAINT fk_equipo_usuario
    FOREIGN KEY (UsuarioID) REFERENCES Usuario(UsuarioID)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);
DELETE FROM Equipo;
SET SQL_SAFE_UPDATES = 0;
SET SQL_SAFE_UPDATES = 1;
INSERT INTO Equipo (nombre, escudo, telefono, UsuarioID)
VALUES 
('Saprissa', 'saprissa.png', '+50688888888', 1),
('Alajuelense', 'alajuelense.png', '11111112', 1),
('Cartaginés', 'cartagines.png', '11111113', 1),
('Herediano', 'herediano.png', '11111114', 1),
('Pérez Zeledón', 'perez_zeledon.png', '11111115', 1),
('Santos', 'santos.png', '11111116', 1),
('Grecia', 'grecia.png', '11111117', 1),
('Liberia', 'liberia.png', '11111118', 1),
('San Carlos', 'san_carlos.png', '11111119', 1),
('Guanacasteca', 'guanacasteca.png', '11111120', 1),
('Puntarenas', 'puntarenas.png', '11111121', 1),
('Cariari', 'cariari.png', '11111122', 1),
('Jicaral', 'jicaral.png', '11111123', 1),
('Limón', 'limon.png', '11111124', 1),
('Uruguay de Coronado', 'uruguay_coronado.png', '11111126', 1),
('Municipal Grecia', 'municipal_grecia.png', '11111117', 1);
SELECT * FROM Equipo;
DESCRIBE Equipo;
CREATE TABLE Torneo(
	  torneo_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre varchar(100) not null,
	imagen varchar(255),
	fechaInicio date not null,
	fechaFinal date not null,
	premio decimal(10,2)not null,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    campeon_id INT,
    FOREIGN KEY (campeon_id) REFERENCES Equipo(id_equipo) ON DELETE SET NULL
);

SELECT * FROM partidos;

CREATE TABLE partidos (
    id_partidos INT AUTO_INCREMENT PRIMARY KEY,
    torneo_id INT NOT NULL,
    ronda INT NOT NULL, 
    posicion_llave INT NOT NULL,
    equipo_local_id INT,
    equipo_visita_id INT,
    marcador_local INT,
    marcador_visita INT,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE', 
    ganador_id INT,
    siguiente_partido_id INT, 
    posicion_siguiente_local_visita VARCHAR(10), 
    FOREIGN KEY (torneo_id)
        REFERENCES Torneo(torneo_id)
        ON DELETE CASCADE,

    FOREIGN KEY (equipo_local_id)
        REFERENCES Equipo(id_equipo)
        ON DELETE SET NULL,

    FOREIGN KEY (equipo_visita_id)
        REFERENCES Equipo(id_equipo)
        ON DELETE SET NULL,

    FOREIGN KEY (ganador_id)
        REFERENCES Equipo(id_equipo)
        ON DELETE SET NULL,

    FOREIGN KEY (siguiente_partido_id)
        REFERENCES Partidos(id_partidos)
        ON DELETE SET NULL
);