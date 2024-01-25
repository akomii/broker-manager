<template>
    <!-- TODO add editing conditions -->
    <Fieldset :legend="$t('meta')">
        <!-- Request Type-->
        <div class="field grid mb-1">
            <div class="col-3 flex align-items-center text-lg">
                {{ $t("enums.requestType.label") }}
            </div>
            <div class="col-9 flex align-items-center gap-2">
                <RequestTypeLabel class="text-sm" :state="RequestType.SINGLE" />
                <ConvertRequestButton v-if="editable" />
            </div>
        </div>
        <!-- External ID and request state -->
        <div class="field grid mb-1" style="min-height: 40px">
            <div class="col-3 flex align-items-center text-lg">
                {{ $t("enums.executionState.label") }}
            </div>
            <div class="col-3 flex align-items-center gap-2">
                <ExecutionStateLabel
                    v-if="execution.executionState"
                    class="text-sm"
                    :state="execution.executionState"
                />
                <i v-else class="pi pi-minus text-lg" />
            </div>
            <div class="col-3 flex align-items-center text-lg">
                {{ $t("externalId") }}
            </div>
            <div class="col-3 flex align-items-center gap-2">
                <p v-if="execution.externalId">{{ execution.externalId }}</p>
                <i v-else class="pi pi-minus text-lg" />
            </div>
        </div>

        <!-- Lower (date) elements -->
        <div class="flex justify-content-center column-gap-3">
            <div class="w-6 mt-3">
                <div class="grid row-gap-3">
                    <!-- ReferenceDate Start -->
                    <div>
                        <p v-if="!editable" class="my-0">
                            {{ $t("dates.referenceDate.start") }}
                        </p>
                        <DatePicker
                            :label="$t('dates.referenceDate.start')"
                            :date="dummyReferenceStart"
                            :editable="editable"
                            @update:date="dummyReferenceStart = $event"
                        />
                    </div>
                    <!-- ReferenceDate End -->
                    <div>
                        <p v-if="!editable" class="my-0">
                            {{ $t("dates.referenceDate.end") }}
                        </p>
                        <DatePicker
                            :label="$t('dates.referenceDate.end')"
                            :date="execution.referenceDate"
                            :editable="editable"
                            @update:date="execution.referenceDate = $event"
                        />
                    </div>
                    <div
                        v-if="!editable"
                        class="flex flex-wrap justify-content-center"
                    >
                        <RequestHistoryButton />
                    </div>
                </div>
            </div>
            <Divider layout="vertical" />
            <div class="w-6 mt-3">
                <div class="grid row-gap-3">
                    <!-- PublishDate -->
                    <div>
                        <p
                            v-if="!editable || execution.publishedDate"
                            class="my-0"
                        >
                            {{ $t("dates.publishDate") }}
                        </p>
                        <ScheduledDatePicker
                            :label="$t('dates.publishDate')"
                            :actualDate="execution.publishedDate"
                            :scheduledDate="execution.scheduledPublishDate"
                            :editable="editable"
                            @update:scheduledDate="
                                execution.scheduledPublishDate = $event
                            "
                        />
                    </div>
                    <!-- ExecutioDate -->
                    <div>
                        <p v-if="!editable" class="my-0">
                            {{ $t("dates.executionDate") }}
                        </p>
                        <DatePicker
                            :label="$t('dates.executionDate')"
                            :date="execution.executionDate"
                            :editable="editable"
                            @update:date="execution.executionDate = $event"
                        />
                    </div>
                    <!-- ClosingDate -->
                    <div>
                        <p
                            v-if="!editable || execution.closedDate"
                            class="my-0"
                        >
                            {{ $t("dates.closingDate") }}
                        </p>
                        <ScheduledDatePicker
                            :label="$t('dates.closingDate')"
                            :actualDate="execution.closedDate"
                            :scheduledDate="execution.scheduledClosingDate"
                            :editable="editable"
                            @update:scheduledDate="
                                execution.scheduledClosingDate = $event
                            "
                        />
                    </div>
                    <!-- ArchiveDate -->
                    <div>
                        <p
                            v-if="!editable || execution.archivedDate"
                            class="my-0"
                        >
                            {{ $t("dates.archiveDate") }}
                        </p>
                        <ScheduledDatePicker
                            :label="$t('dates.archiveDate')"
                            :actualDate="execution.archivedDate"
                            :scheduledDate="execution.scheduledArchiveDate"
                            :editable="editable && isDraft()"
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
import { RequestType, RequestState } from "@/utils/Enums.ts";
import MomentWrapper from "@/utils/MomentWrapper";
import ConvertRequestButton from "./ConvertRequestButton.vue";
import RequestHistoryButton from "@/components/history/RequestHistoryButton.vue";
import { isDuration } from "moment";

export default {
    components: {
        Fieldset,
        Divider,
        Button,
        ScheduledDatePicker,
        DatePicker,
        RequestTypeLabel,
        ExecutionStateLabel,
        RequestHistoryButton,
        ConvertRequestButton,
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
        state: {
            type: String as () => keyof typeof RequestState,
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
    methods: {
        isDraft(): boolean {
            return this.state === RequestState.DRAFT;
        },
    }
};
</script>
