<template>
    <Column
        field="currentClosingDate"
        :header="$t('dates.currentClosingDate')"
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
