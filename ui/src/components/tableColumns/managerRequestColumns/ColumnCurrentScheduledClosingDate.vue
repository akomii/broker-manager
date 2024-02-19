<template>
    <Column :header="$t('dates.currentClosingDate')" sortable>
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
            const latestExecution = executions.reduce(
                (acc: RequestExecution | null, cur: RequestExecution) => {
                    if (
                        cur.executionState !== ExecutionState.PUBLISHED ||
                        cur.publishedDate === null
                    ) {
                        return acc;
                    }
                    const curDate = new Date(cur.publishedDate);
                    const accDate = acc
                        ? new Date(acc.publishedDate as Date)
                        : null;
                    return !acc || curDate > accDate ? cur : acc;
                },
                null
            );
            return latestExecution
                ? new Date(latestExecution.scheduledClosingDate)
                : null;
        },
    },
};
</script>
