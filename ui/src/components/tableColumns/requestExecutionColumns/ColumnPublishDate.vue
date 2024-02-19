<template>
    <Column :header="$t('dates.publishDate')" sortable>
        <template #body="slotProps">
            <ScheduledDateView
                :tooltipLabel="$t('dates.publishDate')"
                :scheduledDate="slotProps.data.scheduledPublishDate"
                :actualDate="slotProps.data.publishedDate"
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
