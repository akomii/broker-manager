<template>
    <LabelCommon
        :state="type"
        :colorMap="requestTypeColorMap"
        localizationPath="enums.requestType."
    />
</template>

<script lang="ts">
import LabelCommon from "./LabelCommon.vue";
import { RequestType } from "@/utils/Enums";

/**
 * Displays a label for a request type with a corresponding color. It utilizes
 * the `LabelCommon` component, applying dynamic styling based on the
 * `RequestType` enum.
 */
export default {
    components: {
        LabelCommon,
    },
    props: {
        type: {
            type: String as () => keyof typeof RequestType,
            required: true,
        },
    },
    computed: {
        requestTypeColorMap(): Record<keyof typeof RequestType, string> {
            return {
                [RequestType.SINGLE]: "primary",
                [RequestType.SERIES]: "primary",
            };
        },
    },
};
</script>
