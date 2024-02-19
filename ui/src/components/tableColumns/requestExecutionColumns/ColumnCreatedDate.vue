<template>
    <Column field="createdDate" :header="$t('dates.createdDate')" sortable>
        <template #body="slotProps">
            <DateView :date="slotProps.data.createdDate" />
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
