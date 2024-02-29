<template>
    <Tag :value="localizedState" :severity="tagColor" rounded />
</template>

<script lang="ts">
import Tag from "primevue/tag";

/**
 * A Vue component that displays a localized and color-coded tag based on the
 * provided state. The state's visual representation is customized through a
 * color map and localized using a specified path for internationalization.
 */
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
            type: Object as () => Record<string, string>,
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
