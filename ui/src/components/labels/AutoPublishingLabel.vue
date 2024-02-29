<template>
    <LabelCommon
        :state="publishType"
        :colorMap="publishTypeColorMap"
        localizationPath=""
    />
</template>

<script lang="ts">
import LabelCommon from "./LabelCommon.vue";

/**
 * A Vue component that utilizes the LabelCommon component to display a label
 * indicating the type of publishing: automatic or manual. It dynamically
 * determines the label based on the `isAutoPublishing` prop and applies color
 * coding through a predefined map.
 */
export default {
    components: {
        LabelCommon,
    },
    props: {
        isAutoPublishing: {
            type: Boolean,
            required: true,
        },
    },
    computed: {
        publishType(): string {
            return this.isAutoPublishing ? "automatic" : "manual";
        },
        publishTypeColorMap(): Record<string, string> {
            return {
                ["automatic"]: "success",
                ["manual"]: "danger",
            };
        },
    },
};
</script>
