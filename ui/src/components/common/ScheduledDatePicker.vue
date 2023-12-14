<template>
    <div v-if="editable && !actualDate">
        <span class="p-float-label">
            <Calendar v-model="compScheduledDate" showIcon showButtonBar showTime hourFormat="24" />
            <label>{{ label }}</label>
        </span>
    </div>
    <div v-else>
        <div v-if="actualDate">
            {{ formatDateToGermanLocale(actualDate) }}
        </div>
        <div v-else-if="scheduledDate" v-tooltip.bottom="`Geplantes ${label}`">
            <span class="text-gray-700">
                {{ formatDateToGermanLocale(scheduledDate) }}
                <i class="pi pi-calendar-times" />
            </span>
        </div>
    </div>
</template>

<script lang="ts">
import Calendar from 'primevue/calendar';
import MomentWrapper from '@/utils/MomentWrapper';

export default {
    components: {
        Calendar,
    },
    props: {
        label: {
            type: String,
            required: true
        },
        scheduledDate: {
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
            compScheduledDate: this.scheduledDate
        };
    },
    watch: {
        compScheduledDate(newDate: Date) {
            this.$emit('update:scheduledDate', newDate);
        }
    },
    methods: {
        formatDateToGermanLocale(date: Date): String {
            return MomentWrapper.formatDateToGermanLocale(date);
        }
    }
};
</script>