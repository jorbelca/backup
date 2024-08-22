#!/bin/bash

# RUTA A LA CARPETA ICLOUD
DEST_FOLDER=~/Library/Mobile\ Documents/com~apple~CloudDocs


# RUTA A LA CARPETA POR PARAMETROS
SOURCE_FOLDER=$1
# Asegurarse de que hay carpeta 
if [ -z "$SOURCE_FOLDER" ]; then
    echo "Error: No se ha proporcionado la carpeta de origen."
    exit 1
fi



# Ruta completa para la carpeta de test
FINAL_FOLDER="$DEST_FOLDER/_backup"

# Crear la carpeta TEST
mkdir -p "$FINAL_FOLDER"

# Verificar si la carpeta  se ha creado correctamente
if [ -d "$FINAL_FOLDER" ]; then
    echo "true"
else
    echo "false"
fi