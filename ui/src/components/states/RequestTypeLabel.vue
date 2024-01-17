<template>
    <Tag :value="localizedState" :severity="tagColor" rounded />
</template>

<script lang="ts">
import Tag from "primevue/tag";
import { RequestType } from "@/utils/Enums";

const requestTypeColorMap: Record<keyof typeof RequestType, string> = {
    [RequestType.SINGLE]: "primary",
    [RequestType.SERIES]: "primary",
};

export default {
    components: {
        Tag,
    },
    props: {
        state: {
            type: String as () => keyof typeof RequestType,
            required: true,
        },
    },
    computed: {
        tagColor(): string {
            return requestTypeColorMap[this.state] || "secondary";
        },
        localizedState(): string {
            return this.$t(`requestType.${this.state}`);
        },
    },
};
</script>
