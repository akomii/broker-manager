<template>
    <div>
        <Fieldset legend="Meta" :toggleable="true" class="flex w-auto m-2">
            <p>Typ</p>
            <EnumState
                class="text-sm"
                :state="RequestType.SINGLE"
                :stateColorMap="requestTypeColorMap"
            />

            <p>Externe ID</p>
            <p>{{ execution.externalId }}</p>

            <p>Ausführungsstatus</p>
            <EnumState
                class="text-sm"
                :state="execution.executionState"
                :stateColorMap="requestStateColorMap"
            />

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

            <p>Referenzzeitraum</p>
            <DatePicker
                :label="'Referenzdatum Start'"
                :date="dummyDate"
                @update:date="dummyDate = $event"
                :editable="editable"
            />
            <br />
            <DatePicker
                :label="'Referenzdatum Ende'"
                :date="execution.referenceDate"
                @update:date="execution.referenceDate = $event"
                :editable="editable"
            />
        </Fieldset>
    </div>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";

import { RequestExecution, SingleExecution } from "@/utils/Types";
import ScheduledDatePicker from "@/components/datePickers/ScheduledDatePicker.vue";
import DatePicker from "@/components/datePickers/DatePicker.vue";
import MomentWrapper from "@/utils/MomentWrapper";
import EnumState from "@/components/states/EnumState.vue";
import {
    requestTypeColorMap,
    requestStateColorMap,
} from "@/utils/Constants.ts";
import { RequestType } from "@/utils/Enums.ts";

export default {
    components: {
        Fieldset,
        ScheduledDatePicker,
        DatePicker,
        EnumState,
    },
    props: {
        execution: {
            type: Object as () => RequestExecution,
            required: true,
        },
        querySchedule: {
            type: Object as () => SingleExecution,
            required: true,
        },
        editable: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            RequestType,
            requestTypeColorMap,
            requestStateColorMap,
            dummyDate: new Date(),
        };
    },
    computed: {
        duration() {
            return MomentWrapper.computePeriod(
                this.dummyDate,
                this.execution.referenceDate
            );
        },
    },
    watch: {
        "execution.scheduledPublishDate": function () {
            this.$emit("update:execution", this.execution);
        },
        "execution.executionDate": function () {
            this.$emit("update:execution", this.execution);
        },
        "execution.scheduledClosingDate": function () {
            this.$emit("update:execution", this.execution);
        },
        "execution.scheduledArchiveDate": function () {
            this.$emit("update:execution", this.execution);
        },
        "execution.referenceDate": function () {
            this.$emit("update:execution", this.execution);
        },
        duration(newVal) {
            this.$emit("update:querySchedule", newVal);
        },
    },
};
</script>
