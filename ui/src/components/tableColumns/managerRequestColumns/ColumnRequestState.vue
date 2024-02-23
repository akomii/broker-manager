<template>
    <Column :header="$t('enums.requestState.label')" sortable>
        <template #body="slotProps">
            <RequestStateLabel :state="slotProps.data.requestState" />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import RequestStateLabel from "@/components/labels/RequestStateLabel.vue";

// TODO refactor and add docs
export default {
    components: {
        Column,
        RequestStateLabel,
    },
};
</script>
