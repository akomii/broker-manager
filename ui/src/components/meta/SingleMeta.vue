<template>
    <div>
        <Fieldset legend="Meta" :toggleable="true" class="flex w-auto m-2">
            <p>Veröffentlichungsdatum</p>
            <ScheduledDatePicker
                :label="'Veröffentlichungsdatum'"
                :actualDate="execution.publishedDate"
                :scheduledDate="execution.scheduledPublishDate"
                @update:scheduledDate="execution.scheduledPublishDate = $event"
                :editable="editable"
            />
            <p>Ausführungsdatum</p>
            <DatePicker
                :label="'Auführungsdatum'"
                :date="execution.executionDate"
                @update:date="execution.executionDate = $event"
                :editable="editable"
            />
            <p>Schließdatum</p>
            <ScheduledDatePicker
                :label="'Schließdatum'"
                :actualDate="execution.closedDate"
                :scheduledDate="execution.scheduledClosingDate"
                @update:scheduledDate="execution.scheduledClosingDate = $event"
                :editable="editable"
            />
            <p>Archivierungsdatum</p>
            <ScheduledDatePicker
                :label="'Archivierungsdatum'"
                :actualDate="execution.archivedDate"
                :scheduledDate="execution.scheduledArchiveDate"
                @update:scheduledDate="execution.scheduledArchiveDate = $event"
                :editable="editable"
            />
        </Fieldset>
    </div>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";

import { RequestExecution } from "@/utils/Types";
import ScheduledDatePicker from "@/components/common/ScheduledDatePicker.vue";
import DatePicker from "@/components/common/DatePicker.vue";

export default {
    components: {
        Fieldset,
        ScheduledDatePicker,
        DatePicker,
    },
    props: {
        execution: {
            type: Object as () => RequestExecution,
            required: true,
        },
        editable: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {};
    },
    watch: {},
    methods: {},
};
</script>
