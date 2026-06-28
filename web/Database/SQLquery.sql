CREATE DATABASE Tornedos;

CREATE TABLE Usuario (
	UsuarioID INT AUTO_INCREMENT PRIMARY KEY,
	Nombre VARCHAR(100) NOT NULL,
	Correo VARCHAR(100) NOT NULL UNIQUE,
	Contrasena VARCHAR(100) NOT NULL
);

SELECT * FROM Usuario;

INSERT INTO Usuario (Nombre, Correo, Contrasena)
VALUES ('Daniela Jimenez', 'daniela@gmail.com', 'admin1234')