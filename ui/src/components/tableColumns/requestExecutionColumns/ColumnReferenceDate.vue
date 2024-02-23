<template>
    <Column :header="$t('dates.referenceDate')" sortable>
        <template #body="slotProps">
            <DateView :date="slotProps.data.referenceDate" />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import DateView from "@/components/timeWidgets/DateView.vue";

// TODO refactor and add docs
export default {
    components: {
        Column,
        DateView,
    },
};
</script>
