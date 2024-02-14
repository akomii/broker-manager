<template>
    <Column field="actions" bodyStyle="text-align: center">
        <template #body="slotProps">
            <DownloadButton
                :label="'TODO'"
                :tooltipLabel="$t('downloadCurrentResults')"
                :disabled="isDisabled(slotProps.data.executionState)"
                :action="dummyAction"
            />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import DownloadButton from "@/components/common/DownloadButton.vue";
import { ExecutionState } from "@/utils/Enums";

export default {
    components: {
        Column,
        DownloadButton,
    },
    methods: {
        isDisabled(state: ExecutionState): boolean {
            return [ExecutionState.ARCHIVED, ExecutionState.PENDING].includes(
                state
            );
        },
        dummyAction() {
            console.log("dummy action");
        },
    },
};
</script>
