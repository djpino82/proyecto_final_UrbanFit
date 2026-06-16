# 🚀 UrbanFit – Sistema de Gestión de Gimnasio

Aplicación web full stack para la gestión de reservas de clases en un gimnasio.

El sistema permite a los clientes reservar y cancelar clases, a los monitores gestionar la asistencia y a los administradores controlar usuarios, clases y horarios.

  
# 🎥 Demo del proyecto

Solicitar por privado
  

# 🧰 Tecnologías utilizadas

-   Java 21
-   Spring Boot
-   Spring Security
-   Spring Data JPA / Hibernate
-   MySQL (MariaDB)
-   Thymeleaf
-   Bootstrap 5
-   HTML5, CSS3
-   Maven
-   Git / GitHub

  
#  Funcionalidades principales

## Gestión de usuarios y roles

-   Sistema de autenticación con Spring Security
-   Roles diferenciados: Administrador, Monitor, Cliente
-   Registro de usuarios con activación posterior por parte del administrador

## Seguridad

-   Autenticación con UserDetailsService personalizado
-   Encriptación de contraseñas con BCrypt
-   Control de acceso por roles en rutas y funcionalidades

## Gestión del gimnasio

-   Reserva y cancelación de clases por parte de los usuarios
-   Control de aforo en tiempo real
-   Gestión de clases, horarios y tipos de actividad
-   Asignación de monitores a clases

## Funcionalidades avanzadas

-   Historial de reservas por usuario
-   Sistema de activación/desactivación de cuentas por estado de pago
-   Tarea programada (scheduler) para desactivar cuentas vencidas automáticamente
  

# Arquitectura del proyecto

El proyecto sigue una arquitectura MVC bien estructurada:

-   ****Controladores****: gestión de rutas y peticiones HTTP
-   ****Servicios****: lógica de negocio y reglas del sistema
-   ****Repositorios****: acceso a base de datos con Spring Data JPA
-   ****Entidades****: modelado de la base de datos
-   ****Configuración de seguridad****: Spring Security y control de sesiones

  
# Base de datos

Principales entidades del sistema:

-   usuarios (datos personales, rol, estado, fecha de pago)
-   roles (ADMIN, MONITOR, CLIENTE)
-   clases (actividades del gimnasio)
-   horarios (días y franjas horarias)
-   reservas (relación usuario-clase)
-   tipos\_clases (categorías deportivas)

  
# ⚙️ Instalación y ejecución

## Requisitos

-   Java 21
-   Maven
-   MySQL / MariaDB
-   Git

## Pasos

Clonar el repositorio:

git clone [https://github.com/djpino82/urbanfit.git](https://github.com/djpino82/urbanfit.git)  
cd urbanfit

 
Crear base de datos:

-   Crear base de datos llamada: urbanfit
-   Configurar credenciales en application.properties

  
Ejemplo de configuración:

spring.datasource.url=jdbc:mariadb://localhost:3306/urbanfit  
spring.datasource.username=root  
spring.datasource.password=  
spring.jpa.hibernate.ddl-auto=update
  

Ejecutar la aplicación:

mvn spring-boot:run

Acceder en el navegador:  
[http://localhost:8080](http://localhost:8080)

  
# Mejoras futuras

-   Integración con pasarela de pago (Stripe / PayPal)
-   Notificaciones por email o dentro de la app
-   Panel de estadísticas y dashboard administrativo
-   Exportación de datos a PDF/Excel
-   Separación backend API REST + frontend React
-   Tests unitarios e integración
  

# Autor

Daniel Jaime Pino  
GitHub: @djpino82  
Proyecto académico del ciclo DAW (2024–2026)

  
# 📄 Licencia

Proyecto con fines educativos. Puede usarse como referencia de aprendizaje, pero no está orientado a producción sin mejoras adicionales de seguridad y escalabilidad.
