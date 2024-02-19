<template>
    <Column :header="$t('dates.downloadDate')" sortable>
        <template #body="slotProps">
            <DateView :date="slotProps.data.date" />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import DateView from "@/components/datePickers/DateView.vue";

export default {
    components: {
        Column,
        DateView,
    },
};
</script>
