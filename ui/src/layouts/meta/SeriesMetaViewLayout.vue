<template>
    <Fieldset :legend="$t('meta')">
        <div class="field grid mb-1">
            <div class="col-3 flex align-items-center text-lg">
                {{ $t("enums.requestType.label") }}
            </div>
            <div class="col-9 flex align-items-center gap-2">
                <RequestTypeLabel class="text-sm" :type="type" />
            </div>
        </div>

        <div class="flex justify-content-center column-gap-3">
            <div class="w-6 mt-3">
                <div class="grid row-gap-3">
                    <div>
                        {{ $t("enquiryPeriod") }}
                        <br />
                        <span v-if="querySchedule.duration">
                            {{
                                $t("durationBeforeReferenceDate", {
                                    duration: formatDuration(
                                        querySchedule.duration
                                    ),
                                })
                            }}
                        </span>
                        <span v-else>
                            <i class="pi pi-minus" />
                        </span>
                    </div>
                    <div>
                        {{ $t("publishing") }}
                        <br />
                        <AutoPublishingLabel
                            :isAutoPublishing="isAutoPublishing"
                        />
                    </div>
                    <div class="flex flex-wrap justify-content-center">
                        <RequestHistoryDialog :history="requestHistory" />
                    </div>
                </div>
            </div>
            <Divider layout="vertical" />
            <div class="w-6 mt-3">
                <div class="grid row-gap-3">
                    <div>
                        {{ $t("publishInterval") }}
                        <br />
                        <span v-if="querySchedule.duration">
                            {{
                                $t("everyInterval", {
                                    interval: formatDuration(dummyInterval),
                                })
                            }}
                        </span>
                        <span v-else>
                            <i class="pi pi-minus" />
                        </span>
                    </div>
                    <div>
                        {{ $t("seriesClosingDate") }}
                        <br />
                        <DateView :date="seriesClosingDate" />
                    </div>
                    <div>
                        {{ $t("seriesArchiveDate") }}
                        <br />
                        <DateView :date="seriesArchiveDate" />
                    </div>
                </div>
            </div>
        </div>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import Divider from "primevue/divider";
import RequestTypeLabel from "@/components/labels/RequestTypeLabel.vue";
import ExecutionStateLabel from "@/components/labels/ExecutionStateLabel.vue";
import { RepeatedExecution } from "@/utils/Types";
import { RequestType } from "@/utils/Enums.ts";
import DateView from "@/components/timeWidgets/DateView.vue";
import ScheduledDateView from "@/components/timeWidgets/ScheduledDateView.vue";
import RequestHistoryDialog from "@/components/dialogs/RequestHistoryDialog.vue";
import MomentWrapper from "@/utils/MomentWrapper";
import type { MomentDuration } from "@/utils/MomentWrapper";
import AutoPublishingLabel from "@/components/labels/AutoPublishingLabel.vue";
import { ModificationHistoryItem } from "@/utils/Types";

// TODO refactor and add docs
export default {
    components: {
        Fieldset,
        Divider,
        RequestTypeLabel,
        ExecutionStateLabel,
        DateView,
        ScheduledDateView,
        RequestHistoryDialog,
        AutoPublishingLabel,
    },
    props: {
        type: {
            type: String as () => keyof typeof RequestType,
            required: true,
        },
        querySchedule: {
            type: Object as () => RepeatedExecution,
            required: true,
        },
        isAutoPublishing: {
            type: Boolean,
            required: true,
        },
        seriesClosingDate: {
            type: Date,
            required: true,
        },
        seriesArchiveDate: {
            type: Date,
            required: true,
        },
        requestHistory: {
            type: Array as () => ModificationHistoryItem[],
            required: true,
        },
    },
    computed: {
        dummyInterval(): MomentDuration {
            return MomentWrapper.addHoursToDuration(
                this.querySchedule.interval,
                this.querySchedule.intervalHours
            );
        },
    },
    methods: {
        formatDuration(duration: MomentDuration): string {
            return MomentWrapper.formatDurationToHumanReadable(
                duration,
                this.$t
            );
        },
    },
};
</script>
