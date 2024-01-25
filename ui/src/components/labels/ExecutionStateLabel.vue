<template>
    <Label
        :state="state"
        :colorMap="executionStateColorMap"
        localizationPath="enums.executionState."
    />
</template>

<script lang="ts">
import Label from "./Label.vue";
import { ExecutionState } from "@/utils/Enums";

export default {
    components: {
        Label,
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
