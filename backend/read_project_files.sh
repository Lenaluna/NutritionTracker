#!/bin/bash

# Liste der zu überprüfenden Dateien und Verzeichnisse
directories=(
    "src/main/java/com/example/NutritionTracker/Application.java"
       "src/main/java/com/example/NutritionTracker/api/FoodItemController.java"
       "src/main/java/com/example/NutritionTracker/api/NutritionLogController.java"
       "src/main/java/com/example/NutritionTracker/api/UserController.java"
       "src/main/java/com/example/NutritionTracker/config/DevDataLoader.java"
       "src/main/java/com/example/NutritionTracker/config/StartupRunner.java"
       "src/main/java/com/example/NutritionTracker/entity/FoodItem.java"
       "src/main/java/com/example/NutritionTracker/entity/NutritionLog.java"
       "src/main/java/com/example/NutritionTracker/entity/User.java"
       "src/main/java/com/example/NutritionTracker/repo/FoodItemRepository.java"
       "src/main/java/com/example/NutritionTracker/repo/NutritionLogRepository.java"
       "src/main/java/com/example/NutritionTracker/repo/UserRepository.java"
       "src/main/java/com/example/NutritionTracker/service/FoodItemService.java"
       "src/main/java/com/example/NutritionTracker/service/NutritionLogService.java"
       "src/main/java/com/example/NutritionTracker/service/UserService.java"
       "src/main/resources/application-dev.yml"
       "src/main/resources/application-integration-test.yml"
       "src/main/resources/application.yml"
       "src/test/java/com/example/NutritionTracker/api/FoodItemControllerTest.java"
       "src/test/java/com/example/NutritionTracker/service/NutritionLogServiceTest.java"
       "src/test/java/com/example/NutritionTracker/api/DevDataLoaderUserTest.java"
       "src/test/java/com/example/NutritionTracker/integration/NutritionLogIntegrationTest.java"
)

# Gehe durch jede Datei und prüfe, ob sie existiert
for file in "${directories[@]}"; do
    if [[ -f "$file" ]]; then
        echo -e "\n--- Inhalt von: $file ---"
        cat "$file"
    elif [[ -d "$file" ]]; then
        echo -e "\n--- Verzeichnis existiert: $file ---"
    else
        echo -e "\n[WARNUNG] $file existiert nicht!"
    fi
done