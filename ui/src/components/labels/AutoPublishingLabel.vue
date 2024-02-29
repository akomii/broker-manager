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
 * Wraps the LabelCommon component to display a label indicating the publishing
 * type. The label's color and text are determined by `isAutoPublishing` prop.
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
