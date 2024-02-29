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
 * A Vue component for displaying request type labels with color coding,
 * utilizing the LabelCommon component. It takes a request type as a prop,
 * categorized by the RequestType enum, and uses a color map to assign colors to
 * different request types. The component also specifies a localization path for
 * internationalizing the request type labels, ensuring they are displayed
 * according to the application's current language settings.
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
