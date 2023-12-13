<template>
    <div v-if="editable && !actualDate">
        <span class="p-float-label">
            <Calendar v-model="localPlannedDate" showIcon showButtonBar showTime hourFormat="24" />
            <label>{{ label }}</label>
        </span>
    </div>
    <div v-else>
        <div v-if="actualDate">
            {{ formatToLocalDate(actualDate) }}
        </div>
        <div v-else-if="plannedDate" v-tooltip.bottom="`Geplantes ${label}`">
            <span class="text-gray-700">
                {{ formatToLocalDate(plannedDate) }}
                <i class="pi pi-calendar-times" />
            </span>
        </div>
    </div>
</template>

<script lang="ts">
import Calendar from 'primevue/calendar';
import { DateFormatter } from '@/utils/DateFormatter.ts';

export default {
    components: {
        Calendar,
    },
    props: {
        label: {
            type: String,
            required: true
        },
        plannedDate: {
            type: Date,
        },
        actualDate: {
            type: Date,
        },
        editable: {
            type: Boolean,
            default: false,
        }
    },
    data() {
        return {
            localPlannedDate: this.plannedDate
        };
    },
    watch: {
        localPlannedDate(newDate: Date) {
            this.$emit('update:plannedDate', newDate);
        }
    },
    methods: {
        formatToLocalDate(date: Date): String {
            return DateFormatter.getInstance().formatToLocalDate(date);
        }
    }
};
</script>