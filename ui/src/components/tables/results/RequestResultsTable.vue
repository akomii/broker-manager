<template>
    <DataTable
        :value="filteredDataTableExecutions"
        sortField="sequenceId"
        :sortOrder="-1"
        ref="resultTable"
        v-model:expandedRows="expandedRows"
    >
        <template #header>
            <RequestTableHeader
                @update:showArchived="showArchived = $event"
                @search="filterDataTableExecutions"
            />
        </template>
        <!-- TODO: hide expander if ExpandedTable is empty-->
        <!-- TODO: add column toggle-->
        <Column expander />
        <ColumnSequenceId key="sequenceId" />
        <ColumnExternalId key="externalId" />
        <ColumnExecutionState key="executionState" />
        <ColumnReferenceDate key="referenceDate" />
        <ColumnResultDownloadAction key="newestResultsDownload" />
        <template #expansion="slotProps">
            <ExpandedResultsLogTable
                :downloadLog="slotProps.data.resultsDownloadLog"
                :executionState="slotProps.data.executionState"
            />
        </template>
        <template #empty>
            <p class="flex justify-content-center">
                {{ $t("noExecutionsFound") }}
            </p>
        </template>
        <template #footer>
            <RequestTableFooter
                :executions="executions"
                :refDataTable="$refs.resultTable"
            />
        </template>
    </DataTable>
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import { RequestExecution } from "@/utils/Types";
import { ExecutionState } from "@/utils/Enums";
import MomentWrapper from "@/utils/MomentWrapper.ts";
import RequestTableHeader from "./RequestTableHeader.vue";
import ColumnSequenceId from "./ColumnSequenceId.vue";
import ColumnExternalId from "./ColumnExternalId.vue";
import ColumnExecutionState from "./ColumnExecutionState.vue";
import ColumnReferenceDate from "./ColumnReferenceDate.vue";
import ColumnResultDownloadAction from "./ColumnResultDownloadAction.vue";
import RequestTableFooter from "./RequestTableFooter.vue";
import ExpandedResultsLogTable from "./ExpandedResultsLogTable.vue";

export default {
    components: {
        DataTable,
        Column,
        ExpandedResultsLogTable,
        RequestTableHeader,
        ColumnSequenceId,
        ColumnExternalId,
        ColumnExecutionState,
        ColumnReferenceDate,
        ColumnResultDownloadAction,
        RequestTableFooter,
    },
    props: {
        executions: {
            type: Array as () => RequestExecution[],
            required: true,
        },
    },
    data() {
        return {
            showArchived: false,
            currentSearchTerm: "",
            expandedRows: [],
        };
    },
    computed: {
        filteredDataTableExecutions(): RequestExecution[] {
            return this.executions
                .filter((execution) => {
                    return (
                        this.showArchived ||
                        execution.executionState !== ExecutionState.ARCHIVED
                    );
                })
                .filter((execution) => {
                    const searchFields = [
                        execution.sequenceId?.toString(),
                        execution.externalId?.toString(),
                        this.$t(
                            `enums.executionState.${execution.executionState}`
                        ).toLowerCase(),
                        MomentWrapper.formatDateToGermanLocale(
                            execution.referenceDate
                        ),
                    ];
                    return searchFields.some((field) =>
                        field
                            ?.toLowerCase()
                            .includes(this.currentSearchTerm.toLowerCase())
                    );
                });
        },
    },
    methods: {
        filterDataTableExecutions(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },
    },
};
</script>
