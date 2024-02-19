<template>
    <Fieldset :legend="$t('meta')">
        <div class="field grid mb-1">
            <div class="col-3 flex align-items-center text-lg">
                {{ $t("enums.requestType.label") }}
            </div>
            <div class="col-9 flex align-items-center gap-2">
                <RequestTypeLabel class="text-sm" :type="type" />
                <ConvertRequestButton :disabled="disabled" />
            </div>
        </div>

        <div class="flex justify-content-center column-gap-3">
            <div class="w-6 mt-3">
                <div class="grid row-gap-3">
                    <div>
                        {{ $t("enquiryPeriod") }}
                        <br />
                        <TimePeriodInput
                            :period="dummyDuration"
                            :disabled="disabled"
                            :showHours="false"
                            @update:period="dummyDuration = $event"
                        />
                        {{ $t("durationBeforeReferenceDate") }}
                    </div>
                    <div>
                        {{ $t("publishing") }}
                        <br />
                        <AutoPublishingLabel
                            class="text-lg"
                            :isAutoPublishing="isAutoPublishing"
                        />
                        <InputSwitch
                            v-model="dummyIsAutoPublishing"
                            :disabled="disabled"
                        />
                    </div>
                </div>
            </div>
            <Divider layout="vertical" />
            <div class="w-6 mt-3">
                <div class="grid row-gap-3">
                    <div>
                        {{ $t("publishInterval") }}
                        <br />
                        {{ $t("all") }}
                        <TimePeriodInput
                            :period="dummyInterval"
                            :disabled="disabled"
                            @update:period="dummyInterval = $event"
                        />
                    </div>
                    <div>
                        <DatePick
                            :label="$t('seriesClosingDate')"
                            :date="dummySeriesClosingDate"
                            :disabled="disabled"
                            @update:date="dummySeriesClosingDate = $event"
                        />
                    </div>
                    <div>
                        <DatePick
                            :label="$t('seriesArchiveDate')"
                            :date="dummySeriesArchiveDate"
                            :disabled="disabled"
                            @update:date="dummySeriesArchiveDate = $event"
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
import RequestTypeLabel from "@/components/labels/RequestTypeLabel.vue";
import ExecutionStateLabel from "@/components/labels/ExecutionStateLabel.vue";
import { RepeatedExecution } from "@/utils/Types";
import { RequestType } from "@/utils/Enums.ts";
import DateView from "@/components/timeWidgets/DateView.vue";
import ScheduledDateView from "@/components/timeWidgets/ScheduledDateView.vue";
import AutoPublishingLabel from "@/components/labels/AutoPublishingLabel.vue";
import ConvertRequestButton from "@/components/buttons/ConvertRequestButton.vue";
import DatePick from "@/components/timeWidgets/DatePick.vue";
import InputSwitch from "primevue/inputswitch";
import TimePeriodInput from "./TimePeriodInput.vue";
import MomentWrapper from "@/utils/MomentWrapper";

export default {
    components: {
        Fieldset,
        Divider,
        RequestTypeLabel,
        ExecutionStateLabel,
        DateView,
        ScheduledDateView,
        AutoPublishingLabel,
        ConvertRequestButton,
        DatePick,
        InputSwitch,
        TimePeriodInput,
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
        disabled: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            dummyIsAutoPublishing: this.isAutoPublishing,
            dummySeriesClosingDate: this.seriesClosingDate,
            dummySeriesArchiveDate: this.seriesArchiveDate,
            dummyInterval: MomentWrapper.createDuration({
                years: this.querySchedule.interval.years(),
                months: this.querySchedule.interval.months(),
                days: this.querySchedule.interval.days(),
                hours: this.querySchedule.intervalHours,
            }),
            dummyDuration: MomentWrapper.createDuration({
                years: Math.abs(this.querySchedule.duration.years()),
                months: Math.abs(this.querySchedule.duration.months()),
                days: Math.abs(this.querySchedule.duration.days()),
            }),
        };
    },
    watch: {
        dummyIsAutoPublishing(val) {
            this.$emit("update:isAutoPublishing", val);
        },
        dummySeriesClosingDate(val) {
            this.$emit("update:seriesClosingDate", val);
        },
        dummySeriesArchiveDate(val) {
            this.$emit("update:seriesArchiveDate", val);
        },
        dummyInterval(newVal) {
            let updatedInterval = MomentWrapper.createDuration({
                years: newVal.years(),
                months: newVal.months(),
                days: newVal.days(),
            });
            this.$emit("update:querySchedule", {
                ...this.querySchedule,
                interval: updatedInterval,
                intervalHours: newVal.hours(),
            });
        },
        dummyDuration(newVal) {
            let updatedDuration = MomentWrapper.createDuration({
                years: -newVal.years(),
                months: -newVal.months(),
                days: -newVal.days(),
            });
            this.$emit("update:querySchedule", {
                ...this.querySchedule,
                duration: updatedDuration,
            });
        },
    },
};
</script>
