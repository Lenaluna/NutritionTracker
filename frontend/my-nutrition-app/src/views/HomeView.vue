<template>
  <v-container class="py-5">
    <h1>Willkommen zum Nutrition Tracker</h1>
    <p>Berechne dein persönliches Aminosäurenprofil.</p>

    <v-form>
      <!-- Name input field with validation -->
      <v-text-field
        v-model="userData.name"
        label="Name"
        class="input-field"
        :error-messages="errors.name"
      ></v-text-field>

      <!-- Weight input field with validation -->
      <v-text-field
        v-model="userData.weight"
        label="Gewicht (kg)"
        type="number"
        class="input-field"
        :error-messages="errors.weight"
      ></v-text-field>

      <!-- Age input field with validation -->
      <v-text-field
        v-model="userData.age"
        label="Alter"
        type="number"
        class="input-field"
        :error-messages="errors.age"
      ></v-text-field>

      <!-- Custom checkboxes for user preferences -->
      <div class="custom-checkbox-wrapper" @click="toggleDecorator('isAthlete')">
        <div class="custom-checkbox" :class="{ checked: userData.isAthlete }">
          <span v-if="userData.isAthlete">✓</span>
        </div>
        <label>Athlete</label>
      </div>

      <div class="custom-checkbox-wrapper" @click="toggleDecorator('isVegan')">
        <div class="custom-checkbox" :class="{ checked: userData.isVegan }">
          <span v-if="userData.isVegan">✓</span>
        </div>
        <label>Vegan</label>
      </div>

      <div class="custom-checkbox-wrapper" @click="toggleDecorator('isLongevityFocused')">
        <div class="custom-checkbox" :class="{ checked: userData.isLongevityFocused }">
          <span v-if="userData.isLongevityFocused">✓</span>
        </div>
        <label>Gesund altern</label>
      </div>

      <!-- General error message -->
      <p v-if="generalError" class="error-message">{{ generalError }}</p>

      <!-- Submit button -->
      <v-btn class="mt-4 submit-button" @click="goToSelection">
        Senden
      </v-btn>
    </v-form>
  </v-container>
</template>

<script>
import { ref } from "vue";
import api from "../api";

export default {
  name: "HomeView",
  setup() {
    // Stores user data
    const userData = ref({
      name: "",
      weight: null,
      age: null,
      isAthlete: false,
      isVegan: false,
      isLongevityFocused: false,
    });

    // Stores validation errors for individual fields
    const errors = ref({
      name: "",
      weight: "",
      age: "",
    });

    // Stores general errors (e.g., 404 or 500 errors)
    const generalError = ref("");

    // Toggles user preference checkboxes
    const toggleDecorator = (key) => {
      userData.value[key] = !userData.value[key];
      console.log(`${key} changed:`, userData.value[key]);
    };

    // Saves user data and handles errors
    const goToSelection = async () => {
      try {
        // Debug: Check data before sending
        console.log("Sent user data:", JSON.stringify(userData.value));

        // Reset previous errors
        errors.value = { name: "", weight: "", age: "" };
        generalError.value = "";

        // Send data to backend
        const response = await api.put("/user-data/update", userData.value);
        console.log("✅ Backend response:", response.data);

        // Store response in local storage
        localStorage.setItem("userData", JSON.stringify(response.data));
        console.log("✅ userData saved in localStorage:", response.data);

        // Redirect to food selection page
        window.location.href = "/selection";
      } catch (error) {
        console.error("Error saving user data:", error);

        if (error.response) {
          const { status, data } = error.response;

          // Validation error (400 BAD REQUEST) - show field errors
          if (status === 400 && typeof data === "object") {
            Object.keys(data).forEach((field) => {
              if (errors.value[field] !== undefined) {
                errors.value[field] = data[field];
              }
            });
            return;
          }

          // 404 NOT FOUND (EntityNotFoundException)
          if (status === 404) {
            generalError.value = "Error: User not found.";
            return;
          }
        }

        // Other errors (500, etc.)
        generalError.value = "An unexpected error occurred. Please try again later.";
      }
    };

    console.log("userData initialized:", userData.value);

    return { userData, toggleDecorator, goToSelection, errors, generalError };
  },
};
</script>

<style scoped>
/* Limits maximum width of input fields */
.input-field {
  max-width: 400px;
}

/* Styling for custom checkboxes */
.custom-checkbox-wrapper {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 15px;
  cursor: pointer;
}

/* Default styling of checkboxes */
.custom-checkbox {
  width: 24px;
  height: 24px;
  border: 2px solid #007bff;
  border-radius: 5px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  color: white;
  background-color: white;
  transition: background-color 0.2s, color 0.2s;
}

/* Styling when checkbox is active */
.custom-checkbox.checked {
  background-color: #007bff;
  color: white;
}

/* Error messages */
.error-message {
  color: red;
  font-weight: bold;
  margin-top: 10px;
}

/* Submit button styling */
.submit-button {
  margin-top: 20px;
}
</style>
