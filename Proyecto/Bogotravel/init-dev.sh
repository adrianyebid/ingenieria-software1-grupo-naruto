#!/bin/bash

echo "====================================="
echo "Iniciando entorno de desarrollo..."
echo "====================================="

# 1. Verificar Java
echo "[✓] Verificando Java..."
if ! command -v java &> /dev/null
then
    echo "❌ Java no está instalado. Aborta."
    exit 1
fi

# 2. Verificar Maven
echo "[✓] Verificando Maven..."
if ! command -v mvn &> /dev/null
then
    echo "❌ Maven no está instalado. Aborta."
    exit 1
fi

# 3. Verificar PostgreSQL (psql)
echo "[✓] Verificando PostgreSQL (psql)..."
if ! command -v psql &> /dev/null
then
    echo "❌ psql no está instalado. Aborta."
    exit 1
fi

# 4. Instalar dependencias
echo "[✓] Instalando dependencias Maven..."
mvn clean install -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Error instalando dependencias Maven."
    exit 1
fi

# 5. Ejecutar script SQL para crear la base de datos
echo "[✓] Ejecutando script SQL de base de datos..."
psql -U postgres -d postgres -f scripts/init_db.sql
if [ $? -ne 0 ]; then
    echo "❌ Error ejecutando el script SQL. Verifica usuario/contraseña de PostgreSQL."
    exit 1
fi

# 6. Ejecutar el proyecto
echo "[✓] Ejecutando aplicación..."
mvn javafx:run
