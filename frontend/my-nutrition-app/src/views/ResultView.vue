<template>
  <v-container class="py-5">
    <!-- Header with user greeting -->
    <h1 v-if="userName">Hallo {{ userName }}, hier sind deine Ergebnisse!</h1>
    <h1 v-else>Hier sind deine Ergebnisse.</h1>

    <div v-if="profile">
      <!-- Selected foods section -->
      <h2>Ausgewählte Lebensmittel</h2>
      <v-list v-if="selectedFoods.length">
        <v-list-item v-for="food in selectedFoods" :key="food.id">
          <v-list-item-title>{{ food.name }}</v-list-item-title>
        </v-list-item>
      </v-list>
      <p v-else class="error">Keine Lebensmittel ausgewählt</p>

      <!-- Total amino acids section -->
      <h2>Gesamt-Aminosäuren</h2>
      <v-table v-if="profile.aminoAcids">
        <thead>
          <tr>
            <th>Aminosäure</th>
            <th>Menge (g)</th>
          </tr>
        </thead>
        <tbody>
      <!-- Amino acids are now displayed in the same order as daily requirements. -->          <tr v-for="key in Object.keys(profile.dailyNeeds)" :key="key">
            <td>{{ key }}</td>
            <td v-if="profile.aminoAcids[key] !== undefined">
              {{ profile.aminoAcids[key].toFixed(2) }}
            </td>
            <td v-else>0.00</td>
          </tr>
        </tbody>
      </v-table>
      <p v-else class="error">Keine Aminosäuredaten verfügbar</p>

      <!-- Daily requirement section -->
      <h2>Tagesbedarf</h2>
      <v-table v-if="profile.dailyNeeds">
        <thead>
          <tr>
            <th>Aminosäure</th>
            <th>Benötigte Menge (g)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(value, key) in profile.dailyNeeds" :key="key">
            <td>{{ key }}</td>
            <td>{{ value.toFixed(2) }}</td>
          </tr>
        </tbody>
      </v-table>
      <p v-else class="error">Keine Tagesbedarfsdaten verfügbar</p>

      <!-- Percentage coverage section -->
      <h2>Prozentuale Deckung</h2>
      <p v-if="profile.coverageMessage" class="coverage-message">
        {{ profile.coverageMessage }}
      </p>
      <v-table v-if="profile.dailyCoverage">
        <thead>
          <tr>
            <th>Aminosäure</th>
            <th>Deckung (%)</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(value, key) in profile.dailyCoverage" :key="key">
            <td>{{ key }}</td>
            <td>{{ value.toFixed(1) }}%</td>
          </tr>
        </tbody>
      </v-table>
      <p v-else class="error">Keine Deckungsdaten verfügbar</p>
    </div>

    <!-- Close application button -->
    <v-btn class="mt-4" @click="closeApp">
      Zum Start
    </v-btn>
  </v-container>
</template>

<script>
import { ref, onMounted } from "vue";
import api from "../api";

export default {
  name: "ResultView",
  setup() {
    const profile = ref(null);
    const selectedFoods = ref([]);
    const userData = ref(null);
    const userName = ref("");

    // Load data from local storage and make API requests
    onMounted(async () => {
      console.log("Lade Daten...");

      const storedUserData = localStorage.getItem("userData");
      const storedSelectedFoods = localStorage.getItem("selectedFoods");

      if (storedUserData) {
        userData.value = JSON.parse(storedUserData);
        userName.value = userData.value.name || "Unbekannter Benutzer";
      }

      if (storedSelectedFoods) {
        selectedFoods.value = JSON.parse(storedSelectedFoods);
      }

      try {
        // Fetch total amino acid values
        const aminoAcidsRes = await api.post("/amino-profile/sum");
        const aminoAcids = aminoAcidsRes.data;

        // Fetch daily requirement values
        const dailyNeedsRes = await api.get("/amino-profile/daily-needs");
        const dailyNeeds = dailyNeedsRes.data;

        // Fetch percentage coverage values
        const coverageRes = await api.post("/amino-profile/coverage");
        const dailyCoverage = coverageRes.data;

        // Generate coverage message
        const coverageMessage = generateCoverageMessage(userName.value, dailyCoverage);

        // Store data
        profile.value = {
          aminoAcids,
          dailyNeeds,
          dailyCoverage,
          coverageMessage,
        };
      } catch (error) {
        console.error("Fehler beim Abrufen der Daten:", error);
      }
    });

    // Function to generate the percentage coverage message
    const generateCoverageMessage = (name, coverage) => {
      const overallCoverage = Object.values(coverage).reduce((a, b) => a + b, 0) / Object.keys(coverage).length;
      return `${name}, deine durchschnittliche Aminosäurenabdeckung beträgt ${overallCoverage.toFixed(1)}%.`;
    };

    // Function to close the application and clear storage
    const closeApp = () => {
      localStorage.clear();
      window.location.href = "/";
    };

    return { profile, selectedFoods, userData, userName, closeApp };
  }
};
</script>

<style scoped>
/* Table styling */
.v-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.v-table th,
.v-table td {
  padding: 8px;
  border: 1px solid #ddd;
  text-align: left;
}

.v-table th {
  background-color: #f4f4f4;
}

/* Styling for error messages */
.error {
  color: red;
  font-style: italic;
  font-weight: bold;
}

/* Styling for coverage message */
.coverage-message {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 10px;
}
</style>
