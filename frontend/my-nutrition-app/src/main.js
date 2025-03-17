// Entry point of the Vue application
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import axios from 'axios';

import 'vuetify/styles'; // Vuetify styles for UI components
import { createVuetify } from 'vuetify';

import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';

// Create Vuetify instance with components and directives
const vuetify = createVuetify({
  components,
  directives
});

// Set base URL for API requests
axios.defaults.baseURL = 'http://localhost:8080';

// Create and mount the Vue app
const app = createApp(App);
app.use(router);
app.use(vuetify);
app.mount('#app');