<template>
  <v-container class="py-5">
    <h1>Wähle deine Lebensmittel</h1>

    <!-- List of available food items -->
    <v-list>
      <v-list-item
        v-for="food in foodItems"
        :key="food.id"
        @click="toggleFoodSelection(food)"
        :class="{ 'selected': selectedFoods.includes(food) }"
      >
        <v-list-item-title>{{ food.name }}</v-list-item-title>
      </v-list-item>
    </v-list>

    <!-- Button to calculate amino acid profile -->
    <v-btn class="mt-4" @click="processSelections" :disabled="!selectedFoods.length">
      Aminosäureprofil berechnen
    </v-btn>
  </v-container>
</template>

<script>
import { ref, onMounted } from 'vue';
import api from '../api';

export default {
  name: 'FoodSelectionView',
  setup() {
    const foodItems = ref([]); // Stores all available food items
    const selectedFoods = ref([]); // Stores selected food items
    const nutritionLogId = ref(null); // Stores the created NutritionLog ID

    // Fetch food items from the API when component is mounted
    onMounted(async () => {
      try {
        const res = await api.get('/food-items/all');
        foodItems.value = res.data;
      } catch (error) {
        console.error('Error fetching food items:', error);
      }
    });

    // Create a new NutritionLog if one doesn't exist
    const createNutritionLog = async () => {
      console.log("🔍 Starting createNutritionLog...");

      const userData = JSON.parse(localStorage.getItem('userData')); // Retrieve user ID
      console.log("📂 userData from localStorage:", userData);

      if (!userData || !userData.id) {
        console.error("Missing user ID. No NutritionLog will be created.");
        return;
      }

      try {
        const response = await api.post('/nutrition-logs/create', { userId: userData.id });
        nutritionLogId.value = response.data.id;
        localStorage.setItem('nutritionLogId', nutritionLogId.value);
        console.log("NutritionLog created with ID:", nutritionLogId.value);
      } catch (error) {
        console.error("Error creating NutritionLog:", error);
      }
    };

    // Select or deselect a food item
    const toggleFoodSelection = (food) => {
      const index = selectedFoods.value.findIndex((item) => item.id === food.id);

      if (index === -1) {
        selectedFoods.value.push(food); // Add food item if not already selected
      } else {
        selectedFoods.value.splice(index, 1); // Remove food item if already selected
      }
    };

    // Process selections: Create NutritionLog and add selected food items
    const processSelections = async () => {
      await createNutritionLog(); // Create a NutritionLog if it doesn't exist

      try {
        // Add all selected food items to the NutritionLog
        for (const food of selectedFoods.value) {
          await api.post(`/nutrition-logs/${nutritionLogId.value}/food-items/${food.id}`);
          console.log(`FoodItem ${food.id} added to NutritionLog ${nutritionLogId.value}.`);
        }

        // Save selection and navigate to results page
        localStorage.setItem('selectedFoods', JSON.stringify(selectedFoods.value));
        window.location.href = '/results';
      } catch (error) {
        console.error('Error adding food items:', error);
      }
    };

    return { foodItems, selectedFoods, toggleFoodSelection, processSelections };
  },
};
</script>

<style scoped>
/* Highlights selected food items */
.selected {
  background-color: lightblue !important;
  color: black !important;
  font-weight: bold;
  border-radius: 5px;
}
</style>
