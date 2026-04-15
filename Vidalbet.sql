create database if not exists vidalbet;
use vidalbet;
-- 2. Tabla de Usuarios
-- Almacena la infor del perfil y el saldo actual.
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, 
    email VARCHAR(100) NOT NULL UNIQUE,
    saldo DECIMAL(10, 2) DEFAULT 0.00,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabla de Apuestas
-- Aquí se guarda lo que el usuario envía desde JavaFX y el premio que calcula COBOL.
CREATE TABLE IF NOT EXISTS apuestas (
    id_apuesta INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    importe DECIMAL(10, 2) NOT NULL,
    cuota DECIMAL(5, 2) NOT NULL,
    premio_posible DECIMAL(10, 2), -- Este lo llenaremos tras el cálculo de COBOL
    estado ENUM('pendiente', 'ganada', 'perdida') DEFAULT 'pendiente',
    fecha_apuesta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

-- 4. Tabla de Historial/Transacciones 
-- Para tener un registro de depósitos y cobros.
CREATE TABLE IF NOT EXISTS transacciones (
    id_transaccion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    tipo ENUM('deposito', 'apuesta', 'premio', 'retiro'),
    cantidad DECIMAL(10, 2),
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- 5. Datos de prueba para empezar a trabajar mañana
INSERT INTO usuarios (username, password, email, saldo) VALUES 
('paco_bet', '1234', 'paco@mail.com', 500.00),
('maria_vidal', 'vidal123', 'maria@mail.com', 150.00);

INSERT INTO apuestas (id_usuario, importe, cuota, premio_posible, estado) VALUES 
(1, 10.00, 2.50, 25.00, 'ganada'),
(2, 50.00, 1.80, 90.00, 'pendiente');

-- Añadimos una regla para que el saldo nunca baje de 0
ALTER TABLE usuarios ADD CONSTRAINT check_saldo_positivo CHECK (saldo >= 0);

-- poder entrar sin contraseña!! --
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY '';
FLUSH PRIVILEGES;


