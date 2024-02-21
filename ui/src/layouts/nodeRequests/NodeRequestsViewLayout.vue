<template>
    <Fieldset :legend="$t('correspondingRequests')">
        <NodeRequestsTable :nodeRequests="nodeRequestsTableElements" />
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import NodeRequestsTable from "@/components/tables/nodeRequestsTable/NodeRequestsTable.vue";
import { NodeRequestsTableElement } from "@/utils/TableElements.ts";
import { TestDataService } from "@/services/TestDataService.js";

export default {
    components: {
        Fieldset,
        NodeRequestsTable,
    },
    props: {
        nodeId: {
            type: Number,
            required: true,
        },
    },
    data() {
        return {
            allManagerRequests: [] as ManagerRequest[],
        };
    },
    //TODO check Performance
    computed: {
        nodeRequestsTableElements(): NodeRequestsTableElement[] {
            const nodeRequests: NodeRequestsTableElement[] = [];
            this.allManagerRequests.forEach((request) => {
                request.executions.forEach((execution) => {
                    const nodeStatusInfo = execution.nodeStatusInfos.find(
                        (status) => status.nodeId === this.nodeId
                    );
                    if (nodeStatusInfo) {
                        nodeRequests.push({
                            id: request.id,
                            sequenceId: execution.sequenceId,
                            externalId: execution.externalId,
                            executionState: execution.executionState,
                            title: request.query.title,
                            referenceDate: execution.referenceDate,
                            nodeStatusInfo: nodeStatusInfo,
                        });
                    }
                });
            });
            return nodeRequests;
        },
    },
    async mounted() {
        await this.fetchManagerRequests();
    },
    methods: {
        async fetchManagerRequests(): Promise<void> {
            this.allManagerRequests = await TestDataService.getRequests();
        },
    },
};
</script>
