#!/bin/bash

# Ruta a la carpeta de destino en iCloud
DEST_FOLDER=~/Library/Mobile\ Documents/com~apple~CloudDocs

# Expansión de la ruta (esto convierte ~ en la ruta absoluta del home)
DEST_FOLDER=$(eval echo $DEST_FOLDER)

# Ruta completa para la carpeta de test
TEST_FOLDER="$DEST_FOLDER/TEST"

# Crear la carpeta TEST
mkdir -p "$TEST_FOLDER"

# Verificar si la carpeta TEST se ha creado correctamente
if [ -d "$TEST_FOLDER" ]; then
    echo "true"
       rm -rf "$TEST_FOLDER"
else
    echo "false"
fi