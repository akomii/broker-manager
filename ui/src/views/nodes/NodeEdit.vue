<template>
    <div v-if="node">
        <NodeHeaderEdit
            :id="node.id"
            :commonName="node.clientDN.CN"
            :tags="node.tags"
            :menu="viewMenu"
            @update:tags="node.tags = $event"
            @update:commonName="node.clientDN.CN = $event"
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
import NodeHeaderEdit from "@/layouts/nodes/headers/NodeHeaderEdit.vue";

export default {
    components: {
        ProgressSpinner,
        NodeHeaderEdit,
    },
    data() {
        return {
            node: null as ManagerNode | null,
            // TODO: add routing and services
            viewMenu: [
                { label: this.$t("menu.node.save") },
                { label: this.$t("cancel") },
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
