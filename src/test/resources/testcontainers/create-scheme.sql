
CREATE TABLE clientes (
 id_cliente INT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL,
 telefono VARCHAR(20) NOT NULL,
 direccion VARCHAR(150) NOT NULL
);



CREATE TABLE proveedores (
  id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  direccion VARCHAR(150) NOT NULL
);



CREATE TABLE productos (
   id_producto INT AUTO_INCREMENT PRIMARY KEY,
   nombre VARCHAR(100) NOT NULL,
   precio DECIMAL(10,2) NOT NULL,
   stock INT NOT NULL,
   id_proveedor INT NOT NULL,
   FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
);



CREATE TABLE ventas (
  id_venta INT AUTO_INCREMENT PRIMARY KEY,
  fecha DATE NOT NULL,
  id_cliente INT NOT NULL,
  FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);



CREATE TABLE detalle_venta (
  id_detalle INT AUTO_INCREMENT PRIMARY KEY,
  id_venta INT NOT NULL,
  id_producto INT NOT NULL,
  cantidad INT NOT NULL,
  FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
  FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);



CREATE TABLE compras (
  id_compra INT AUTO_INCREMENT PRIMARY KEY,
  fecha DATE NOT NULL,
  id_proveedor INT NOT NULL,
  FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor)
);



CREATE TABLE detalle_compra (
   id_detalle INT AUTO_INCREMENT PRIMARY KEY,
   id_compra INT NOT NULL,
   id_producto INT NOT NULL,
   cantidad INT NOT NULL,
   FOREIGN KEY (id_compra) REFERENCES compras(id_compra),
   FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);




INSERT INTO clientes (nombre, telefono, direccion) VALUES
('Johan Steven Mendoza Ruiz', '3001111111', 'Medellin'),
('Ana Maria Ojeda Santos', '3002222222', 'Bello'),
('Alan David Valoyes Pertuz', '3003333333', 'Envigado'),
('Denier Caro Tobar', '3004444444', 'Itagui'),
('Luis Fernando Arevalo Cuesta', '3005555555', 'Sabaneta'),
('Sofia Lopez', '3006666666', 'La Estrella');


INSERT INTO proveedores (nombre, telefono, direccion) VALUES
('Proveedor A', '3101111111', 'Bogota'),
('Proveedor B', '3102222222', 'Cali'),
('Proveedor C', '3103333333', 'Barranquilla'),
('Proveedor D', '3104444444', 'Cartagena'),
('Proveedor E', '3105555555', 'Medellin'),
('Proveedor F', '3106666666', 'Pereira');


INSERT INTO productos (nombre, precio, stock, id_proveedor) VALUES
('Martillo', 25000, 50, 1),
('Taladro', 180000, 20, 2),
('Destornillador', 10000, 100, 3),
('Llave inglesa', 30000, 40, 4),
('Serrucho', 22000, 35, 5),
('Cinta métrica', 15000, 60, 6);


INSERT INTO ventas (fecha, id_cliente) VALUES
('2026-04-01', 1),
('2026-04-02', 2),
('2026-04-03', 3),
('2026-04-04', 4),
('2026-04-05', 5),
('2026-04-06', 6);


INSERT INTO detalle_venta (id_venta, id_producto, cantidad) VALUES
(1, 1, 2),
(2, 2, 1),
(3, 3, 5),
(4, 4, 2),
(5, 5, 1),
(6, 6, 3);


INSERT INTO compras (fecha, id_proveedor) VALUES
('2026-03-01', 1),
('2026-03-02', 2),
('2026-03-03', 3),
('2026-03-04', 4),
('2026-03-05', 5),
('2026-03-06', 6);


INSERT INTO detalle_compra (id_compra, id_producto, cantidad) VALUES
(1, 1, 20),
(2, 2, 10),
(3, 3, 50),
(4, 4, 15),
(5, 5, 12),
(6, 6, 25);