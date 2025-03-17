#!/bin/bash

# Verzeichnis des Skripts ermitteln und dorthin wechseln
SCRIPT_DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
cd "$SCRIPT_DIR"

# Ziel-Datei für die Ausgabe
TARGET_FILE="project_code_output.txt"

# Vorherige Datei löschen, falls vorhanden
rm -f "$TARGET_FILE"

# Funktion zum Anhängen des Dateiinhalts mit Trennlinien
append_file_content() {
    local file=$1
    echo -e "\n========== $file ==========" >> "$TARGET_FILE"
    cat "$file" >> "$TARGET_FILE"
    echo -e "\n" >> "$TARGET_FILE"
}

# Verzeichnisse und Dateien
FOLDERS=("router" "views")
FILES=("App.vue" "main.js")

# Alle Vue- und JS-Dateien in den Verzeichnissen durchlaufen
for folder in "${FOLDERS[@]}"; do
    if [ -d "$folder" ]; then
        for file in $(find "$folder" -type f -name "*.vue" -o -name "*.js"); do
            append_file_content "$file"
        done
    else
        echo "⚠ Verzeichnis $folder nicht gefunden!"
    fi
done

for file in "${FILES[@]}"; do
    if [ -f "$file" ]; then
        append_file_content "$file"
    else
        echo "⚠ Datei $file nicht gefunden!"
    fi
done

echo "✅ Code wurde erfolgreich in $TARGET_FILE gespeichert."