import os

directories = [
    # Backend
    'Backend/src/main/java/com/example/NutritionTracker/Application.java',
    'Backend/src/main/java/com/example/NutritionTracker/api',
    'Backend/src/main/java/com/example/NutritionTracker/service',
    'Backend/src/main/java/com/example/NutritionTracker/entity',
    'Backend/src/main/java/com/example/NutritionTracker/repo',
    'Backend/src/main/resources',
    'Backend/src/test/java/com/example/NutritionTracker/api',
    'Backend/src/test/java/com/example/NutritionTracker/service',

    # Frontend
    'Frontend/my-angular-app/src/app/app.component.ts',
    'Frontend/my-angular-app/src/app/app.routes.ts',
    'Frontend/my-angular-app/src/app/components/food-item',
    'Frontend/my-angular-app/src/app/components/nutrition-log',
    'Frontend/my-angular-app/src/app/components/user',
    'Frontend/my-angular-app/src/app/services'
]

def list_and_read_files(directories):
    file_contents = {}
    for directory in directories:
        if os.path.isdir(directory):
            for root, _, files in os.walk(directory):
                for file in files:
                    file_path = os.path.join(root, file)
                    try:
                        with open(file_path, 'r', encoding='utf-8') as f:
                            file_contents[file_path] = f.read()
                    except Exception as e:
                        file_contents[file_path] = f'Error reading file: {e}'
        elif os.path.isfile(directory):
            try:
                with open(directory, 'r', encoding='utf-8') as f:
                    file_contents[directory] = f.read()
            except Exception as e:
                file_contents[directory] = f'Error reading file: {e}'
    return file_contents

results = list_and_read_files(directories)

for file_path, content in results.items():
    print(f'--- {file_path} ---\n{content}\n')