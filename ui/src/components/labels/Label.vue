<template>
    <Tag :value="localizedState" :severity="tagColor" rounded />
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
        colorMap: {
            type: Object,
            required: true,
        },
        localizationPath: {
            type: String,
            required: true,
        },
    },
    computed: {
        tagColor(): string {
            return this.colorMap[this.state] || "secondary";
        },
        localizedState(): string {
            return this.$t(this.localizationPath + this.state);
        },
    },
};
</script>
