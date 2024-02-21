<template>
    <Fieldset :legend="$t('correspondingRequests')">
        <NodeExecutionsTable :nodeExecutions="nodeExecutions" />
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import NodeExecutionsTable from "@/components/tables/nodeExecutionsTable/NodeExecutionsTable.vue";
import { NodeExecutionsTableElement } from "@/utils/TableElements.ts";
import { ManagerRequest } from "@/utils/Types";
import { TestDataService } from "@/services/TestDataService.js";

export default {
    components: {
        Fieldset,
        NodeExecutionsTable,
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
        nodeExecutions(): NodeExecutionsTableElement[] {
            const nodeRequests: NodeExecutionsTableElement[] = [];
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
                            query: {
                                title: request.query.title,
                            },
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
