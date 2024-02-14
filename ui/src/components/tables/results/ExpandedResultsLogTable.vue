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
import ColumnDownloadDate from "@/components/tables/downloadLogColumns/ColumnDownloadDate.vue";
import ColumnUser from "@/components/tables/downloadLogColumns/ColumnUser.vue";
import ColumnUserOrgs from "@/components/tables/downloadLogColumns/ColumnUserOrgs.vue";
import ColumnHash from "@/components/tables/downloadLogColumns/ColumnHash.vue";
import ColumnResultDownloadAction from "@/components/tables/downloadLogColumns/ColumnResultDownloadAction.vue";
import { ResultsDownloadLog } from "@/utils/Types";
import { ExecutionState } from "@/utils/Enums";

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
