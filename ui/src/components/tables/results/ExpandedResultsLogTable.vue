<template>
    <DataTable :value="mergedLogAndState" class="flex justify-content-center">
        <ColumnDownloadDate key="downloadDate" />
        <ColumnUser key="user" />
        <ColumnOrganizations key="organizations" />
        <ColumnsResultHash key="resultHash" />
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
import ColumnDownloadDate from "./ColumnDownloadDate.vue";
import ColumnUser from "./ColumnUser.vue";
import ColumnOrganizations from "./ColumnOrganizations.vue";
import ColumnsResultHash from "./ColumnsResultHash.vue";
import ColumnResultDownloadAction from "./ColumnResultDownloadAction.vue";
import { ResultsDownloadLog } from "@/utils/Types";
import { ExecutionState } from "@/utils/Enums";

export default {
    components: {
        DataTable,
        Column,
        ColumnDownloadDate,
        ColumnUser,
        ColumnOrganizations,
        ColumnsResultHash,
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
