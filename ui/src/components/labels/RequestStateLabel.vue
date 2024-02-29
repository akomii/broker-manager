<template>
    <LabelCommon
        :state="state"
        :colorMap="requestStateColorMap"
        localizationPath="enums.requestState."
    />
</template>

<script lang="ts">
import LabelCommon from "./LabelCommon.vue";
import { RequestState } from "@/utils/Enums";

/**
 * A Vue component that wraps the LabelCommon component to display a label for a
 * request's state. It passes the request state, a color map for the state, and
 * a localization path to the LabelCommon component.
 */
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
