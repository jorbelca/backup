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

# Ruta completa para la carpeta de backups con la fecha y hora
FINAL_FOLDER="$DEST_FOLDER/_backups/$DATE"

# Crear la carpeta donde almacenar los backups con la fecha
mkdir -p "$FINAL_FOLDER"

# Copiar la carpeta de origen al destino (dentro de la subcarpeta con fecha)
cp -r "$SOURCE_FOLDER" "$FINAL_FOLDER"

# Verificar si la carpeta se ha copiado correctamente
if [ -d "$FINAL_FOLDER/$(basename "$SOURCE_FOLDER")" ]; then
    echo "true"
else
    echo "false"
fi