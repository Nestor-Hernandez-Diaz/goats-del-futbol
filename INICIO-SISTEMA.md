# GOATs del Fútbol - Sistema de Inicio

## 🚀 Inicio Rápido

### ⭐ MÉTODO RECOMENDADO: Panel de Control XAMPP + Script Backend

Este es el método más confiable:

#### Paso 1: Iniciar Apache y MySQL desde XAMPP

1. Abre el **Panel de Control de XAMPP** (xampp-control.exe)
2. Click en **Start** para Apache
3. Click en **Start** para MySQL

#### Paso 2: Iniciar el Backend

Ejecuta en PowerShell:

```powershell
cd c:\xampp\htdocs\proyecto-goats-futbol\goats-api
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
& "$env:JAVA_HOME\bin\java.exe" -jar target\goats-api-0.0.1-SNAPSHOT.jar
```

**Mantén esta ventana abierta** mientras trabajas. El backend mostrará los logs.

---

### Opción 2: Script Automático (Experimental)

Ejecuta este comando en PowerShell:

```powershell
cd c:\xampp\htdocs\proyecto-goats-futbol
.\start-system.ps1
```

Este script:
1. ✅ Verifica y repara MySQL si es necesario
2. ✅ Inicia MySQL automáticamente
3. ✅ Verifica Apache
4. ✅ Compila el backend si es necesario
5. ✅ Inicia el backend automáticamente
6. ✅ Monitorea todos los servicios

⚠️ **Nota**: Si MySQL falla, usa el Panel de Control de XAMPP (Opción 1).

### Opción 3: Detener Todos los Servicios

```powershell
cd c:\xampp\htdocs\proyecto-goats-futbol
.\stop-system.ps1
```

## 📋 URLs del Sistema

- **Frontend**: http://localhost/proyecto-goats-futbol
- **Backend API**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Dashboard Admin**: http://localhost/proyecto-goats-futbol/pages/admin.html
- **Login**: http://localhost/proyecto-goats-futbol/pages/login.html

## 🔑 Credenciales

### Usuario Admin
- **Username**: `admin`
- **Password**: `admin123`
- **Rol**: `ROLE_ADMIN`

## 🛠️ Solución de Problemas

### MySQL no inicia

El script automáticamente:
- Repara archivos Aria corruptos
- Repara tablas marcadas como "crashed"
- Reinicia MySQL hasta 30 intentos

Si persiste el problema:
1. Abre el Panel de Control de XAMPP
2. Detén MySQL manualmente
3. Vuelve a ejecutar `start-system.ps1`

### Backend no responde

El script espera hasta 60 segundos. Si no responde:
1. Verifica que MySQL esté corriendo
2. Revisa los logs en la terminal del backend
3. Verifica que Java 17 esté instalado en `C:\Program Files\Java\jdk-17`

### Apache no está corriendo

Inicia Apache manualmente desde el Panel de Control de XAMPP.

## 📁 Estructura del Proyecto

```
proyecto-goats-futbol/
├── start-system.ps1        # ⭐ Script principal de inicio
├── stop-system.ps1         # Script para detener servicios
├── goats-api/              # Backend Spring Boot
│   ├── src/
│   ├── target/
│   │   └── goats-api-0.0.1-SNAPSHOT.jar
│   └── mvnw.cmd
├── js/
│   ├── main.js
│   ├── admin.js
│   └── player-stats.js
├── css/
│   ├── styles.css
│   └── admin.css
├── pages/
│   ├── admin.html
│   ├── login.html
│   ├── messi.html
│   ├── ronaldo.html
│   └── neymar.html
└── index.html
```

## 🎯 Tareas Completadas

- ✅ **Tarea 1**: Dashboard Admin con moderación de comentarios
- ✅ **Tarea 2**: Estadísticas dinámicas desde backend

## 📝 Tareas Pendientes

- ⏳ **Tarea 3**: Sistema de comentarios en frontend
- ⏳ **Tarea 4**: Autenticación JWT en navegación
- ⏳ **Tarea 5**: Tests unitarios backend
- ⏳ **Tarea 6**: Resolver @PreAuthorize
- ⏳ **Tarea 7**: Sistema de suscripciones
- ⏳ **Tarea 8**: Sistema de notificaciones

## 🔄 Reiniciar Servicios

Si necesitas reiniciar algún servicio:

```powershell
# Detener todo
.\stop-system.ps1

# Esperar 5 segundos

# Iniciar todo
.\start-system.ps1
```

## 💡 Consejos

1. **Siempre usa `start-system.ps1`** para iniciar el sistema
2. **No cierres la ventana de PowerShell** mientras trabajas (monitorea los servicios)
3. **Presiona Ctrl+C** en la ventana de PowerShell para detener todo
4. **Si hay errores**, el script intentará repararlos automáticamente

## 📞 Soporte

Si encuentras problemas no resueltos por el script:
1. Revisa los logs en `C:\xampp\mysql\data\mysql_error.log`
2. Verifica que los puertos 3306 (MySQL) y 8080 (Backend) estén libres
3. Asegúrate de tener Java 17 instalado

---

**Última actualización**: 16 Noviembre 2025
