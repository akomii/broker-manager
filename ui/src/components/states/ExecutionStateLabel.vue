<template>
    <Tag :value="localizedState" :severity="tagColor" rounded />
</template>

<script lang="ts">
import Tag from "primevue/tag";
import { ExecutionState } from "@/utils/Enums";

export const executionStateColorMap = {
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
        tagColor() {
            return executionStateColorMap[this.state] || "secondary";
        },
        localizedState() {
            return this.$t(`executionState.${this.state}`);
        },
    },
};
</script>
