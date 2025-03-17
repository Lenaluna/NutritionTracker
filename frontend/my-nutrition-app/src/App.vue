<script setup>
// Import Vue Router components
import { RouterLink, RouterView } from 'vue-router';
</script>

<template>
  <v-app>
    <!-- Application navigation bar -->
    <v-app-bar app color="primary">
      <v-toolbar-title>Nutrition Tracker</v-toolbar-title>
      <v-spacer></v-spacer>
      <nav>
        <RouterLink to="/">Startseite</RouterLink>
        <RouterLink to="/selection">Lebensmittelauswahl</RouterLink>
        <RouterLink to="/results">Ergebnisse</RouterLink>
      </nav>
    </v-app-bar>

    <!-- Main application content -->
    <v-main>
      <RouterView />
    </v-main>
  </v-app>
</template>

<style scoped>
/* Navigation bar styles */
nav {
  display: flex;
  gap: 1rem;
  padding-right: 1rem;
}

nav a {
  color: white;
  text-decoration: none;
  font-weight: bold;
}

nav a:hover {
  text-decoration: underline;
}
</style>
