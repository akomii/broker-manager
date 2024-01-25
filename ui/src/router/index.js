import { createRouter, createWebHistory } from "vue-router";
import Requests from "@/views/requests/requests.vue";
import Clinics from "@/views/clinics/clinics.vue";
import DemoView from "@/views/requests/demoView.vue";

const routes = [
    {
        path: "/",
        redirect: "/requests",
    },
    {
        path: "/requests",
        component: Requests,
    },
    {
        path: "/request/:id",
        component: DemoView,
    },
    {
        path: "/clinics",
        component: Clinics,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
