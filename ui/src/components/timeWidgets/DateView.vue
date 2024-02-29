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
         * Computes the formatted date string using the MomentWrapper utility.
         * Formats the `date` prop to the German locale date format.
         *
         * @returns {string} The formatted date string.
         */
        formattedDate(): string {
            return MomentWrapper.formatDateToGermanLocale(this.date);
        },
    },
};
</script>
