-- Categorías específicas para tecnología/electrodomésticos
INSERT INTO categorias (nombre, descripcion) VALUES
('Computadoras', 'Laptops, PCs, Servidores'),
('Periféricos', 'Teclados, Mouse, Monitores, Impresoras'),
('Audio/Video', 'TVs, Parlantes, Audífonos, Proyectores'),
('Electrodomésticos', 'Refrigeradoras, Lavadoras, Cocinas'),
('Celulares', 'Smartphones, Tablets, Accesorios'),
('Componentes', 'Procesadores, RAM, Tarjetas Gráficas, Discos'),
('Redes', 'Routers, Switches, Cables, Accesorios de red'),
('Software', 'Sistemas operativos, Aplicaciones, Licencias');

-- Usuarios
INSERT INTO usuarios (username, password, nombre, email, rol) VALUES
('admin', 'admin123', 'Administrador', 'admin@sigi.com', 'ADMIN'),
('tecnico', 'tecnico123', 'Técnico de Inventario', 'tecnico@sigi.com', 'TECNICO'),
('vendedor', 'vendedor123', 'Vendedor', 'vendedor@sigi.com', 'VENDEDOR');

-- Productos de tecnología/electrodomésticos
INSERT INTO productos (codigo, nombre, descripcion, precio, stock_actual, stock_minimo, categoria_id) VALUES
-- Computadoras
('LAP-DELL-001', 'Laptop Dell Inspiron 15', 'Core i5, 8GB RAM, 512GB SSD, Windows 11', 2500.00, 15, 5, 1),
('LAP-HP-001', 'Laptop HP Pavilion', 'Core i7, 16GB RAM, 1TB SSD, NVIDIA MX450', 3500.00, 10, 3, 1),
('PC-GAMER-001', 'PC Gamer Armada', 'Ryzen 5, 16GB RAM, RTX 3060, 1TB SSD', 4500.00, 8, 2, 1),

-- Periféricos
('MON-SAMS-001', 'Monitor Samsung 24"', 'LED Full HD 75Hz, HDMI, VGA', 450.00, 25, 10, 2),
('TEC-MEC-001', 'Teclado Mecánico Redragon', 'RGB, switches azules, gaming', 120.00, 50, 20, 2),
('MOUSE-LOG-001', 'Mouse Logitech G203', 'RGB, 8000 DPI, gaming', 60.00, 40, 15, 2),

-- Audio/Video
('TV-LG-001', 'TV LG 55" 4K', 'Smart TV, WebOS, 4K UHD', 1800.00, 12, 4, 3),
('PARL-JBL-001', 'Parlante JBL Flip 5', 'Bluetooth, resistente al agua', 150.00, 30, 10, 3),

-- Electrodomésticos
('REFR-SAMS-001', 'Refrigeradora Samsung', '2 puertas, 500L, inverter', 2200.00, 8, 3, 4),
('LAVA-LG-001', 'Lavadora LG', 'Carga frontal, 15kg, vapor', 1300.00, 10, 4, 4),
('MICRO-PANA-001', 'Microondas Panasonic', '30L, 1100W, grill', 300.00, 25, 8, 4),

-- Celulares
('CEL-SAMS-001', 'Samsung Galaxy S23', '256GB, 8GB RAM, Android 13', 3200.00, 18, 6, 5),
('CEL-IPHONE-001', 'iPhone 14 Pro', '256GB, iOS 16, cámara 48MP', 4200.00, 12, 4, 5),
('TAB-SAMS-001', 'Tablet Samsung S8', '11", 256GB, S Pen incluido', 850.00, 20, 8, 5),

-- Componentes
('CPU-INTEL-001', 'Procesador Intel i5-13400', '10 núcleos, 4.6GHz, LGA1700', 280.00, 35, 12, 6),
('RAM-KING-001', 'RAM Kingston 16GB', 'DDR4 3200MHz, CL16', 75.00, 60, 25, 6),
('SSD-SAMS-001', 'SSD Samsung 1TB', 'NVMe M.2, 7000MB/s', 110.00, 45, 18, 6),

-- Redes
('ROUT-TP-001', 'Router TP-Link Archer', 'WiFi 6, 3000Mbps, 4 antenas', 120.00, 30, 10, 7),
('SWITCH-001', 'Switch Netgear 8 puertos', 'Gigabit, metal, fanless', 65.00, 25, 8, 7),

-- Software
('WIN11-001', 'Windows 11 Pro', 'Licencia OEM, 64-bit', 180.00, 100, 30, 8),
('OFFICE-001', 'Microsoft Office 2021', 'Home & Student, 1 PC', 150.00, 80, 25, 8);

-- Movimientos iniciales
INSERT INTO movimientos (producto_id, tipo, cantidad, precio_unitario, motivo, fecha) VALUES
(1, 'ENTRADA', 15, 2500.00, 'Compra inicial proveedor', NOW()),
(1, 'SALIDA', 3, 2500.00, 'Venta a cliente corporativo', NOW()),
(2, 'ENTRADA', 10, 3500.00, 'Importación USA', NOW()),
(3, 'ENTRADA', 8, 4500.00, 'Pedido especial gamer', NOW()),
(4, 'ENTRADA', 25, 450.00, 'Compra mayorista', NOW()),
(4, 'SALIDA', 5, 450.00, 'Venta retail', NOW()),
(10, 'ENTRADA', 8, 2200.00, 'Compra electrodomésticos', NOW()),
(11, 'ENTRADA', 10, 1300.00, 'Pedido Lavadoras LG', NOW()),
(12, 'ENTRADA', 25, 300.00, 'Compra Microondas', NOW()),
(13, 'ENTRADA', 18, 3200.00, 'Importación celulares', NOW()),
(13, 'SALIDA', 6, 3200.00, 'Venta promocional', NOW());