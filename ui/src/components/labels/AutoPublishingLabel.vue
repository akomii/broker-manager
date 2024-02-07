<template>
    <Label
        :state="publishType"
        :colorMap="publishTypeColorMap"
        localizationPath=""
    />
</template>

<script lang="ts">
import Label from "./Label.vue";

export default {
    components: {
        Label,
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
