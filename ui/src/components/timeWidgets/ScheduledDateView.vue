<template>
    <span v-if="actualDate">{{ formattedActualDate }}</span>
    <div
        v-else
        v-tooltip.bottom="$t('scheduledDate', { dateLabel: tooltipLabel })"
    >
        <span class="text-gray-700">
            {{ formattedScheduledDate }}
            <i class="pi pi-calendar-times" />
        </span>
    </div>
</template>

<script lang="ts">
import MomentWrapper from "@/utils/MomentWrapper.ts";

/**
 * A Vue component that displays dates in a formatted manner using the German
 * locale. It supports displaying an actual date or a scheduled date with a
 * tooltip based on the provided props.
 */
export default {
    props: {
        /**
         * The scheduled date to display. Must be a valid Date object.
         */
        scheduledDate: {
            type: Date,
            required: true,
        },
        /**
         * The label for the tooltip displayed when only the scheduled date is
         * available.
         */
        tooltipLabel: {
            type: String,
            required: true,
        },
        /**
         * The actual date to display if available; otherwise, the scheduled
         * date is shown.
         */
        actualDate: {
            type: Object as () => Date | null | undefined,
            default: () => null,
        },
    },
    computed: {
        /**
         * Formats the scheduled date to the German locale.
         * @returns {string} The formatted scheduled date.
         */
        formattedScheduledDate(): string {
            return MomentWrapper.formatDateToGermanLocale(this.scheduledDate);
        },
        /**
         * Formats the actual date to the German locale, if available.
         * @returns {string} The formatted actual date.
         */
        formattedActualDate(): string {
            return this.actualDate
                ? MomentWrapper.formatDateToGermanLocale(this.actualDate)
                : "";
        },
    },
};
</script>
