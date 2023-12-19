<template>
    <Tag :value="state" :severity="tagColor" rounded />
</template>

<script lang="ts">
import Tag from "primevue/tag";

export default {
    components: {
        Tag,
    },
    props: {
        state: {
            type: String,
            required: true,
        },
        stateColorMap: {
            type: Object as () => Record<string, string>,
            required: true,
        },
    },
    computed: {
        tagColor() {
            return (
                this.stateColorMap[
                    this.state as keyof typeof this.stateColorMap
                ] || "secondary"
            );
        },
    },
};
</script>
