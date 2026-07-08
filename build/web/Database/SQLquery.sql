CREATE DATABASE Torneos;

CREATE TABLE Usuario (
	UsuarioID INT AUTO_INCREMENT PRIMARY KEY,
	Nombre VARCHAR(100) NOT NULL,
	Correo VARCHAR(100) NOT NULL UNIQUE,
	Contrasena VARCHAR(100) NOT NULL
);

SELECT * FROM Usuario;
SELECT * FROM Equipo;

INSERT INTO Usuario (Nombre, Correo, Contrasena)
VALUES ('Daniela Jimenez', 'daniela@gmail.com', 'admin1234');

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
INSERT INTO Equipo (nombre, escudo, telefono, UsuarioID)
VALUES ('Saprissa', 'saprissa.png', '+50688888888', 1);

SELECT * FROM Equipo;

CREATE TABLE Torneo(
	id_torneo INT AUTO_INCREMENT PRIMARY KEY,
	nombre varchar(100) not null,
	imagen varchar(255),
	fechaInicio date not null,
	fechaFinal date not null,
	premio decimal(10,2)not null 
);