#!/bin/bash

# RUTA A LA CARPETA DONDE SE CREARÁ EL ARCHIVO DE SEÑAL
STOP_SIGNAL_PATH=~/Library/Mobile\ Documents/com~apple~CloudDocs/_backups/stop_signal

# Crear el archivo de señal
touch "$STOP_SIGNAL_PATH"

# Confirmar que se ha creado el archivo de señal
if [ -f "$STOP_SIGNAL_PATH" ]; then
    echo "Señal de detención creada exitosamente."
     # Eliminar el archivo
     rm -rf "$STOP_SIGNAL_PATH"
else
    echo "Error al crear la señal de detención."
fi