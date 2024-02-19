import { createRouter, createWebHistory } from "vue-router";
import RequestList from "@/views/requests/requests.vue";
import RequestView from "@/views/requests/demoView.vue";
import RequestEdit from "@/views/requests/demoEdit.vue";
import RequestResult from "@/views/requests/requestResult.vue";
import Nodes from "@/views/nodes/nodes.vue";

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
        path: "/nodes",
        component: Nodes,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
