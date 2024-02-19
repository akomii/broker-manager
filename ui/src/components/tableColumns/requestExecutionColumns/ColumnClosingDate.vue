<template>
    <Column :header="$t('dates.closingDate')" sortable>
        <template #body="slotProps">
            <ScheduledDateView
                :tooltipLabel="$t('dates.closingDate')"
                :scheduledDate="slotProps.data.scheduledClosingDate"
                :actualDate="slotProps.data.closedDate"
            />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import ScheduledDateView from "@/components/datePickers/ScheduledDateView.vue";

export default {
    components: {
        Column,
        ScheduledDateView,
    },
};
</script>
