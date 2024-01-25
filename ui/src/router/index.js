import { createRouter, createWebHistory } from "vue-router";
import Requests from "@/views/requests/requests.vue";
import Clinics from "@/views/clinics/clinics.vue";
import DemoSingleDraft from "@/views/requests/demoSingleDraft.vue";
import DemoSingleOnline from "@/views/requests/demoSingleOnline.vue";

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
        component: DemoSingleDraft,
    },
    {
        path: "/request/2",
        component: DemoSingleOnline,
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
