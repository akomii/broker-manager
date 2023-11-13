import { createApp } from "vue";
import PrimeVue from "primevue/config";
import App from "./App.vue";
import "primevue/resources/themes/md-light-deeppurple/theme.css";
import "primeicons/primeicons.css";

createApp(App).use(PrimeVue, { inputStyle: "filled" }).mount("#app");
