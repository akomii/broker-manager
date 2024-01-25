import { createRouter, createWebHistory } from "vue-router";
import RequestList from "@/views/requests/requests.vue";
import RequestView from "@/views/requests/demoView.vue";
import RequestEdit from "@/views/requests/demoEdit.vue";
import Clinics from "@/views/clinics/clinics.vue";

const routes = [
    {
        path: "/",
        redirect: "/requests",
    },
    {
        path: "/requests",
        component: RequestList,
    },
    {
        path: "/requests/:id",
        component: RequestView,
    },
    {
        path: "/requests/:id/edit",
        component: RequestEdit,
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
