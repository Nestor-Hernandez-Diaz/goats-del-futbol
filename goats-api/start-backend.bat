@echo off
echo ================================================
echo   INICIANDO BACKEND - GOATS DEL FUTBOL
echo ================================================
echo.
set JAVA_HOME=C:\Program Files\Java\jdk-17
echo JAVA_HOME configurado: %JAVA_HOME%
echo.
echo Iniciando Spring Boot en puerto 8080...
echo NO CIERRES ESTA VENTANA mientras uses la aplicacion
echo.
mvnw.cmd spring-boot:run
pause
