import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";

import PrimeVue from "primevue/config";
import ToastService from 'primevue/toastservice';
import "primevue/resources/themes/mdc-light-deeppurple/theme.css";
import "primeicons/primeicons.css";
import "primeflex/primeflex.css";

createApp(App)
  .use(router)
  .use(PrimeVue, { inputStyle: "filled" })
  .use(ToastService)
  .mount("#app");
