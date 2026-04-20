**URBANFIT - README DEL PROYECTO**

**1\. TÍTULO DEL PROYECTO**

**UrbanFit - Aplicación Web para la Gestión de Clases de Gimnasio**

**2\. DESCRIPCIÓN DEL PROYECTO**

UrbanFit es una aplicación web completa para la gestión y reserva de clases en un gimnasio. Desarrollada como Proyecto Final de Grado del ciclo formativo de Desarrollo de Aplicaciones Web (DAW), permite a los clientes reservar clases, a los monitores controlar la asistencia y a los administradores gestionar toda la plataforma de manera eficiente.

**3\. ROLES Y FUNCIONALIDADES**

**3.1. Cliente (usuario registrado)**

- Iniciar y cerrar sesión.
- Consultar clases disponibles y horarios.
- Reservar y cancelar clases (antes de que comiencen).
- Ver historial de reservas (pasadas y futuras).
- Editar su perfil personal.

**3.2. Monitor**

- Ver las clases que imparte.
- Consultar la lista de alumnos inscritos en cada clase.
- Marcar asistencia de los usuarios.

**3.3. Administrador**

- Gestionar usuarios (CRUD, cambio de roles, activar/desactivar).
- Gestionar tipos de clases y clases (CRUD).
- Asignar monitores y horarios a cada clase.
- Activar cuentas de clientes tras pago presencial.

**3.4. Visitante (no registrado)**

- Ver información del gimnasio y actividades.
- Registrarse como nuevo cliente.

**4\. STACK TECNOLÓGICO**

| Capa          | Tecnologías                                                       |
| ------------- | ----------------------------------------------------------------- |
| Backend       | Java 21, Spring Boot, Spring Security, Spring Data JPA, Hibernate |
| Frontend      | Thymeleaf, Bootstrap 5, CSS propio, HTML5                         |
| Base de Datos | MySQL (usando MariaDB con XAMPP)                                  |
| Herramientas  | Maven, Spring Tool Suite 4 (STS), Git, GitHub                     |
| Despliegue    | Tomcat embebido (Spring Boot)                                     |

**5\. ARQUITECTURA**

El proyecto sigue el patrón MVC (Modelo-Vista-Controlador) con una clara separación de capas:

- **Entity**: Clases que mapean las tablas de la base de datos.
- **Repository**: Interfaces que extienden JpaRepository.
- **Service**: Lógica de negocio (validaciones, reglas de reserva, scheduler de pagos).
- **Controller**: Gestión de rutas HTTP y comunicación con Thymeleaf.
- **Configuration**: Configuración de Spring Security y sesiones.

**Seguridad implementada:**

- Autenticación mediante UserDetailsService personalizado (login por email).
- Contraseñas encriptadas con BCrypt.
- Protección de rutas por roles (hasRole("ADMIN"), hasRole("MONITOR"), etc.).
- Control de suscripciones: el cliente se registra como INACTIVO y el administrador lo activa tras el pago. Un scheduler diario desactiva automáticamente las cuentas con pago vencido (más de 1 mes).

**6\. ESTRUCTURA DE LA BASE DE DATOS**

**Tablas principales:**

- **roles**: Almacena los roles: Administrador, Monitor, Cliente.
- **usuarios**: Datos personales, email, contraseña, rol, estado (activo/inactivo), fecha_pago.
- **tipos_clases**: Categorías de actividades (CrossTraining, Spinning, Pilates, etc.).
- **clases**: Nombre, capacidad, monitor asignado, tipo de clase.
- **horarios**: Día de la semana, hora de inicio y fin (relacionado con una clase).
- **reservas**: Relaciona usuario + horario, estado (activa/cancelada) y asistencia confirmada.

_Nota: Incluir aquí el diagrama de la base de datos si se dispone de él._

**7\. INSTALACIÓN Y CONFIGURACIÓN**

**7.1. Requisitos previos**

- Java 21 JDK
- Maven
- XAMPP (o MySQL Workbench)
- Git

**7.2. Pasos para ejecutar el proyecto**

- Clonar el repositorio:  
   git clone <https://github.com/djpino82/urbanfit.git>  
   cd urbanfit
- Configurar la base de datos:
  - Iniciar Apache y MySQL desde XAMPP.
  - Acceder a phpMyAdmin y crear una base de datos llamada urbanfit.
  - Ejecutar el script schema.sql (si existe) o permitir que Hibernate cree las tablas automáticamente.
- Configurar el archivo application.properties:  
   spring.datasource.url=jdbc:mariadb://localhost:3306/urbanfit  
   spring.datasource.username=root  
   spring.datasource.password=  
   spring.datasource.driver-class-name=org.mariadb.jdbc.Driver  
   spring.jpa.hibernate.ddl-auto=update  
   spring.jpa.show-sql=true  
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
- Ejecutar la aplicación:  
   mvn spring-boot:run
- Acceder a la web en: [http://localhost:8080](http://localhost:8080/)

**Nota:** El primer usuario administrador se puede insertar directamente en la tabla usuarios con rol Administrador y contraseña encriptada con BCrypt, o mediante el registro y luego cambiando su rol desde la base de datos.

**8\. MEJORAS FUTURAS**

- Integrar una pasarela de pago real (Stripe/PayPal) para activación automática de clientes.
- Implementar un sistema de notificaciones por email o dentro de la aplicación.
- Añadir exportación de informes a PDF o Excel.
- Incluir gráficas interactivas para las estadísticas.
- Refactorizar el frontend con React y crear una API REST independiente.
- Ampliar las pruebas automatizadas y añadir tests de integración.

**9\. CONCLUSIONES FINALES**

Este proyecto no solo representa el cierre de un trabajo académico, sino el final de una etapa importante y el inicio de una nueva trayectoria profesional. A lo largo del desarrollo se han aplicado los conocimientos adquiridos durante el ciclo de DAW, superando dificultades técnicas como problemas con la base de datos o la configuración de seguridad.

El resultado es una aplicación funcional, segura y escalable, que demuestra la capacidad de reinventarse y aprender desde cero en un ámbito completamente diferente al anterior desempeño profesional del autor.

**10\. AUTOR**

**Daniel Jaime Pino**  
GitHub: @djpino82  
Proyecto realizado para el Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Web (DAW)  
Curso 2024/2026

**11\. LICENCIA**

Este proyecto es de uso académico. Puede usarse como referencia para el aprendizaje, pero no está destinado a producción sin las debidas mejoras de seguridad y escalabilidad.















   
