import { createRouter, createWebHistory } from "vue-router";
import Requests from "@/views/requests/Requests.vue";
import RequestView from "@/views/requests/RequestView.vue";
import RequestEdit from "@/views/requests/RequestEdit.vue";
import RequestResult from "@/views/requests/RequestResult.vue";
import Nodes from "@/views/nodes/Nodes.vue";
import NotFound from "@/views/errors/NotFound.vue";
import ServerError from "@/views/errors/ServerError.vue";

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
    { path: "/500", component: ServerError },
    { path: "/:catchAll(.*)", component: NotFound },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
