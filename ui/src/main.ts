import { createApp } from "vue";
import PrimeVue from "primevue/config";
import App from "./App.vue";
import "primevue/resources/themes/lara-light-teal/theme.css";
import "primeicons/primeicons.css";

createApp(App).use(PrimeVue).mount("#app");
