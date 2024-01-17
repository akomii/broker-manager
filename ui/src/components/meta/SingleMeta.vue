<template>
    <Fieldset legend="Meta" :toggleable="true" class="m-2">
        <div class="flex flex-wrap justify-content-center">
            <div class="grid nested-grid w-6">
                <div class="col-12">
                    <div class="grid align-items-center">
                        <div class="col-4">
                            <p>{{ $t("meta.requestType") }}</p>
                        </div>
                        <div class="col-8">
                            <RequestTypeLabel
                                class="text-sm"
                                :state="RequestType.SINGLE"
                            />
                        </div>
                    </div>
                </div>
                <div class="col-12">
                    <div class="grid align-items-center">
                        <div class="col-4">
                            <p>{{ $t("meta.externalId") }}</p>
                        </div>
                        <div class="col-8">
                            <p v-if="execution.externalId">
                                {{ execution.externalId }}
                            </p>
                            <i v-else class="pi pi-minus" />
                        </div>
                    </div>
                </div>
                <div class="col-12">
                    <div class="grid align-items-center">
                        <div class="col-4">
                            <p>{{ $t("meta.referenceDate.start") }}</p>
                        </div>
                        <div class="col-8">
                            <DatePicker
                                :label="$t('meta.referenceDate.start')"
                                :date="dummyReferenceStart"
                                :editable="editable"
                                @update:date="dummyReferenceStart = $event"
                            />
                        </div>
                    </div>
                </div>
                <div class="col-12">
                    <div class="grid align-items-center">
                        <div class="col-4">
                            <p>{{ $t("meta.referenceDate.end") }}</p>
                        </div>
                        <div class="col-8">
                            <DatePicker
                                :label="$t('meta.referenceDate.end')"
                                :date="execution.referenceDate"
                                :editable="editable"
                                @update:date="execution.referenceDate = $event"
                            />
                        </div>
                    </div>
                </div>
                <div class="col-12">
                    <div class="grid align-items-center">
                        <p>TODO BUTTON FÜR ÄNDERUNGSHISTORIE</p>
                    </div>
                </div>
            </div>

            <Divider layout="vertical" />

            <div class="grid nested-grid w-6">
                <div class="col-12">
                    <div class="grid align-items-center">
                        <div class="col-4">
                            <p>{{ $t("meta.executionState") }}</p>
                        </div>
                        <div class="col-8">
                            <ExecutionStateLabel
                                v-if="execution.executionState"
                                class="text-sm"
                                :state="execution.executionState"
                            />
                            <i v-else class="pi pi-minus" />
                        </div>
                    </div>
                </div>
                <div class="col-12">
                    <div class="grid align-items-center">
                        <div class="col-4">
                            <p>{{ $t("meta.publishDate") }}</p>
                        </div>
                        <div class="col-8">
                            <ScheduledDatePicker
                                :label="$t('meta.publishDate')"
                                :actualDate="execution.publishedDate"
                                :scheduledDate="execution.scheduledPublishDate"
                                :editable="editable"
                                @update:scheduledDate="
                                    execution.scheduledPublishDate = $event
                                "
                            />
                        </div>
                    </div>
                </div>
                <div class="col-12">
                    <div class="grid align-items-center">
                        <div class="col-4">
                            <p>{{ $t("meta.executionDate") }}</p>
                        </div>
                        <div class="col-8">
                            <DatePicker
                                :label="$t('meta.executionDate')"
                                :date="execution.executionDate"
                                :editable="editable"
                                @update:date="execution.executionDate = $event"
                            />
                        </div>
                    </div>
                </div>
                <div class="col-12">
                    <div class="grid align-items-center">
                        <div class="col-4">
                            <p>{{ $t("meta.closingDate") }}</p>
                        </div>
                        <div class="col-8">
                            <ScheduledDatePicker
                                :label="$t('meta.closingDate')"
                                :actualDate="execution.closedDate"
                                :scheduledDate="execution.scheduledClosingDate"
                                :editable="editable"
                                @update:scheduledDate="
                                    execution.scheduledClosingDate = $event
                                "
                            />
                        </div>
                    </div>
                </div>
                <div class="col-12">
                    <div class="grid align-items-center">
                        <div class="col-4">
                            <p>{{ $t("meta.archiveDate") }}</p>
                        </div>
                        <div class="col-8">
                            <ScheduledDatePicker
                                :label="$t('meta.archiveDate')"
                                :actualDate="execution.archivedDate"
                                :scheduledDate="execution.scheduledArchiveDate"
                                :editable="editable"
                                @update:scheduledDate="
                                    execution.scheduledArchiveDate = $event
                                "
                            />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import Divider from "primevue/divider";
import ScheduledDatePicker from "@/components/datePickers/ScheduledDatePicker.vue";
import DatePicker from "@/components/datePickers/DatePicker.vue";
import RequestTypeLabel from "@/components/states/RequestTypeLabel.vue";
import ExecutionStateLabel from "../states/ExecutionStateLabel.vue";
import { RequestExecution, SingleExecution } from "@/utils/Types";
import { RequestType } from "@/utils/Enums.ts";
import MomentWrapper from "@/utils/MomentWrapper";

export default {
    components: {
        Fieldset,
        ScheduledDatePicker,
        DatePicker,
        RequestTypeLabel,
        ExecutionStateLabel,
        Divider,
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
            dummyReferenceStart: MomentWrapper.addDurationToDate(
                this.execution.referenceDate,
                this.querySchedule.duration
            ),
        };
    },
    computed: {
        duration() {
            return MomentWrapper.computePeriod(
                this.dummyReferenceStart,
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
            this.querySchedule.duration = newVal;
            this.$emit("update:querySchedule", this.querySchedule);
        },
    },
};
</script>
