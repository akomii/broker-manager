<template>
    <div v-if="editable">
        <span class="p-float-label">
            <Calendar
                v-model="dummyDate"
                showIcon
                showButtonBar
                showTime
                hourFormat="24"
            />
            <label>{{ label }}</label>
        </span>
    </div>
    <div v-else>
        {{ formattedDate }}
    </div>
</template>

<script lang="ts">
import Calendar from "primevue/calendar";
import MomentWrapper from "@/utils/MomentWrapper.ts";

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
        editable: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            dummyDate: this.date,
        };
    },
    computed: {
        formattedDate(): string {
            return MomentWrapper.formatDateToGermanLocale(this.date);
        },
    },
    watch: {
        dummyDate(newDate: Date) {
            this.$emit("update:date", newDate);
        },
    },
};
</script>
