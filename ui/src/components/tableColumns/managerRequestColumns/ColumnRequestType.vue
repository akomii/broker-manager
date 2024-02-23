<template>
    <Column
        :header="$t('enums.requestType.label')"
        sortable
    >
        <template #body="slotProps">
            <RequestTypeLabel :state="slotProps.data.requestType" />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import RequestTypeLabel from "@/components/labels/RequestTypeLabel.vue";

// TODO refactor and add docs
export default {
    components: {
        Column,
        RequestTypeLabel,
    },
};
</script>
