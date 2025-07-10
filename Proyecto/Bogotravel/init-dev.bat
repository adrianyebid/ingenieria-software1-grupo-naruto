@echo off
echo =====================================
echo Iniciando entorno de desarrollo
echo =====================================

:: 1. Verificar Java
echo Verificando Java...
java -version >nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
    echo Java no esta instalado o no esta en el PATH.
    goto fin
) ELSE (
    echo Java detectado.
)

:: 2. Verificar Maven
REM echo Verificando Maven...
REM mvn -v >nul 2>&1
REM IF %ERRORLEVEL% NEQ 0 (
REM     echo Maven no esta instalado o no esta en el PATH.
REM     goto fin
REM ) ELSE (
REM     echo Maven detectado.
REM )

:: 3. Inicializar base de datos
echo Ejecutando script SQL...
psql -U postgres -d bogotravel -f scripts\init_db.sql

IF %ERRORLEVEL% NEQ 0 (
    echo Error al ejecutar el script SQL.
    goto fin
) ELSE (
    echo Base de datos creada y poblada.
)

:: 4. Compilar el proyecto
echo Compilando proyecto con Maven...
mvn clean install

IF %ERRORLEVEL% NEQ 0 (
    echo Fallo la compilacion.
    goto fin
) ELSE (
    echo Compilacion exitosa.
)

:: 5. Ejecutar el proyecto
echo Ejecutando aplicacion...
mvn javafx:run

:fin
echo ===============================
echo Script finalizado.
pause
