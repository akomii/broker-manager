<template>
    {{ formattedDate }}
</template>

<script lang="ts">
import MomentWrapper from "@/utils/MomentWrapper.ts";

/**
 * A Vue component that displays a date formatted in the German locale format.
 * It utilizes the MomentWrapper utility for date formatting.
 */
export default {
    props: {
        /**
         * The date to be formatted and displayed. Must be a valid Date object.
         */
        date: {
            type: Date,
            required: true,
        },
    },
    computed: {
        /**
         * Computed property that formats the provided date into
         * the German locale format.
         * @returns {string} The date formatted in the German locale.
         */
        formattedDate(): string {
            return MomentWrapper.formatDateToGermanLocale(this.date);
        },
    },
};
</script>
