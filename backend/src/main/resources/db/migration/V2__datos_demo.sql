-- Datos mínimos para la demostración.
-- Códigos UAM-xxx-nnn reservados para estos datos; en Postman usen otros
-- códigos al crear equipos nuevos (por ejemplo UAM-TEST-001).

INSERT INTO equipos (codigo_patrimonial, nombre, tipo, estado) VALUES
    ('UAM-LAP-001', 'Laptop Dell Latitude 5420',  'LAPTOP',    'PRESTADO'),
    ('UAM-LAP-002', 'Laptop Lenovo ThinkPad E14', 'LAPTOP',    'DISPONIBLE'),
    ('UAM-LAP-003', 'Laptop HP ProBook 450',      'LAPTOP',    'DISPONIBLE'),
    ('UAM-PRO-001', 'Proyector Epson X49',        'PROYECTOR', 'DISPONIBLE'),
    ('UAM-PRO-002', 'Proyector BenQ MS550',       'PROYECTOR', 'DISPONIBLE'),
    ('UAM-TAB-001', 'Tableta Samsung Galaxy Tab A8', 'TABLETA', 'DISPONIBLE'),
    ('UAM-TAB-002', 'Tableta Apple iPad 9',       'TABLETA',   'DISPONIBLE');

-- UAM-LAP-001 tiene un préstamo ACTIVO (sirve para probar el 409 y el
-- bloqueo de eliminación).
INSERT INTO prestamos (equipo_id, nombre_solicitante, fecha_prestamo,
                       fecha_prevista_devolucion, fecha_devolucion_real, estado)
SELECT id, 'Carlos Mendoza', CURRENT_DATE - 2, CURRENT_DATE + 5, NULL, 'ACTIVO'
FROM equipos WHERE codigo_patrimonial = 'UAM-LAP-001';

-- Historial: un préstamo ya devuelto de UAM-PRO-001.
INSERT INTO prestamos (equipo_id, nombre_solicitante, fecha_prestamo,
                       fecha_prevista_devolucion, fecha_devolucion_real, estado)
SELECT id, 'Ana Gutiérrez', CURRENT_DATE - 10, CURRENT_DATE - 7, CURRENT_DATE - 8, 'DEVUELTO'
FROM equipos WHERE codigo_patrimonial = 'UAM-PRO-001';
