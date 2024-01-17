<template>
    <DatePicker
        v-if="editable && !actualDate"
        :label="label"
        :date="scheduledDate"
        :editable="editable"
        @update:date="updateScheduledDate"
    />
    <div v-else>
        <div v-if="actualDate">
            {{ formattedActualDate }}
        </div>
        <div
            v-else-if="scheduledDate"
            v-tooltip.bottom="
                $t('dates.scheduledDate', { dateLabel: label })
            "
        >
            <span class="text-gray-700">
                {{ formattedScheduledDate }}
                <i class="pi pi-calendar-times" />
            </span>
        </div>
    </div>
</template>

<script lang="ts">
import DatePicker from "@/components/datePickers/DatePicker.vue";
import MomentWrapper from "@/utils/MomentWrapper.ts";

export default {
    components: {
        DatePicker,
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
            validator: function (value) {
                return (
                    value === null ||
                    value === undefined ||
                    value instanceof Date
                );
            },
        },
        editable: {
            type: Boolean,
            default: false,
        },
    },
    computed: {
        formattedActualDate(): string {
            return MomentWrapper.formatDateToGermanLocale(this.actualDate);
        },
        formattedScheduledDate(): string {
            return MomentWrapper.formatDateToGermanLocale(this.scheduledDate);
        },
    },
    methods: {
        updateScheduledDate(newDate: Date) {
            this.$emit("update:scheduledDate", newDate);
        },
    },
};
</script>
