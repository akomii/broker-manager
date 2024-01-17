<template>
    <Tag :value="localizedState" :severity="tagColor" rounded />
</template>

<script lang="ts">
import Tag from "primevue/tag";
import { RequestState } from "@/utils/Enums";

const requestStateColorMap: Record<keyof typeof RequestState, string> = {
    [RequestState.DRAFT]: "warning",
    [RequestState.ONLINE]: "success",
    [RequestState.CLOSED]: "danger",
    [RequestState.ARCHIVED]: "info",
};

export default {
    components: {
        Tag,
    },
    props: {
        state: {
            type: String as () => keyof typeof RequestState,
            required: true,
        },
    },
    computed: {
        tagColor(): string {
            return requestStateColorMap[this.state] || "secondary";
        },
        localizedState(): string {
            return this.$t(`requestState.${this.state}`);
        },
    },
};
</script>
