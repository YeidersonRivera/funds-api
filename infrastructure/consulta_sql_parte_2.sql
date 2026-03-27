-- ============================================================
-- BTG Pactual – Prueba Técnica | Parte 2: SQL (20%)
-- Base de datos: BTG
-- ============================================================
-- Consulta solicitada:
--   Obtener los nombres de los clientes que tienen inscrito
--   algún producto disponible en las sucursales que visitan.
-- ============================================================

-- ── Creación de tablas ─────────────────────────────────────

CREATE TABLE cliente (
                         id        INT PRIMARY KEY,
                         nombre    VARCHAR(100) NOT NULL,
                         apellidos VARCHAR(100) NOT NULL,
                         ciudad    VARCHAR(100) NOT NULL
);

CREATE TABLE producto (
                          id           INT PRIMARY KEY,
                          nombre       VARCHAR(100) NOT NULL,
                          tipoProducto VARCHAR(100) NOT NULL
);

CREATE TABLE sucursal (
                          id     INT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          ciudad VARCHAR(100) NOT NULL
);

CREATE TABLE inscripcion (
                             idProducto INT NOT NULL,
                             idCliente  INT NOT NULL,
                             PRIMARY KEY (idProducto, idCliente),
                             FOREIGN KEY (idProducto) REFERENCES producto(id),
                             FOREIGN KEY (idCliente)  REFERENCES cliente(id)
);

CREATE TABLE disponibilidad (
                                idSucursal INT NOT NULL,
                                idProducto INT NOT NULL,
                                PRIMARY KEY (idSucursal, idProducto),
                                FOREIGN KEY (idSucursal) REFERENCES sucursal(id),
                                FOREIGN KEY (idProducto) REFERENCES producto(id)
);

CREATE TABLE visitan (
                         idSucursal  INT  NOT NULL,
                         idCliente   INT  NOT NULL,
                         fechaVisita DATE NOT NULL,
                         PRIMARY KEY (idSucursal, idCliente),
                         FOREIGN KEY (idSucursal) REFERENCES sucursal(id),
                         FOREIGN KEY (idCliente)  REFERENCES cliente(id)
);

-- ── Datos de prueba ────────────────────────────────────────

INSERT INTO cliente VALUES (1, 'Juan',   'García',  'Bogotá');
INSERT INTO cliente VALUES (2, 'María',  'López',   'Medellín');
INSERT INTO cliente VALUES (3, 'Carlos', 'Pérez',   'Cali');

INSERT INTO producto VALUES (1, 'Cuenta Ahorros',  'AHORRO');
INSERT INTO producto VALUES (2, 'CDT',              'INVERSION');
INSERT INTO producto VALUES (3, 'Tarjeta Crédito',  'CREDITO');

INSERT INTO sucursal VALUES (1, 'Sucursal Norte',  'Bogotá');
INSERT INTO sucursal VALUES (2, 'Sucursal Sur',    'Bogotá');
INSERT INTO sucursal VALUES (3, 'Sucursal Centro', 'Medellín');

-- Disponibilidad (no todas las sucursales ofrecen los mismos productos)
INSERT INTO disponibilidad VALUES (1, 1); -- Norte  → Cuenta Ahorros
INSERT INTO disponibilidad VALUES (2, 1); -- Sur    → Cuenta Ahorros
INSERT INTO disponibilidad VALUES (1, 2); -- Norte  → CDT
INSERT INTO disponibilidad VALUES (3, 3); -- Centro → Tarjeta Crédito

-- Visitas
INSERT INTO visitan VALUES (1, 1, '2025-01-10'); -- Juan   visita Norte
INSERT INTO visitan VALUES (2, 1, '2025-01-15'); -- Juan   visita Sur
INSERT INTO visitan VALUES (3, 2, '2025-01-12'); -- María  visita Centro
INSERT INTO visitan VALUES (2, 3, '2025-01-08'); -- Carlos visita Sur

-- Inscripciones
INSERT INTO inscripcion VALUES (1, 1); -- Juan   → Cuenta Ahorros
INSERT INTO inscripcion VALUES (2, 1); -- Juan   → CDT
INSERT INTO inscripcion VALUES (3, 2); -- María  → Tarjeta Crédito
INSERT INTO inscripcion VALUES (2, 3); -- Carlos → CDT

-- ── Consulta solicitada ────────────────────────────────────
-- Obtener los clientes que tienen inscrito algún producto
-- disponible en las sucursales que visitan.
--
-- Lógica:
--   INNER JOIN inscripcion  → productos inscritos del cliente
--   INNER JOIN disponibilidad → sucursales donde ese producto existe
--   INNER JOIN visitan      → filtra solo donde el cliente visita esa sucursal
--
-- Resultado esperado: Juan García, María López
-- Carlos Pérez no aparece porque tiene CDT inscrito (disponible en Norte)
-- pero él solo visita Sur.
-- ============================================================

SELECT DISTINCT
    c.nombre,
    c.apellidos
FROM cliente c
         INNER JOIN inscripcion i
                    ON i.idCliente = c.id
         INNER JOIN disponibilidad d
                    ON d.idProducto = i.idProducto
         INNER JOIN visitan v
                    ON v.idSucursal = d.idSucursal
                        AND v.idCliente = c.id
GROUP BY c.nombre, c.apellidos
ORDER BY c.apellidos, c.nombre;