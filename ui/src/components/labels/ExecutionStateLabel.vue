<template>
    <LabelCommon
        :state="state"
        :colorMap="executionStateColorMap"
        localizationPath="enums.executionState."
    />
</template>

<script lang="ts">
import LabelCommon from "./LabelCommon.vue";
import { ExecutionState } from "@/utils/Enums";

/**
 * Displays a label with a color and text based on the execution state. Uses
 * LabelCommon component, mapping execution states to colors.
 */
export default {
    components: {
        LabelCommon,
    },
    props: {
        state: {
            type: String as () => keyof typeof ExecutionState,
            required: true,
        },
    },
    computed: {
        executionStateColorMap(): Record<keyof typeof ExecutionState, string> {
            return {
                [ExecutionState.PENDING]: "warning",
                [ExecutionState.PUBLISHED]: "success",
                [ExecutionState.CLOSED]: "danger",
                [ExecutionState.ARCHIVED]: "info",
            };
        },
    },
};
</script>
