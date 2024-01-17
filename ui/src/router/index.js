import { createRouter, createWebHistory } from "vue-router";
import Requests from "@/views/requests/requests.vue";
import Clinics from "@/views/clinics/clinics.vue";
import DemoDraftSingle from "@/views/requests/demoDraftSingle.vue";

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
        path: "/request/1",
        component: DemoDraftSingle,
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
