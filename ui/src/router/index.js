import { createRouter, createWebHistory } from "vue-router";
import RequestList from "@/views/requests/requests.vue";
import RequestView from "@/views/requests/demoView.vue";
import RequestEdit from "@/views/requests/demoEdit.vue";
import RequestResult from "@/views/requests/requestResult.vue";
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
        path: "/requests/:id/results",
        component: RequestResult,
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
