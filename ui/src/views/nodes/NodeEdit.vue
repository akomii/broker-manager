<template>
    <div v-if="node">
        <NodeEditHeader
            :id="node.id"
            :commonName="node.clientDN.CN"
            :tags="node.tags"
            :menu="viewMenu"
            @update:tags="node.tags = $event"
            @update:commonName="node.clientDN.CN = $event"
        />
        <div class="grid">
            <div class="col-4">
                <NodeMetaEditLayout
                    class="ml-3"
                    :apiKey="node.apiKey"
                    :organization="node.clientDN.O"
                    :location="node.clientDN.L"
                    :lastContact="node.lastContact"
                    @update:apiKey="node.apiKey = $event"
                    @update:organization="node.clientDN.O = $event"
                    @update:location="node.clientDN.L = $event"
                />
            </div>
            <div class="col-8"></div>
        </div>
    </div>
    <div v-else class="flex justify-content-center flex-wrap py-4">
        <ProgressSpinner />
    </div>
</template>

<script lang="ts">
import ProgressSpinner from "primevue/progressspinner";
import { TestDataService } from "@/services/TestDataService";
import { ManagerNode } from "@/utils/Types";
import NodeEditHeader from "@/layouts/headers/NodeEditHeader.vue";
import NodeMetaEditLayout from "@/layouts/meta/NodeMetaEditLayout.vue";

export default {
    components: {
        ProgressSpinner,
        NodeEditHeader,
        NodeMetaEditLayout,
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
