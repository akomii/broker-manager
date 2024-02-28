// Vue imports
import { createApp } from "vue";
import { createI18n } from "vue-i18n";
import App from "./App.vue";
import router from "./router";

// PrimeVue imports
import PrimeVue from "primevue/config";
import ToastService from "primevue/toastservice";
import ConfirmationService from "primevue/confirmationservice";
import Tooltip from "primevue/tooltip";

// Styles imports
import "primevue/resources/themes/mdc-light-deeppurple/theme.css";
import "primeicons/primeicons.css";
import "primeflex/primeflex.css";
import "./assets/global.css";

// Locale imports
import de from "./locales/de.json";

// i18n configuration
const i18n = createI18n({
    locale: "de",
    messages: { de },
});

// PrimeVue configuration
const primeVueConfig = { inputStyle: "filled", locale: de };

// App creation
const app = createApp(App)
    .use(router)
    .use(i18n)
    .use(PrimeVue, primeVueConfig)
    .use(ToastService)
    .use(ConfirmationService)
    .directive("tooltip", Tooltip)
    .mount("#app");
