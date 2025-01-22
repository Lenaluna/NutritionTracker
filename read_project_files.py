python3 -c "
import os

directories = [
    'src/main/java/com/example/NutritionTracker/Application.java',
    'src/main/java/com/example/NutritionTracker/api',
    'src/main/java/com/example/NutritionTracker/service',
    'src/main/java/com/example/NutritionTracker/entity',
    'src/main/java/com/example/NutritionTracker/repo',
    'src/main/resources',
    'src/test/java/com/example/NutritionTracker/api',
    'src/test/java/com/example/NutritionTracker/entity'
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
    print(f'--- {file_path} ---\\n{content}\\n')
"
