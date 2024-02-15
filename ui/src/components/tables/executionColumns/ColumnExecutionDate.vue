<template>
    <Column field="executionDate" :header="$t('dates.executionDate')" sortable>
        <template #body="slotProps">
            <DateView :date="slotProps.data.executionDate" />
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
