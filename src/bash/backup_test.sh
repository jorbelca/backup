#!/bin/bash

# Ruta a la carpeta iCloud
DEST_FOLDER=~/Library/Mobile\ Documents/com~apple~CloudDocs

# Ruta a la carpeta pasada como parámetro
SOURCE_FOLDER=$1

# Asegurarse de que se ha proporcionado la carpeta de origen
if [ -z "$SOURCE_FOLDER" ]; then
    echo "Error: No se ha proporcionado la carpeta de origen."
    exit 1
fi

# Obtener la fecha y hora actuales para nombrar la subcarpeta
DATE=$(date +"%Y-%m-%d_%H-%M-%S")
if [ $? -ne 0 ]; then
    echo "Error al obtener la fecha y hora actuales."
    exit 1
fi

# Ruta completa para la carpeta de backups con la fecha y hora
FINAL_FOLDER="$DEST_FOLDER/_backups/$DATE"

# Crear la carpeta donde almacenar los backups con la fecha
mkdir -p "$FINAL_FOLDER"
if [ $? -ne 0 ]; then
    echo "Error al crear la carpeta de backups en $FINAL_FOLDER."
    exit 1
fi

# Copiar la carpeta de origen al destino (dentro de la subcarpeta con fecha)
cp -r "$SOURCE_FOLDER" "$FINAL_FOLDER"
if [ $? -ne 0 ]; then
    echo "Error al copiar la carpeta de origen a $FINAL_FOLDER."
    exit 1
fi

# Verificar si la carpeta se ha copiado correctamente
if [ -d "$FINAL_FOLDER/$(basename "$SOURCE_FOLDER")" ]; then
    echo "true"
else
    echo "false"
fi