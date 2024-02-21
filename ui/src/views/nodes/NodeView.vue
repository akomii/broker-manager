<template>
    <div v-if="node">
        <NodeViewHeader
            :id="node.id"
            :commonName="node.clientDN.CN"
            :tags="node.tags"
            :menu="viewMenu"
        />
    </div>
    <div v-else class="flex justify-content-center flex-wrap py-4">
        <ProgressSpinner />
    </div>
</template>

<script lang="ts">
import ProgressSpinner from "primevue/progressspinner";
import { TestDataService } from "@/services/TestDataService";
import { ManagerNode } from "@/utils/Types";
import NodeViewHeader from "@/layouts/headers/NodeViewHeader.vue";

export default {
    components: {
        ProgressSpinner,
        NodeViewHeader,
    },
    data() {
        return {
            node: null as ManagerNode | null,
            // TODO: add routing and services
            viewMenu: [
                { label: this.$t("menu.node.unsubscribe") },
                { label: this.$t("menu.node.edit") },
            ],
        };
    },
    mounted() {
        const nodeId = this.$route.params.id;
        TestDataService.getNodeById(nodeId)
            .then((data: ManagerNode) => {
                this.node = data;
            })
            .catch((error) => {
                console.error("Error fetching node:", error);
            });
    },
};
</script>
