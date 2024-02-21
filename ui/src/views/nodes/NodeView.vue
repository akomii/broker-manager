<template>
    <div v-if="node">
        <NodeViewHeader
            :id="node.id"
            :commonName="node.clientDN.CN"
            :tags="node.tags"
            :menu="viewMenu"
        />
        <NodeRequestsTable :requests="managerRequestsOfNode" />
    </div>
    <div v-else class="flex justify-content-center flex-wrap py-4">
        <ProgressSpinner />
    </div>
</template>

<script lang="ts">
import ProgressSpinner from "primevue/progressspinner";
import { TestDataService } from "@/services/TestDataService";
import { ManagerNode, ManagerRequest } from "@/utils/Types";
import NodeViewHeader from "@/layouts/headers/NodeViewHeader.vue";
import NodeRequestsTable from "@/components/tables/nodeRequestsTable/NodeRequestsTable.vue";

export default {
    components: {
        ProgressSpinner,
        NodeViewHeader,
        NodeRequestsTable,
    },
    data() {
        return {
            node: null as ManagerNode | null,
            allManagerRequests: [] as ManagerRequest[],
            // TODO: add routing and services
            viewMenu: [
                { label: this.$t("menu.node.unsubscribe") },
                { label: this.$t("menu.node.edit") },
            ],
        };
    },
    computed: {
        managerRequestsOfNode(): ManagerRequest[] {
            return this.allManagerRequests;
        },
    },
    async mounted() {
        await this.fetchManagerRequests();
        const nodeId = this.$route.params.id;
        TestDataService.getNodeById(nodeId)
            .then((data: ManagerNode) => {
                this.node = data;
            })
            .catch((error) => {
                console.error("Error fetching node:", error);
            });
    },
    methods: {
        async fetchManagerRequests(): Promise<void> {
            this.allManagerRequests = await TestDataService.getRequests();
        },
    },
};
</script>
