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
import DateView from "@/components/datePickers/DateView.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";

export default {
    components: {
        Column,
        DateView,
        NotAvailableIcon,
    },
};
</script>
