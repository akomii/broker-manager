<template>
    <span class="p-float-label">
        <Calendar
            v-model="dummyDate"
            showIcon
            showButtonBar
            showTime
            hourFormat="24"
            :disabled="disabled"
        />
        <label>{{ label }}</label>
    </span>
</template>

<script lang="ts">
import Calendar from "primevue/calendar";

export default {
    components: {
        Calendar,
    },
    props: {
        label: {
            type: String,
            required: true,
        },
        date: {
            type: Date,
            required: true,
        },
        disabled: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            dummyDate: this.date,
        };
    },
    watch: {
        dummyDate(newDate: Date) {
            this.$emit("update:date", newDate);
        },
    },
};
</script>
