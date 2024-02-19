<template>
    <Column
        :header="$t('enums.executionState.label')"
        sortable
    >
        <template #body="slotProps">
            <ExecutionStateLabel :state="slotProps.data.executionState" />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import ExecutionStateLabel from "@/components/labels/ExecutionStateLabel.vue";

export default {
    components: {
        Column,
        ExecutionStateLabel,
    },
};
</script>
