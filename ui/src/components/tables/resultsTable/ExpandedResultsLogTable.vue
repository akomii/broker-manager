<template>
    <DataTable :value="mergedLogAndState" class="flex justify-content-center">
        <ColumnDownloadDate key="downloadDate" />
        <ColumnUser key="user" />
        <ColumnUserOrgs key="userOrgs" />
        <ColumnHash key="hash" />
        <ColumnResultDownloadAction
            key="olderResultsDownload"
            :tooltipLabel="$t('downloadThisResultsAgain')"
            :action="dummyAction"
        />
        <template #empty>
            <p class="flex justify-content-center">
                {{ $t("noResultsAvailable") }}
            </p>
        </template>
    </DataTable>
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import ColumnDownloadDate from "@/components/tableColumns/resultsDownloadLogColumns/ColumnDownloadDate.vue";
import ColumnUser from "@/components/tableColumns/resultsDownloadLogColumns/ColumnUser.vue";
import ColumnUserOrgs from "@/components/tableColumns/resultsDownloadLogColumns/ColumnUserOrgs.vue";
import ColumnHash from "@/components/tableColumns/resultsDownloadLogColumns/ColumnHash.vue";
import ColumnResultDownloadAction from "@/components/tableColumns/resultsDownloadLogColumns/ColumnResultDownloadAction.vue";
import { ResultsDownloadLog } from "@/utils/Types";
import { ExecutionState } from "@/utils/Enums";

// TODO refactor and add docs
export default {
    components: {
        DataTable,
        Column,
        ColumnDownloadDate,
        ColumnUser,
        ColumnUserOrgs,
        ColumnHash,
        ColumnResultDownloadAction,
    },
    props: {
        downloadLog: {
            type: Array as () => ResultsDownloadLog[],
            required: true,
        },
        executionState: {
            type: String as () => ExecutionState,
            required: true,
        },
    },
    computed: {
        mergedLogAndState() {
            return this.downloadLog.map((log) => ({
                ...log,
                executionState: this.executionState,
            }));
        },
    },
    methods: {
        dummyAction() {
            console.log("dummy action");
        },
    },
};
</script>
