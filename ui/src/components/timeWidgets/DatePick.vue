<template>
    <span v-if="label" class="p-float-label">
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
    <Calendar
        v-else
        v-model="dummyDate"
        showIcon
        showButtonBar
        showTime
        hourFormat="24"
        :disabled="disabled"
    />
</template>

<script lang="ts">
import Calendar from "primevue/calendar";

export default {
    components: {
        Calendar,
    },
    props: {
        date: {
            type: Date,
            required: true,
        },
        label: {
            type: String,
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
