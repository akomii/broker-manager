<template>
    <Column :header="$t('dates.currentClosingDate')" sortable>
        <template #body="slotProps">
            <div
                v-if="
                    slotProps.data.currentExecution &&
                    slotProps.data.currentExecution.scheduledClosingDate !== null
                "
            >
                <DateView
                    :date="slotProps.data.currentExecution.scheduledClosingDate"
                />
            </div>
            <NotAvailableIcon v-else />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import DateView from "@/components/timeWidgets/DateView.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";

// TODO refactor and add docs
export default {
    components: {
        Column,
        DateView,
        NotAvailableIcon,
    },
};
</script>
