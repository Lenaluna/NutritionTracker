#!/bin/bash

# Basisverzeichnis (hier das Hauptverzeichnis des Projekts setzen)
BASE_DIR="src/main/java/com/example/NutritionTracker"

# Durchlaufe rekursiv alle Dateien im Verzeichnis
find "$BASE_DIR" src/main/resources src/test -type f | while read -r file; do
    echo -e "\n--- Inhalt von: $file ---"
    cat "$file"
done