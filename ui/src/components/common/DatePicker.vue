<template>
    <div v-if="editable">
        <span class="p-float-label">
            <Calendar
                v-model="compDate"
                showIcon
                showButtonBar
                showTime
                hourFormat="24"
            />
            <label>{{ label }}</label>
        </span>
    </div>
    <div v-else>
        {{ formatDateToGermanLocale(date) }}
    </div>
</template>

<script lang="ts">
import Calendar from "primevue/calendar";
import MomentWrapper from "@/utils/MomentWrapper";

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
        },
        editable: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            compDate: this.date,
        };
    },
    watch: {
        compDate(newDate: Date) {
            this.$emit("update:date", newDate);
        },
    },
    methods: {
        formatDateToGermanLocale(date: Date): String {
            return MomentWrapper.formatDateToGermanLocale(date);
        },
    },
};
</script>
