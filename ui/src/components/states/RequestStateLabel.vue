<template>
    <Tag :value="localizedState" :severity="tagColor" rounded />
</template>

<script lang="ts">
import Tag from "primevue/tag";
import { RequestState } from "@/utils/Enums";

const requestStateColorMap = {
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
        tagColor() {
            return requestStateColorMap[this.state] || "secondary";
        },
        localizedState() {
            return this.$t(`requestState.${this.state}`);
        },
    },
};
</script>
