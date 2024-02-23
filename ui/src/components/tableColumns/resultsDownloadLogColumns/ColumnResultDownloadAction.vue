<template>
    <Column bodyStyle="text-align: center">
        <template #body="slotProps">
            <DownloadButton
                :label="'TODO'"
                :tooltipLabel="tooltipLabel"
                :disabled="isDisabled(slotProps.data.executionState)"
                :action="action"
            />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import DownloadButton from "@/components/buttons/DownloadButton.vue";
import { ExecutionState } from "@/utils/Enums";

// TODO refactor and add docs
export default {
    components: {
        Column,
        DownloadButton,
    },
    props: {
        tooltipLabel: {
            type: String,
            required: true,
        },
        action: {
            type: Function,
            required: true,
        },
    },
    methods: {
        isDisabled(state: ExecutionState): boolean {
            return [ExecutionState.ARCHIVED, ExecutionState.PENDING].includes(
                state
            );
        },
    },
};
</script>
