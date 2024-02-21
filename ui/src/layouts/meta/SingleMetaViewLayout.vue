<template>
    <SingleMetaCommon
        :type="type"
        :execution="execution"
        :querySchedule="querySchedule"
    >
        <template #left-section>
            <div>
                {{ $t("dates.referencePeriod.start") }}
                <br />
                <DateView :date="dummyReferenceStart" />
            </div>
            <div>
                {{ $t("dates.referencePeriod.end") }}
                <br />
                <DateView :date="execution.referenceDate" />
            </div>
            <div class="flex flex-wrap justify-content-center">
                <RequestHistoryDialog :history="requestHistory" />
            </div>
        </template>
        <template #right-section>
            <div>
                {{ $t("dates.publishDate") }}
                <br />
                <ScheduledDateView
                    :tooltipLabel="$t('dates.publishDate')"
                    :scheduledDate="execution.scheduledPublishDate"
                    :actualDate="execution.publishedDate"
                />
            </div>
            <div>
                {{ $t("dates.executionDate") }}
                <br />
                <DateView :date="execution.executionDate" />
            </div>
            <div>
                {{ $t("dates.closingDate") }}
                <br />
                <ScheduledDateView
                    :tooltipLabel="$t('dates.closingDate')"
                    :scheduledDate="execution.scheduledClosingDate"
                    :actualDate="execution.closedDate"
                />
            </div>
            <div>
                {{ $t("dates.archiveDate") }}
                <br />
                <ScheduledDateView
                    :tooltipLabel="$t('dates.archiveDate')"
                    :scheduledDate="execution.scheduledArchiveDate"
                    :actualDate="execution.archivedDate"
                />
            </div>
        </template>
    </SingleMetaCommon>
</template>

<script lang="ts">
import SingleMetaCommon from "./SingleMetaCommon.vue";
import DateView from "@/components/timeWidgets/DateView.vue";
import ScheduledDateView from "@/components/timeWidgets/ScheduledDateView.vue";
import RequestHistoryDialog from "@/components/dialogs/RequestHistoryDialog.vue";
import { ModificationHistoryItem } from "@/utils/Types";

export default {
    components: {
        SingleMetaCommon,
        DateView,
        ScheduledDateView,
        RequestHistoryDialog,
    },
    mixins: [SingleMetaCommon],
    props: {
        requestHistory: {
            type: Array as () => ModificationHistoryItem[],
            required: true,
        },
    },
};
</script>
