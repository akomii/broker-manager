<template>
    <LabelCommon
        :state="state"
        :colorMap="requestStateColorMap"
        localizationPath="enums.requestState."
    />
</template>

<script lang="ts">
import LabelCommon from "@/components/labels/LabelCommon.vue";
import { RequestState } from "@/utils/Enums";

export default {
    components: {
        LabelCommon,
    },
    props: {
        state: {
            type: String as () => keyof typeof RequestState,
            required: true,
        },
    },
    computed: {
        requestStateColorMap(): Record<keyof typeof RequestState, string> {
            return {
                [RequestState.DRAFT]: "warning",
                [RequestState.ONLINE]: "success",
                [RequestState.CLOSED]: "danger",
                [RequestState.ARCHIVED]: "info",
            };
        },
    },
};
</script>
