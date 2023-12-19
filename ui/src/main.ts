import { createApp } from "vue";
import App from "@/App.vue";
import router from "@/router";
import GermanLocale from "@/locales/de.json";

import PrimeVue from "primevue/config";
import ToastService from "primevue/toastservice";
import Tooltip from "primevue/tooltip";
import "primevue/resources/themes/mdc-light-deeppurple/theme.css";
import "primeicons/primeicons.css";
import "primeflex/primeflex.css";

createApp(App)
    .use(router)
    .use(PrimeVue, { inputStyle: "filled", locale: GermanLocale })
    .use(ToastService)
    .directive("tooltip", Tooltip)
    .mount("#app");
