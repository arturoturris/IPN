# AP-IPN
Proyecto final Administración Proyectos - IPN

## Requerimientos
- JDK 14
- Apache Netbeans 11.3 (u otro IDE)
- XAMPP

## Instalación de BD (XAMPP)

1. Descargar el archivo "ipn.sql"
2. Iniciar el servidor Apache y MySql desde XAMPP Control Panel
3. Ingresar a localhost/phpmyadmin
4. Pulsar el botón "Nueva" para crear una base de datos
5. Nombrar a la base de datos "ipn" y seleccionar el formato "utf8mb4_general_ci"
6. Pulsar el botón de crear
7. Dar clic a la base de datos recien creada
8. Pulsar la opción "Importar"
9. Pulsar examinar y seleccionar el archivo "ipn.sql"
10. Seleccionar el conjunto de caracteres utf8
11. Pulsar botón de continuar

## Configuración de usuario y contraseña

En caso de contar con un nombre de usuario y contraseña para el servidor MySql:

1. Abrir el proyecto en Netbeans
2. Localizar el paquete "controller" y abrirlo
3. Localizar el archivo "Launcher.java"
4. Dirigirse a la linea ~48 y poner el usuario y contraseña de su usuario en MySql
5. Dirigirse a la linea ~77 y poner el usuario y contraseña de su usuario en MySql
6. Limpiar y reconstruir el proyecto
