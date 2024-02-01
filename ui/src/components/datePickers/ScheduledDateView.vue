<template>
    <DateView v-if="actualDate" :date="actualDate" />
    <div
        v-else
        v-tooltip.bottom="
            $t('dates.scheduledDate', { dateLabel: tooltipLabel })
        "
    >
        <span class="text-gray-700">
            {{ formattedScheduledDate }}
            <i class="pi pi-calendar-times" />
        </span>
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
        tooltipLabel: {
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
