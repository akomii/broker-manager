<template>
    <SingleMetaCommon
        :type="type"
        :execution="execution"
        :querySchedule="querySchedule"
    >
        <template #request-type-extra>
            <ConvertRequestButton :disabled="disabled" />
        </template>
        <template #left-section>
            <DatePick
                :label="$t('dates.referencePeriod.start')"
                :date="dummyReferenceStart"
                :disabled="disabled"
                @update:date="dummyReferenceStart = $event"
            />
            <DatePick
                :label="$t('dates.referencePeriod.end')"
                :date="execution.referenceDate"
                :disabled="disabled"
                @update:date="execution.referenceDate = $event"
            />
        </template>
        <template #right-section>
            <DatePick
                :label="$t('dates.publishDate')"
                :date="execution.scheduledPublishDate"
                :disabled="disabled"
                @update:date="execution.scheduledPublishDate = $event"
            />
            <DatePick
                :label="$t('dates.executionDate')"
                :date="execution.executionDate"
                :disabled="disabled"
                @update:date="execution.executionDate = $event"
            />
            <DatePick
                :label="$t('dates.closingDate')"
                :date="execution.scheduledClosingDate"
                :disabled="disabled"
                @update:date="execution.scheduledClosingDate = $event"
            />
            <DatePick
                :label="$t('dates.archiveDate')"
                :date="execution.scheduledArchiveDate"
                :disabled="disabled"
                @update:date="execution.scheduledArchiveDate = $event"
            />
        </template>
    </SingleMetaCommon>
</template>

<script lang="ts">
import DatePick from "@/components/timeWidgets/DatePick.vue";
import MomentWrapper from "@/utils/MomentWrapper";
import ConvertRequestButton from "./ConvertRequestButton.vue";
import SingleMetaCommon from "./SingleMetaCommon.vue";

export default {
    components: {
        DatePick,
        ConvertRequestButton,
        SingleMetaCommon,
    },
    mixins: [SingleMetaCommon],
    props: {
        disabled: {
            type: Boolean,
            default: false,
        },
    },
    computed: {
        duration() {
            return MomentWrapper.computeDurationBetweenDates(
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
