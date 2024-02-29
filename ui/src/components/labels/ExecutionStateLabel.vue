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
 * A Vue component designed to display execution state labels with corresponding
 * color coding. It leverages the LabelCommon component, passing the execution
 * state, a predefined color map based on the execution state, and a
 * localization path for internationalization.
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
