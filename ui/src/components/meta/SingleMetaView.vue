<template>
    <SingleMetaCommon
        :type="type"
        :execution="execution"
        :querySchedule="querySchedule"
    >
        <template #left-section>
            <DateView
                :label="$t('dates.referenceDate.start')"
                :date="dummyReferenceStart"
            />
            <DateView
                :label="$t('dates.referenceDate.end')"
                :date="execution.referenceDate"
            />
            <div class="flex flex-wrap justify-content-center">
                <RequestHistoryButton />
            </div>
        </template>
        <template #right-section>
            <ScheduledDateView
                :label="$t('dates.publishDate')"
                :scheduledDate="execution.scheduledPublishDate"
                :actualDate="execution.publishedDate"
            />
            <DateView
                :label="$t('dates.executionDate')"
                :date="execution.executionDate"
            />
            <ScheduledDateView
                :label="$t('dates.closingDate')"
                :scheduledDate="execution.scheduledClosingDate"
                :actualDate="execution.closedDate"
            />
            <ScheduledDateView
                :label="$t('dates.archiveDate')"
                :scheduledDate="execution.scheduledArchiveDate"
                :actualDate="execution.archivedDate"
            />
        </template>
    </SingleMetaCommon>
</template>

<script lang="ts">
import SingleMetaCommon from "./SingleMetaCommon.vue";
import DateView from "@/components/datePickers/DateView.vue";
import ScheduledDateView from "@/components/datePickers/ScheduledDateView.vue";
import RequestHistoryButton from "@/components/history/RequestHistoryButton.vue";

export default {
    components: {
        SingleMetaCommon,
        DateView,
        ScheduledDateView,
        RequestHistoryButton,
    },
    mixins: [SingleMetaCommon],
};
</script>
