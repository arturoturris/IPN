-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-11-2020 a las 22:07:12
-- Versión del servidor: 10.4.11-MariaDB
-- Versión de PHP: 7.4.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `ipn`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `Rfc` varchar(13) NOT NULL,
  `Persona_Id` int(11) NOT NULL,
  `Regimen` varchar(45) NOT NULL,
  `Email` varchar(100) DEFAULT NULL,
  `Telefono` varchar(45) DEFAULT NULL,
  `Celular` varchar(45) DEFAULT NULL,
  `ContrasegnaSAT` varchar(45) DEFAULT NULL,
  `FechaActualizacionDatos` date DEFAULT NULL,
  `Comentarios` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`Rfc`, `Persona_Id`, `Regimen`, `Email`, `Telefono`, `Celular`, `ContrasegnaSAT`, `FechaActualizacionDatos`, `Comentarios`) VALUES
('AEPA660113JT9', 16, 'INCORPORACION FISCAL', 'carmin_lau@hotmail.com', '2-85-41-22', '', '1234', '2020-06-12', ''),
('CARD830301J37', 17, 'ACTIVIDAD EMPRESARIAL', '', '', '', '1234', '2020-06-12', 'Preguntar por correo electronico'),
('CUMJ610423A7A', 18, 'INCORPORACION FISCAL', 'zapateriadelleon@hotmail.com ', '22-23-65-27-08 ', '8-90-96-38', '1234', '2020-06-12', ''),
('HEGD59047722', 19, 'INCORPORACION FISCAL', '', '', '', '1234', '2020-06-12', 'Preguntar por telefonos'),
('TELL971027D36', 20, 'INCORPORACION FISCAL', '', '', '', '', '2020-06-13', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `concepto`
--

CREATE TABLE `concepto` (
  `IdConcepto` int(11) NOT NULL,
  `Recibo_IdRecibo` int(11) NOT NULL,
  `ProductoServicio_IdProdServ` int(11) NOT NULL,
  `Cantidad` int(11) NOT NULL,
  `PrecioUnitario` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `concepto`
--

INSERT INTO `concepto` (`IdConcepto`, `Recibo_IdRecibo`, `ProductoServicio_IdProdServ`, `Cantidad`, `PrecioUnitario`) VALUES
(1, 2, 1, 1, 300);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `documento`
--

CREATE TABLE `documento` (
  `idDocumento` int(11) NOT NULL,
  `Cliente_Rfc` varchar(13) NOT NULL,
  `TipoDocumento_idDocumento` int(11) NOT NULL,
  `FechaExpedicion` date NOT NULL,
  `FechaExpiracion` date NOT NULL,
  `Archivo` varchar(300) DEFAULT NULL,
  `Contrasegna` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleado`
--

CREATE TABLE `empleado` (
  `IdEmpleado` int(11) NOT NULL,
  `Persona_Id` int(11) NOT NULL,
  `Puesto` varchar(45) NOT NULL,
  `Horario` varchar(45) DEFAULT NULL,
  `Salario` decimal(6,2) DEFAULT NULL,
  `FechaAlta` date NOT NULL,
  `Estado` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `empleado`
--

INSERT INTO `empleado` (`IdEmpleado`, `Persona_Id`, `Puesto`, `Horario`, `Salario`, `FechaAlta`, `Estado`) VALUES
(1, 1, 'Administrador', '13:00-20:00', '6.50', '2020-05-22', 'Activo'),
(2, 4, 'Jefa', '19:00', '50.28', '2020-05-24', 'Activo'),
(7, 13, 'Ayudante general', '09:00-12:00', '20.00', '2020-03-01', 'Activo'),
(9, 15, 'Atención a clientes', '12:00-22:00', '30.00', '2017-01-02', 'Activo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `negocio`
--

CREATE TABLE `negocio` (
  `IdNegocio` int(11) NOT NULL,
  `Cliente_Rfc` varchar(13) NOT NULL,
  `Nombre` varchar(70) NOT NULL,
  `GiroComercial` varchar(100) NOT NULL,
  `Calle` varchar(45) NOT NULL,
  `Numero` int(11) NOT NULL,
  `Interior` varchar(10) DEFAULT NULL,
  `Colonia` varchar(45) NOT NULL,
  `Municipio` varchar(45) NOT NULL,
  `Estado` varchar(45) NOT NULL,
  `CodigoPostal` int(5) NOT NULL,
  `Superficie` double DEFAULT NULL,
  `Horario` varchar(45) DEFAULT NULL,
  `Coordenadas` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `negocio`
--

INSERT INTO `negocio` (`IdNegocio`, `Cliente_Rfc`, `Nombre`, `GiroComercial`, `Calle`, `Numero`, `Interior`, `Colonia`, `Municipio`, `Estado`, `CodigoPostal`, `Superficie`, `Horario`, `Coordenadas`) VALUES
(1, 'CARD830301J37', 'ING', 'INGENIERIA', 'RIO BRAVO', 5142, '', 'JARDINES DE SAN MANUEL', 'PUEBLA', 'PUEBLA', 72510, 28, '12:00-19:00', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pago`
--

CREATE TABLE `pago` (
  `IdPago` int(11) NOT NULL,
  `Empleado_IdEmpleado` int(11) NOT NULL,
  `Recibo_IdRecibo` int(11) NOT NULL,
  `MontoPagado` float NOT NULL,
  `MetodoPago` varchar(45) NOT NULL,
  `FechaPago` date NOT NULL,
  `Comentario` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pendiente`
--

CREATE TABLE `pendiente` (
  `IdPendiente` int(11) NOT NULL,
  `Cliente_Rfc` varchar(13) NOT NULL,
  `FechaCreacion` date NOT NULL,
  `Descripcion` varchar(300) NOT NULL,
  `Importancia` varchar(45) NOT NULL,
  `FechaCita` varchar(45) DEFAULT NULL,
  `Empleado_IdEmpleado` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `persona`
--

CREATE TABLE `persona` (
  `Id` int(11) NOT NULL,
  `Sexo` char(1) NOT NULL,
  `Nombre` varchar(40) NOT NULL,
  `Paterno` varchar(45) NOT NULL,
  `Materno` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `persona`
--

INSERT INTO `persona` (`Id`, `Sexo`, `Nombre`, `Paterno`, `Materno`) VALUES
(1, 'H', 'Luis Arturo', 'Tenorio', 'López'),
(4, 'M', 'Luz del Carmen', 'Lopez', 'Gonzalez'),
(9, 'M', 'Perla', 'Hernandez', 'Juarez'),
(10, 'M', 'Perla', 'Hernandez', 'Juarez'),
(12, 'H', 'Luis Arturo', 'Tenorio', 'Lopez'),
(13, 'M', 'Perla', 'Hernández', 'Juárez'),
(15, 'H', 'José Arturo', 'Tenorio', 'Mendoza'),
(16, 'H', 'ALEJANDRO', 'ARCE', 'PALALIA'),
(17, 'H', 'DAVID RICARDO', 'CASCO', 'ROLDAN'),
(18, 'H', 'JORGE', 'CRUZ', 'MUÑOZ'),
(19, 'M', 'DORA REYNA', 'HERNANDEZ', 'GALLEGOS'),
(20, 'H', 'LUIS ARTURO', 'TENORIO', 'LOPEZ'),
(21, 'M', 'NICOLAS', 'TENORIO', 'HERNANDEZ');

--
-- Disparadores `persona`
--
DELIMITER $$
CREATE TRIGGER `persona_ucase` BEFORE INSERT ON `persona` FOR EACH ROW BEGIN
SET NEW.Nombre=UCASE(NEW.Nombre);
SET NEW.Paterno=UCASE(NEW.Paterno);
SET NEW.Materno=UCASE(NEW.Materno);
SET NEW.Sexo=UCASE(NEW.Sexo);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productoservicio`
--

CREATE TABLE `productoservicio` (
  `IdProdServ` int(11) NOT NULL,
  `Tipo` varchar(45) NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Descripcion` varchar(300) NOT NULL,
  `Precio` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `productoservicio`
--

INSERT INTO `productoservicio` (`IdProdServ`, `Tipo`, `Nombre`, `Descripcion`, `Precio`) VALUES
(1, 'Servicio', 'CONTADURIA MENSUAL', 'DECLARACIONES', 300),
(2, 'Servicio', 'TRAMITE DE E-FIRMA', 'TRAMITE PARA FIRMA ELECTRONICA', 200),
(3, 'Producto', 'TPV CLIP', 'TERMINAL CLIP PARA ACEPTAR PAGOS CON TARJETA', 350),
(4, 'Producto', 'NOTAS DE VENTA', 'PAQUETE DE 100 NOTAS DE VENTA FOLIADAS', 350),
(5, 'Servicio', 'CONSTANCIA MEDIDAS PREVENTIVAS INCENDIOS', 'TRAMITE PARA CONSTANCIA EN MEDIDAS PREVENTIVAS CONTRA INCENDIOS (BOMBEROS)', 550);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `recibo`
--

CREATE TABLE `recibo` (
  `IdRecibo` int(11) NOT NULL,
  `Cliente_Rfc` varchar(13) NOT NULL,
  `FechaDeEmision` date NOT NULL,
  `Pagado` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `recibo`
--

INSERT INTO `recibo` (`IdRecibo`, `Cliente_Rfc`, `FechaDeEmision`, `Pagado`) VALUES
(2, 'TELL971027D36', '2020-06-13', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tipodocumento`
--

CREATE TABLE `tipodocumento` (
  `IdTipoDocumento` int(11) NOT NULL,
  `Nombre` varchar(45) NOT NULL,
  `Descripcion` varchar(45) DEFAULT NULL,
  `Requisitos` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `tipodocumento`
--

INSERT INTO `tipodocumento` (`IdTipoDocumento`, `Nombre`, `Descripcion`, `Requisitos`) VALUES
(1, 'LICENCIA DE FUNCIONAMIENTO', 'LICENCIA DE FUNCIONAMIENTO DEL NEGOCIO', NULL),
(2, 'LICENCIA DE USO DE SUELO', 'LICENCIA DE USO DE SUELO DEL NEGOCIO', NULL),
(3, 'E-FIRMA (.key)', 'LLAVE PRIVADA DE LA FIRMA ELECTRONICA', NULL),
(4, 'E-FIRMA (.cer)', 'CERTIFICADO DIGITAL DE LA FIRMA ELECTRONICA', NULL),
(5, 'FACTURA', 'FACTURA', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `IdUsuario` int(11) NOT NULL,
  `Usuario` varchar(45) NOT NULL,
  `Contrasegna` varchar(64) NOT NULL,
  `IdEmpleado` int(11) NOT NULL,
  `UltimaEntrada` date NOT NULL,
  `Tipo` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`IdUsuario`, `Usuario`, `Contrasegna`, `IdEmpleado`, `UltimaEntrada`, `Tipo`) VALUES
(1, 'AdminArturo', '1234', 1, '2020-05-22', 'Administrador'),
(2, 'EmpleadoArturo', '1234', 1, '1969-12-31', 'Empleado'),
(4, 'carmina', '1234', 2, '1969-12-31', 'Empleado'),
(5, 'perla', '1234', 7, '1969-12-31', 'Empleado');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`Rfc`),
  ADD KEY `fk_Cliente_Persona` (`Persona_Id`);

--
-- Indices de la tabla `concepto`
--
ALTER TABLE `concepto`
  ADD KEY `fk_Concepto_ProductoServicio1` (`ProductoServicio_IdProdServ`),
  ADD KEY `fk_Concepto_Recibo1` (`Recibo_IdRecibo`);

--
-- Indices de la tabla `documento`
--
ALTER TABLE `documento`
  ADD PRIMARY KEY (`idDocumento`),
  ADD KEY `fk_Cliente_Rfc` (`Cliente_Rfc`),
  ADD KEY `fk_Documento_TipoDocumento1` (`TipoDocumento_idDocumento`);

--
-- Indices de la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD PRIMARY KEY (`IdEmpleado`),
  ADD KEY `fk_Empleado_Persona1` (`Persona_Id`);

--
-- Indices de la tabla `negocio`
--
ALTER TABLE `negocio`
  ADD PRIMARY KEY (`IdNegocio`),
  ADD KEY `fk_Negocio_Cliente1` (`Cliente_Rfc`);

--
-- Indices de la tabla `pago`
--
ALTER TABLE `pago`
  ADD PRIMARY KEY (`IdPago`),
  ADD KEY `FK_Recibo_Pago` (`Recibo_IdRecibo`),
  ADD KEY `FK_Empleado_Pago` (`Empleado_IdEmpleado`);

--
-- Indices de la tabla `pendiente`
--
ALTER TABLE `pendiente`
  ADD PRIMARY KEY (`IdPendiente`),
  ADD KEY `fk_Pendiente_Cliente1` (`Cliente_Rfc`),
  ADD KEY `fk_Pendiente_Empleado1` (`Empleado_IdEmpleado`);

--
-- Indices de la tabla `persona`
--
ALTER TABLE `persona`
  ADD PRIMARY KEY (`Id`);

--
-- Indices de la tabla `productoservicio`
--
ALTER TABLE `productoservicio`
  ADD PRIMARY KEY (`IdProdServ`);

--
-- Indices de la tabla `recibo`
--
ALTER TABLE `recibo`
  ADD PRIMARY KEY (`IdRecibo`),
  ADD KEY `fk_Recibo_Cliente1` (`Cliente_Rfc`);

--
-- Indices de la tabla `tipodocumento`
--
ALTER TABLE `tipodocumento`
  ADD PRIMARY KEY (`IdTipoDocumento`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`IdUsuario`),
  ADD UNIQUE KEY `Usuario` (`Usuario`),
  ADD KEY `fk_Usuario_Empleado1` (`IdEmpleado`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `documento`
--
ALTER TABLE `documento`
  MODIFY `idDocumento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `empleado`
--
ALTER TABLE `empleado`
  MODIFY `IdEmpleado` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `negocio`
--
ALTER TABLE `negocio`
  MODIFY `IdNegocio` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `pago`
--
ALTER TABLE `pago`
  MODIFY `IdPago` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `pendiente`
--
ALTER TABLE `pendiente`
  MODIFY `IdPendiente` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `persona`
--
ALTER TABLE `persona`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT de la tabla `productoservicio`
--
ALTER TABLE `productoservicio`
  MODIFY `IdProdServ` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `recibo`
--
ALTER TABLE `recibo`
  MODIFY `IdRecibo` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `tipodocumento`
--
ALTER TABLE `tipodocumento`
  MODIFY `IdTipoDocumento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `IdUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD CONSTRAINT `fk_Cliente_Persona` FOREIGN KEY (`Persona_Id`) REFERENCES `persona` (`Id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Filtros para la tabla `concepto`
--
ALTER TABLE `concepto`
  ADD CONSTRAINT `fk_Concepto_ProductoServicio1` FOREIGN KEY (`ProductoServicio_IdProdServ`) REFERENCES `productoservicio` (`IdProdServ`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Concepto_Recibo1` FOREIGN KEY (`Recibo_IdRecibo`) REFERENCES `recibo` (`IdRecibo`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Filtros para la tabla `documento`
--
ALTER TABLE `documento`
  ADD CONSTRAINT `fk_Cliente_Rfc` FOREIGN KEY (`Cliente_Rfc`) REFERENCES `cliente` (`Rfc`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Filtros para la tabla `empleado`
--
ALTER TABLE `empleado`
  ADD CONSTRAINT `fk_Empleado_Persona1` FOREIGN KEY (`Persona_Id`) REFERENCES `persona` (`Id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Filtros para la tabla `negocio`
--
ALTER TABLE `negocio`
  ADD CONSTRAINT `fk_Negocio_Cliente1` FOREIGN KEY (`Cliente_Rfc`) REFERENCES `cliente` (`Rfc`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `pago`
--
ALTER TABLE `pago`
  ADD CONSTRAINT `FK_Empleado_Pago` FOREIGN KEY (`Empleado_IdEmpleado`) REFERENCES `empleado` (`IdEmpleado`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_Recibo_Pago` FOREIGN KEY (`Recibo_IdRecibo`) REFERENCES `recibo` (`IdRecibo`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Filtros para la tabla `pendiente`
--
ALTER TABLE `pendiente`
  ADD CONSTRAINT `fk_Pendiente_Cliente1` FOREIGN KEY (`Cliente_Rfc`) REFERENCES `cliente` (`Rfc`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_Pendiente_Empleado1` FOREIGN KEY (`Empleado_IdEmpleado`) REFERENCES `empleado` (`IdEmpleado`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `recibo`
--
ALTER TABLE `recibo`
  ADD CONSTRAINT `fk_Recibo_Cliente1` FOREIGN KEY (`Cliente_Rfc`) REFERENCES `cliente` (`Rfc`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `fk_Usuario_Empleado1` FOREIGN KEY (`IdEmpleado`) REFERENCES `empleado` (`IdEmpleado`) ON DELETE CASCADE ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
