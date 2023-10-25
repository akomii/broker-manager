import { createApp } from "vue";
import PrimeVue from "primevue/config";
import App from "./App.vue";
import "./theme.css";
import 'primeicons/primeicons.css'


createApp(App).use(PrimeVue).mount("#app");
