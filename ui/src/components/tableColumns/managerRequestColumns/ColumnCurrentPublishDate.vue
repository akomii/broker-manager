<template>
    <Column :header="$t('dates.currentPublishDate')" sortable>
        <template #body="slotProps">
            <div
                v-if="
                    getPublishedDateOfMostCurrentExecution(
                        slotProps.data.executions
                    ) !== null
                "
            >
                <DateView
                    :date="
                        getPublishedDateOfMostCurrentExecution(
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
        getPublishedDateOfMostCurrentExecution(
            executions: RequestExecution[]
        ): Date | null {
            return executions.reduce((acc: Date | null, cur) => {
                if (
                    cur.executionState !== ExecutionState.PUBLISHED ||
                    cur.publishedDate === null
                ) {
                    return acc;
                }
                const curDate = new Date(cur.publishedDate);
                return !acc || curDate > acc ? curDate : acc;
            }, null);
        },
    },
};
</script>
