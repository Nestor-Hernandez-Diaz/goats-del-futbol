# Guía de Configuración XAMPP + MySQL para GOATs API

## Estado Actual del Proyecto

### Backend (Spring Boot 3.5.7)
- Java 17
- Puerto: 8080
- Base de datos: MySQL vía XAMPP
- ORM: Spring Data JPA + Hibernate
- Documentación: Swagger UI

### Configuración Actual
- Database: `goats_futbol`
- Usuario: `root` (por defecto)
- Password: vacía (por defecto XAMPP)
- Puerto MySQL: 3306

---

## Paso 1: Verificar Instalación de XAMPP

### 1.1 Iniciar XAMPP Control Panel
1. Abrir XAMPP Control Panel como **Administrador**
2. Iniciar módulos:
   - ✅ **Apache** (para phpMyAdmin)
   - ✅ **MySQL** (base de datos)

### 1.2 Verificar que MySQL está corriendo
```powershell
# Verificar si MySQL está escuchando en puerto 3306
netstat -an | Select-String "3306"
```

**Resultado esperado:**
```
TCP    0.0.0.0:3306           0.0.0.0:0              LISTENING
TCP    [::]:3306              [::]:0                 LISTENING
```

---

## Paso 2: Configurar Base de Datos

### 2.1 Acceder a phpMyAdmin
1. Abrir navegador: `http://localhost/phpmyadmin`
2. Usuario: `root`
3. Contraseña: *dejar vacío*

### 2.2 Crear Base de Datos
Ejecutar en pestaña SQL:

```sql
-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS goats_futbol 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE goats_futbol;
```

### 2.3 Crear Usuario Dedicado (Opcional pero Recomendado)
```sql
-- Crear usuario dedicado
CREATE USER IF NOT EXISTS 'goats_user'@'localhost' 
IDENTIFIED BY 'goats_password_2025';

-- Otorgar privilegios
GRANT ALL PRIVILEGES ON goats_futbol.* TO 'goats_user'@'localhost';

-- Aplicar cambios
FLUSH PRIVILEGES;
```

### 2.4 Verificar Usuario
```sql
-- Ver usuarios existentes
SELECT User, Host, plugin FROM mysql.user WHERE User IN ('root', 'goats_user');

-- Ver privilegios del usuario
SHOW GRANTS FOR 'goats_user'@'localhost';
```

---

## Paso 3: Configurar Spring Boot

### 3.1 Opción A: Usar Usuario Root (Desarrollo)

**application.properties** (ya configurado):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/goats_futbol?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
```

### 3.2 Opción B: Usar Usuario Dedicado (Recomendado)

**application.properties**:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/goats_futbol?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=goats_user
spring.datasource.password=goats_password_2025
```

### 3.3 Opción C: Variables de Entorno (Producción)

**PowerShell**:
```powershell
# Establecer variables de entorno para la sesión actual
$env:DB_URL = "jdbc:mysql://localhost:3306/goats_futbol?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USER = "goats_user"
$env:DB_PASS = "goats_password_2025"
```

---

## Paso 4: Verificar Conexión desde PowerShell

### 4.1 Probar Conexión MySQL
```powershell
# Navegar a bin de MySQL en XAMPP
cd C:\xampp\mysql\bin

# Conectar a MySQL
.\mysql.exe -u root -p
# (presionar Enter si no hay contraseña)
```

**Comandos dentro de MySQL:**
```sql
-- Ver bases de datos
SHOW DATABASES;

-- Usar goats_futbol
USE goats_futbol;

-- Ver tablas (después de iniciar Spring Boot)
SHOW TABLES;

-- Ver estructura de tabla players
DESCRIBE players;

-- Ver datos
SELECT * FROM players;

-- Salir
EXIT;
```

---

## Paso 5: Iniciar Aplicación Spring Boot

### 5.1 Compilar el Proyecto
```powershell
# Navegar a la carpeta del proyecto
cd c:\xampp\htdocs\proyecto-goats-futbol\goats-api

# Compilar con Maven
.\mvnw.cmd clean package -DskipTests
```

### 5.2 Ejecutar la Aplicación

**Opción A: Con Maven Wrapper**
```powershell
.\mvnw.cmd spring-boot:run
```

**Opción B: Con JAR generado**
```powershell
java -jar target\goats-api-0.0.1-SNAPSHOT.jar
```

**Opción C: Con Variables de Entorno**
```powershell
$env:DB_USER = "goats_user"
$env:DB_PASS = "goats_password_2025"
java -jar target\goats-api-0.0.1-SNAPSHOT.jar
```

### 5.3 Verificar Inicio Exitoso

**Mensajes esperados en consola:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.5.7)

[INFO] Started GoatsApiApplication in X.XXX seconds
[INFO] Tomcat started on port 8080
```

---

## Paso 6: Probar los Endpoints

### 6.1 Swagger UI
Abrir en navegador: `http://localhost:8080/swagger-ui/index.html`

### 6.2 Probar API con PowerShell

**Listar todos los jugadores:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/players" -Method Get | ConvertTo-Json -Depth 3
```

**Obtener jugador por ID:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/players/1" -Method Get | ConvertTo-Json -Depth 3
```

**Buscar jugadores:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/players?name=Messi" -Method Get | ConvertTo-Json -Depth 3
```

---

## Paso 7: Solución de Problemas Comunes

### ❌ Error: "Communications link failure"
**Causa:** MySQL no está corriendo.

**Solución:**
1. Abrir XAMPP Control Panel
2. Hacer clic en "Start" en MySQL
3. Verificar con: `netstat -an | Select-String "3306"`

---

### ❌ Error: "Access denied for user 'root'@'localhost'"
**Causa:** Contraseña incorrecta.

**Solución A: Resetear contraseña de root**
```powershell
# Detener MySQL desde XAMPP
# Abrir phpMyAdmin y ejecutar:
ALTER USER 'root'@'localhost' IDENTIFIED BY '';
FLUSH PRIVILEGES;
```

**Solución B: Verificar configuración**
```properties
# application.properties
spring.datasource.username=root
spring.datasource.password=
```

---

### ❌ Error: "Unknown database 'goats_futbol'"
**Causa:** Base de datos no creada.

**Solución:**
```sql
-- En phpMyAdmin o MySQL CLI
CREATE DATABASE IF NOT EXISTS goats_futbol 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

---

### ❌ Error: Puerto 8080 en uso
**Causa:** Otro proceso usando el puerto.

**Solución A: Cambiar puerto**
```properties
# application.properties
server.port=8081
```

**Solución B: Liberar puerto 8080**
```powershell
# Ver qué proceso usa el puerto
netstat -ano | Select-String "8080"

# Matar proceso (reemplazar PID)
Stop-Process -Id <PID> -Force
```

---

### ❌ Error: "Public Key Retrieval is not allowed"
**Causa:** Falta parámetro en URL de conexión.

**Solución:** Ya está configurado en tu `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/goats_futbol?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
```

---

### ❌ Errores de Flyway
**Causa:** Flyway habilitado sin migraciones.

**Solución:** Ya está deshabilitado en tu configuración:
```properties
spring.flyway.enabled=false
```

---

## Paso 8: Verificación Final

### Checklist de Configuración Exitosa

- [ ] XAMPP Control Panel muestra Apache y MySQL en verde
- [ ] phpMyAdmin accesible en `http://localhost/phpmyadmin`
- [ ] Base de datos `goats_futbol` creada
- [ ] Usuario MySQL configurado (root o goats_user)
- [ ] Spring Boot inicia sin errores
- [ ] Swagger UI accesible en `http://localhost:8080/swagger-ui/index.html`
- [ ] Endpoint `GET /api/players` devuelve datos
- [ ] Tablas creadas automáticamente por Hibernate
- [ ] Datos de `data.sql` insertados correctamente

### Comandos de Verificación Rápida

```powershell
# 1. Verificar MySQL corriendo
netstat -an | Select-String "3306"

# 2. Verificar puerto 8080 libre (antes de iniciar)
netstat -an | Select-String "8080"

# 3. Iniciar aplicación
cd c:\xampp\htdocs\proyecto-goats-futbol\goats-api
.\mvnw.cmd spring-boot:run

# 4. En otra terminal, probar API
Invoke-RestMethod -Uri "http://localhost:8080/api/players" -Method Get
```

---

## Paso 9: Configuración Avanzada (Opcional)

### 9.1 Habilitar Logs de SQL
Ya está configurado en `application.properties`:
```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### 9.2 Configurar Pool de Conexiones
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
```

### 9.3 Configurar CORS para Frontend
```properties
# Ya configurado en SecurityConfig.java
# Permite peticiones desde http://localhost y http://127.0.0.1
```

---

## Estructura de Base de Datos Generada

### Tabla: players
```sql
CREATE TABLE players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    nickname VARCHAR(100),
    country VARCHAR(100),
    position VARCHAR(50),
    biography TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

### Datos Iniciales (data.sql)
- Lionel Messi
- Cristiano Ronaldo
- Neymar Jr

---

## Comandos Útiles de MySQL

### Ver estado de tablas
```sql
USE goats_futbol;
SHOW TABLES;
SHOW TABLE STATUS;
```

### Ver datos
```sql
SELECT * FROM players;
SELECT COUNT(*) FROM players;
```

### Limpiar datos (reinicio)
```sql
TRUNCATE TABLE players;
-- Reiniciar auto_increment
ALTER TABLE players AUTO_INCREMENT = 1;
```

### Backup de base de datos
```powershell
# Desde PowerShell
cd C:\xampp\mysql\bin
.\mysqldump.exe -u root goats_futbol > C:\xampp\htdocs\proyecto-goats-futbol\backup_$(Get-Date -Format 'yyyyMMdd_HHmmss').sql
```

### Restaurar backup
```powershell
.\mysql.exe -u root goats_futbol < C:\xampp\htdocs\proyecto-goats-futbol\backup_20251115_120000.sql
```

---

## Próximos Pasos

1. ✅ XAMPP + MySQL configurado
2. ✅ Spring Boot conectado a MySQL
3. ✅ CRUD básico de jugadores funcionando
4. ⏳ Implementar autenticación JWT
5. ⏳ Agregar modelos: Comments, Subscriptions
6. ⏳ Integrar frontend con backend
7. ⏳ Tests unitarios e integración

---

## Referencias

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [XAMPP Documentation](https://www.apachefriends.org/docs/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- Plan Backend: `documentation/PLAN_BACKEND_MVC.md`
- Frontend: `documentation/DOCUMENTACION_IMPLEMENTACION_FRONTEND.md`

---

## Registro de Configuración

- Fecha: 2025-11-15
- XAMPP versión: (verificar en Control Panel)
- MySQL versión: (verificar con `SELECT VERSION();`)
- Java versión: 17
- Spring Boot versión: 3.5.7
- Estado: ⏳ En configuración
