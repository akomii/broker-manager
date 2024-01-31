<template>
    <DateView v-if="actualDate" :label="label" :date="actualDate" />
    <div
        v-else
        v-tooltip.bottom="$t('dates.scheduledDate', { dateLabel: label })"
    >
        <p class="my-0">
            {{ label }}
        </p>
        <p class="my-0 text-gray-700">
            {{ formattedScheduledDate }}
            <i class="pi pi-calendar-times" />
        </p>
    </div>
</template>

<script lang="ts">
import MomentWrapper from "@/utils/MomentWrapper.ts";
import DateView from "./DateView.vue";

export default {
    components: {
        DateView,
    },
    props: {
        label: {
            type: String,
            required: true,
        },
        scheduledDate: {
            type: Date,
            required: true,
        },
        actualDate: {
            type: Date,
            default: undefined,
            validator: function (value) {
                return value === undefined || value instanceof Date;
            },
        },
    },
    computed: {
        formattedScheduledDate(): string {
            return MomentWrapper.formatDateToGermanLocale(this.scheduledDate);
        },
    },
};
</script>
