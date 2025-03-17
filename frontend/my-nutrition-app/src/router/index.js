// Vue Router configuration
import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import FoodSelectionView from '../views/FoodSelectionView.vue';
import ResultView from '../views/ResultView.vue';

// Define application routes
const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/selection',
    name: 'FoodSelection',
    component: FoodSelectionView
  },
  {
    path: '/results',
    name: 'Results',
    component: ResultView
  }
];

// Create and export the router instance
const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
