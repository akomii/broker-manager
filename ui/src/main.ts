import { createApp } from "vue";
import { createI18n } from "vue-i18n";
import App from "@/App.vue";
import router from "@/router";
import de from "@/locales/de.json";

import PrimeVue from "primevue/config";
import ToastService from "primevue/toastservice";
import Tooltip from "primevue/tooltip";
import "primevue/resources/themes/mdc-light-deeppurple/theme.css";
import "primeicons/primeicons.css";
import "primeflex/primeflex.css";

const i18n = createI18n({
    locale: "de",
    messages: { de },
});

createApp(App)
    .use(router)
    .use(i18n)
    .use(PrimeVue, { inputStyle: "filled", locale: de })
    .use(ToastService)
    .directive("tooltip", Tooltip)
    .mount("#app");
