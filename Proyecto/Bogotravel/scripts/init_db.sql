-- Eliminar la base de datos si existe y crearla de nuevo
CREATE DATABASE bogotravel;

-- Cambiar a la base de datos recién creada
\c bogotravel;

-- =============================
-- Tabla: usuarios
-- =============================
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password TEXT NOT NULL
);

-- =============================
-- Tabla: categorías
-- =============================
CREATE TABLE IF NOT EXISTS categorias (
    id SERIAL PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE
);

-- Insertar categorías por defecto
INSERT INTO categorias (nombre)
VALUES
    ('Cultural'),
    ('Natural'),
    ('Gastronómico'),
    ('Tecnológico'),
    ('Recreativo'),
    ('Religioso'),
    ('Histórico')
ON CONFLICT DO NOTHING;

-- =============================
-- Tabla: lugares turísticos
-- =============================
CREATE TABLE IF NOT EXISTS lugares_turisticos (
    id SERIAL PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT,
    localidad TEXT,
    id_categoria INT,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id) ON DELETE SET NULL
);

-- Insertar lugares turísticos de Bogotá
INSERT INTO lugares_turisticos (nombre, descripcion, localidad, id_categoria)
VALUES
    ('Monserrate', 'Cerro emblemático con vista panorámica', 'Santa Fe', 1),
    ('Museo del Oro', 'Colección de piezas precolombinas', 'La Candelaria', 1),
    ('Jardín Botánico', 'Espacio verde con especies nativas', 'Engativá', 2),
    ('Andrés Carne de Res DC', 'Restaurante típico muy visitado', 'Chapinero', 3),
    ('Maloka', 'Museo interactivo de ciencia y tecnología', 'Fontibón', 4),
    ('Parque Simón Bolívar', 'Parque con lago y eventos culturales', 'Teusaquillo', 5)
ON CONFLICT DO NOTHING;

-- =============================
-- Tabla: entradas (diario)
-- =============================
CREATE TABLE IF NOT EXISTS entradas (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    contenido TEXT,
    fecha_visita DATE NOT NULL,
    lugar_descripcion VARCHAR(100),
    email_usuario VARCHAR(100) NOT NULL,
    FOREIGN KEY (email_usuario) REFERENCES usuarios(email) ON DELETE CASCADE
);

-- =============================
-- Tabla: lugares por visitar
-- =============================
CREATE TABLE IF NOT EXISTS por_visitar (
    id SERIAL PRIMARY KEY,
    id_lugar INT NOT NULL,
    prioridad INT DEFAULT 1,
    recordatorio DATE,
    email_usuario VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_lugar) REFERENCES lugares_turisticos(id) ON DELETE CASCADE,
    FOREIGN KEY (email_usuario) REFERENCES usuarios(email) ON DELETE CASCADE
);

-- =============================
-- Tabla: fotos asociadas a entradas
-- =============================
CREATE TABLE IF NOT EXISTS fotos_entrada (
    id SERIAL PRIMARY KEY,
    entrada_id INTEGER NOT NULL,
    ruta TEXT NOT NULL,
    FOREIGN KEY (entrada_id) REFERENCES entradas(id) ON DELETE CASCADE
);
