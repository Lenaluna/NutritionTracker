import axios from 'axios';

// Axios instance with base API URL
const api = axios.create({
  baseURL: 'http://localhost:8080/api', // Backend API
  headers: { 'Content-Type': 'application/json' } // JSON requests
});

export default api;
