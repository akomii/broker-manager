<template>
    <Tag :value="localizedState" :severity="tagColor" rounded />
</template>

<script lang="ts">
import Tag from "primevue/tag";

/**
 * A component that displays a localized and color-coded tag based on input
 * properties. It requires a `state` to determine the label, a `colorMap` to
 * determine the tag's color, and a `localizationPath` to localize the state's
 * value.
 */
export default {
    components: {
        Tag,
    },
    props: {
        /**
         * The state identifier used to determine the tag's label and color. It
         * is required to match a key in the `colorMap` for color assignment and
         * is appended to `localizationPath` for localization.
         */
        state: {
            type: String,
            required: true,
        },
        /**
         * A map of state values to their corresponding color strings.
         * This object is used to determine the color of the tag based on its
         * current state.
         */
        colorMap: {
            type: Object as () => Record<string, string>,
            required: true,
        },
        /**
         * The base path for localization keys used to localize the tag's label.
         * The `state` value is appended to this path to form the complete
         * localization key.
         */
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
