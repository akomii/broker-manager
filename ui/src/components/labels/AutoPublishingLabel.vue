<template>
    <LabelCommon
        :state="publishType"
        :colorMap="publishTypeColorMap"
        localizationPath=""
    />
</template>

<script lang="ts">
import LabelCommon from "@/components/labels/LabelCommon.vue";

// TODO refactor and add docs
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
