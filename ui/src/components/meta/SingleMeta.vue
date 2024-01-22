<template>
    <Fieldset legend="Meta" :toggleable="true">
        <!-- TODO REFACTOR THE HTML -->
        <div class="flex justify-content-center">
            <div class="w-6">
                <div class="flex-column">
                    <div class="flex flex-wrap align-items-center">
                        <span class="w-5 flex align-items-center gap-2">
                            {{ $t("enums.requestType.label") }}
                            <Button
                                icon="pi pi-prime"
                                class="text-sm"
                                outlined
                            />
                            </span                        >
                        <span class="w-6">
                            <RequestTypeLabel
                                class="text-sm"
                                :state="RequestType.SINGLE"
                            />
                        </span>
                    </div>
                    <div class="flex flex-wrap align-items-center">
                        <span class="w-5">{{ $t("externalId") }}</span>
                        <span class="w-6">
                            <p v-if="execution.externalId">
                                {{ execution.externalId }}
                            </p>
                            <i v-else class="pi pi-minus" />
                        </span>
                    </div>
                    <div class="flex flex-wrap">
                        <p v-if="!editable" class="my-0">
                            {{ $t("dates.referenceDate.start") }}
                        </p>
                        <DatePicker
                            class="mb-3"
                            :label="$t('dates.referenceDate.start')"
                            :date="dummyReferenceStart"
                            :editable="editable"
                            @update:date="dummyReferenceStart = $event"
                        />
                    </div>
                    <div class="flex flex-wrap">
                        <p v-if="!editable" class="my-0">
                            {{ $t("dates.referenceDate.end") }}
                        </p>
                        <DatePicker
                            class="mb-3"
                            :label="$t('dates.referenceDate.end')"
                            :date="execution.referenceDate"
                            :editable="editable"
                            @update:date="execution.referenceDate = $event"
                        />
                    </div>
                    <div class="flex flex-wrap">
                        <span class="w-12 border-solid">
                            TODO BUTTON FÜR ÄNDERUNGSHISTORIE
                        </span>
                    </div>
                </div>
            </div>

            <Divider layout="vertical" />

            <div class="w-6">
                <div class="flex-column">
                    <div class="flex flex-wrap align-items-center mb-3">
                        <span class="min-w-4 max-w-6">{{
                            $t("enums.executionState.label")
                        }}</span>
                        <span class="min-w-6 max-w-8">
                            <ExecutionStateLabel
                                v-if="execution.executionState"
                                class="text-sm"
                                :state="execution.executionState"
                            />
                            <i v-else class="pi pi-minus" />
                        </span>
                    </div>
                    <div class="flex flex-wrap">
                        <p
                            v-if="!editable || execution.publishedDate"
                            class="my-0 mr-4"
                        >
                            {{ $t("dates.publishDate") }}
                        </p>
                        <ScheduledDatePicker
                            class="mb-3"
                            :label="$t('dates.publishDate')"
                            :actualDate="execution.publishedDate"
                            :scheduledDate="execution.scheduledPublishDate"
                            :editable="editable"
                            @update:scheduledDate="
                                execution.scheduledPublishDate = $event
                            "
                        />
                    </div>

                    <div class="flex flex-wrap">
                        <p
                            v-if="!editable || execution.closedDate"
                            class="my-0 mr-4"
                        >
                            {{ $t("dates.closingDate") }}
                        </p>
                        <ScheduledDatePicker
                            class="mb-3"
                            :label="$t('dates.closingDate')"
                            :actualDate="execution.closedDate"
                            :scheduledDate="execution.scheduledClosingDate"
                            :editable="editable"
                            @update:scheduledDate="
                                execution.scheduledClosingDate = $event
                            "
                        />
                    </div>

                    <div class="flex flex-wrap">
                        <p
                            v-if="!editable || execution.archivedDate"
                            class="my-0 mr-4"
                        >
                            {{ $t("dates.archiveDate") }}
                        </p>
                        <ScheduledDatePicker
                            class="mb-3"
                            :label="$t('dates.archiveDate')"
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
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import Divider from "primevue/divider";
import Button from "primevue/button";
import ScheduledDatePicker from "@/components/datePickers/ScheduledDatePicker.vue";
import DatePicker from "@/components/datePickers/DatePicker.vue";
import RequestTypeLabel from "@/components/states/RequestTypeLabel.vue";
import ExecutionStateLabel from "@/components/states/ExecutionStateLabel.vue";
import { RequestExecution, SingleExecution } from "@/utils/Types";
import { RequestType } from "@/utils/Enums.ts";
import MomentWrapper from "@/utils/MomentWrapper";

export default {
    components: {
        Fieldset,
        Divider,
        Button,
        ScheduledDatePicker,
        DatePicker,
        RequestTypeLabel,
        ExecutionStateLabel,
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
