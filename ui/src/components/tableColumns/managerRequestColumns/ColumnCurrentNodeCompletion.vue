<template>
    <Column
        :header="$t('acceptance')"
        sortable
    >
        <template #body="slotProps">
            <div
                v-if="
                    getScheduledClosingDateOfMostCurrentExecution(
                        slotProps.data.executions
                    ) !== null
                "
            >
                <DateView
                    :date="
                        getScheduledClosingDateOfMostCurrentExecution(
                            slotProps.data.executions
                        )
                    "
                />
            </div>
            <NotAvailableIcon v-else />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import DateView from "@/components/datePickers/DateView.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";
import { RequestExecution } from "@/utils/Types";
import { ExecutionState } from "@/utils/Enums";

export default {
    components: {
        Column,
        DateView,
        NotAvailableIcon,
    },
    methods: {
        getScheduledClosingDateOfMostCurrentExecution(
            executions: RequestExecution[]
        ): Date | null {
            let latestExecution: RequestExecution | null = null;
            let latestPublishedDate: Date | null = null;
            for (const execution of executions) {
                if (
                    execution.executionState !== ExecutionState.PUBLISHED ||
                    execution.publishedDate === null
                ) {
                    continue;
                }
                const currentPublishedDate = new Date(execution.publishedDate);
                if (
                    !latestPublishedDate ||
                    currentPublishedDate > latestPublishedDate
                ) {
                    latestPublishedDate = currentPublishedDate;
                    latestExecution = execution;
                }
            }
            return latestExecution
                ? new Date(latestExecution.scheduledClosingDate)
                : null;
        },
    },
};
</script>



<!--
<template>
    <Column field="completion" :header="$t('acceptance')">
        <template #body="slotProps">
            <TargetNodesTableDialog
                v-if="isNodeStatusInfoNotEmpty(slotProps.data.nodeStatusInfos)"
                :targetNodes="slotProps.data.nodes"
                :targetNodeStatusInfos="slotProps.data.nodeStatusInfos"
                :sequenceId="slotProps.data.sequenceId"
            />
            <NotAvailableIcon v-else class="flex justify-content-center" />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import TargetNodesTableDialog from "@/components/dialogs/TargetNodesTableDialog.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";
import { NodeStatusInfo } from "@/utils/Types";

export default {
    components: {
        Column,
        TargetNodesTableDialog,
        NotAvailableIcon,
    },
    methods: {
        isNodeStatusInfoNotEmpty(nodeStatusInfo: NodeStatusInfo[]): boolean {
            return nodeStatusInfo.length > 0;
        },
    },
};
</script>
-->
