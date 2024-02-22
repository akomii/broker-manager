<template>
    <div v-if="node">
        <NodeViewHeader
            :id="node.id"
            :commonName="node.clientDN.CN"
            :tags="node.tags"
            :menu="viewMenu"
        />
        <div class="grid">
            <div class="col-4">
                <NodeMetaViewLayout
                    class="ml-3"
                    :apiKey="node.apiKey"
                    :organization="node.clientDN.O"
                    :location="node.clientDN.L"
                    :lastContact="node.lastContact"
                />
                <NotesViewLayout
                    class="ml-3"
                    :notes="node.notes"
                    @update:notes="node.notes = $event"
                />
            </div>
            <div class="col-8">
                <NodeExecutionsViewLayout class="mr-3" :nodeId="node.id" />
            </div>
        </div>
    </div>
    <div v-else class="flex justify-content-center flex-wrap py-4">
        <ProgressSpinner />
    </div>
</template>

<script lang="ts">
import ProgressSpinner from "primevue/progressspinner";
import { TestDataService } from "@/services/TestDataService";
import { ManagerNode } from "@/utils/Types.ts";
import NodeViewHeader from "@/layouts/headers/NodeViewHeader.vue";
import NodeExecutionsViewLayout from "@/layouts/executions/NodeExecutionsViewLayout.vue";
import NodeMetaViewLayout from "@/layouts/meta/NodeMetaViewLayout.vue";
import NotesViewLayout from "@/layouts/notes/NotesViewLayout.vue";

export default {
    components: {
        ProgressSpinner,
        NodeViewHeader,
        NodeExecutionsViewLayout,
        NodeMetaViewLayout,
        NotesViewLayout,
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
