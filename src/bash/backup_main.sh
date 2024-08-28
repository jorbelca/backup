#!/bin/bash


# Archivo de señal para detener el script
STOP_FILE="$HOME/Documents/backup_stop_signal"

# RUTA A LA CARPETA ICLOUD
DEST_FOLDER=~/Library/Mobile\ Documents/com~apple~CloudDocs

# RUTA A LA CARPETA POR PARAMETROS
SOURCE_FOLDER=$1
# PARAMETROS DIAS 
KEEP_LAST_3_DAYS=$2
KEEP_LAST_WEEK=$3


# Asegurarse de que hay una carpeta de origen
if [ -z "$SOURCE_FOLDER" ]; then
    echo "Error: No se ha proporcionado la carpeta de origen."
    exit 1
fi

# Crear carpeta para backups, con la fecha actual y la hora.minutos (formato HH.MM)
TIMESTAMP=$(date +"%Y-%m-%d_%H.%M")
FINAL_FOLDER="$DEST_FOLDER/_backups/$TIMESTAMP"
mkdir -p "$FINAL_FOLDER"

# Copiar la carpeta origen a la ubicación de backup
echo "Copiando carpeta de origen..."
cp -r "$SOURCE_FOLDER" "$FINAL_FOLDER/"

# Verificar si la carpeta se ha copiado correctamente
if [ $? -eq 0 ]; then
    echo "Backup completado exitosamente."

    # Obtener el nombre de la carpeta original
    FOLDER_NAME=$(basename "$SOURCE_FOLDER")

    # Comprimir la carpeta copiada
    echo "Comprimiendo carpeta..."
    tar -czvf "$FINAL_FOLDER/$FOLDER_NAME.tar.gz" -C "$FINAL_FOLDER" "$FOLDER_NAME" 2> "$FINAL_FOLDER/compression_error.log"

    # Esperar a que termine el comando tar y capturar el código de salida
    TAR_EXIT_CODE=$?
    if [ $TAR_EXIT_CODE -eq 0 ]; then
        echo "Compresión completada: $FINAL_FOLDER/$FOLDER_NAME.tar.gz"

        # Eliminar la carpeta sin comprimir
        rm -rf "$FINAL_FOLDER/$FOLDER_NAME"
        rm "$FINAL_FOLDER/compression_error.log" # Eliminar el archivo de error
        echo "true"
    else
        echo "Error al comprimir la carpeta. Revisa el archivo $FINAL_FOLDER/compression_error.log para más detalles."
        echo "false"
    fi
else
    echo "Error al copiar la carpeta."
    echo "false"
fi


# Lógica para eliminar backups antiguos
if [ "$KEEP_LAST_WEEK" = "true" ]; then
    echo "Eliminando backups más antiguos de 7 días..."
    find "$DEST_FOLDER/_backups/" -type d -mtime +7 -exec rm -rf {} \;
elif [ "$KEEP_LAST_3_DAYS" = "true" ]; then
    echo "Eliminando backups más antiguos de 3 días..."
    find "$DEST_FOLDER/_backups/" -type d -mtime +3 -exec rm -rf {} \;
fi

exit 0