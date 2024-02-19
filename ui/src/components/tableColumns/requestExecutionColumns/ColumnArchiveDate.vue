<template>
    <Column :header="$t('dates.archiveDate')" sortable>
        <template #body="slotProps">
            <ScheduledDateView
                :tooltipLabel="$t('dates.archiveDate')"
                :scheduledDate="slotProps.data.scheduledArchiveDate"
                :actualDate="slotProps.data.archivedDate"
            />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import ScheduledDateView from "@/components/timeWidgets/ScheduledDateView.vue";

export default {
    components: {
        Column,
        ScheduledDateView,
    },
};
</script>
