<template>
    <Tag :value="localizedState" :severity="tagColor" rounded />
</template>

<script lang="ts">
import Tag from "primevue/tag";
import { ExecutionState } from "@/utils/Enums";

export const executionStateColorMap: Record<keyof typeof ExecutionState, string> = {
    [ExecutionState.PENDING]: "warning",
    [ExecutionState.PUBLISHED]: "success",
    [ExecutionState.CLOSED]: "danger",
    [ExecutionState.ARCHIVED]: "info",
};

export default {
    components: {
        Tag,
    },
    props: {
        state: {
            type: String as () => keyof typeof ExecutionState,
            required: true,
        },
    },
    computed: {
        tagColor(): string {
            return executionStateColorMap[this.state] || "secondary";
        },

        localizedState(): string {
            return this.$t(`enums.executionState.${this.state}`);
        },
    },
};
</script>
