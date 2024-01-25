<template>
    <Label
        :state="state"
        :colorMap="requestTypeColorMap"
        localizationPath="enums.requestType."
    />
</template>

<script lang="ts">
import Label from "./Label.vue";
import { RequestType } from "@/utils/Enums";

export default {
    components: {
        Label,
    },
    props: {
        state: {
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
