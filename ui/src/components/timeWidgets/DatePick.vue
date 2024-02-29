<template>
    <BlockUI :blocked="disabled">
        <FloatLabel v-if="label">
            <Calendar
                v-model="dummyDate"
                showButtonBar
                showTime
                hourFormat="24"
            />
            <label>{{ label }}</label>
        </FloatLabel>
        <Calendar
            v-else
            v-model="dummyDate"
            showButtonBar
            showTime
            hourFormat="24"
        />
    </BlockUI>
</template>

<script lang="ts">
import BlockUI from "primevue/blockui";
import FloatLabel from "primevue/floatlabel";
import Calendar from "primevue/calendar";

/**
 * A Vue component that provides a date selection interface with optional
 * floating label. It can be disabled, and it communicates the selected date
 * through an event. Uses PrimeVue's BlockUI to optionally block interaction,
 * FloatLabel for enhanced UI, and Calendar for date and time selection.
 */
export default {
    components: {
        BlockUI,
        FloatLabel,
        Calendar,
    },
    props: {
        /**
         * The initial date to be displayed by the calendar.
         */
        date: {
            type: Object as () => Date | null | undefined,
            default: () => null,
        },
        /**
         * Optional label for the calendar. If provided, the FloatLabel
         * component is used.
         */
        label: String,
        /**
         * Controls whether the component is disabled (i.e., user interaction is
         * blocked).
         */
        disabled: Boolean,
    },
    emits: ["update:date"],
    data() {
        return {
            dummyDate: this.date,
        };
    },
    watch: {
        /**
         * Watches the internal model date for changes and emits an update event
         * to the parent component, facilitating two-way binding.
         * @param {Date} date - The newly selected date.
         */
        dummyDate(date: Date) {
            this.$emit("update:date", date);
        },
    },
};
</script>
