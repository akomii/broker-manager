<template>
    <DataTable
        :value="filteredExecutions"
        sortField="sequenceId"
        :sortOrder="-1"
        ref="resultTable"
        v-model:expandedRows="expandedRows"
    >
        <template #header>
            <ResultsTableHeader
                @update:showArchived="showArchived = $event"
                @search="filterExecutions"
            />
        </template>
        <!-- TODO: hide expander if ExpandedTable is empty-->
        <Column expander />
        <ColumnSequenceId key="sequenceId" />
        <ColumnExternalId key="externalId" />
        <ColumnExecutionState key="executionState" />
        <ColumnReferenceDate key="referenceDate" />
        <ColumnResultDownloadAction
            key="newestResultsDownload"
            :tooltipLabel="$t('downloadCurrentResults')"
            :action="dummyAction"
        />
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
            <TableFooter :refDataTable="$refs.resultTable">
                <template #count-message>
                    {{ executionsCountMessage }}
                </template>
            </TableFooter>
        </template>
    </DataTable>
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import { RequestExecution } from "@/utils/Types";
import { ExecutionState } from "@/utils/Enums";
import MomentWrapper from "@/utils/MomentWrapper.ts";
import ResultsTableHeader from "@/components/tables/resultsTable/ResultsTableHeader.vue";
import ColumnSequenceId from "@/components/tableColumns/requestExecutionColumns/ColumnSequenceId.vue";
import ColumnExternalId from "@/components/tableColumns/requestExecutionColumns/ColumnExternalId.vue";
import ColumnExecutionState from "@/components/tableColumns/requestExecutionColumns/ColumnExecutionState.vue";
import ColumnReferenceDate from "@/components/tableColumns/requestExecutionColumns/ColumnReferenceDate.vue";
import ColumnResultDownloadAction from "@/components/tableColumns/resultsDownloadLogColumns/ColumnResultDownloadAction.vue";
import TableFooter from "@/components/tables/TableFooter.vue";
import ExpandedResultsLogTable from "@/components/tables/resultsTable/ExpandedResultsLogTable.vue";

export default {
    components: {
        DataTable,
        Column,
        ExpandedResultsLogTable,
        ResultsTableHeader,
        ColumnSequenceId,
        ColumnExternalId,
        ColumnExecutionState,
        ColumnReferenceDate,
        ColumnResultDownloadAction,
        TableFooter,
    },
    props: {
         // TODO CREATE NEW DATATYPE AS INPUT PROP
         // TODO refactor and add docs
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
        filteredExecutions(): RequestExecution[] {
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
                        ),
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
        executionsCountMessage(): string {
            const count = this.executions.length;
            return count === 1
                ? this.$t("oneExecution")
                : this.$t("xExecutions", { numExecutions: count });
        },
    },
    methods: {
        filterExecutions(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },
        dummyAction() {
            console.log("dummy action");
        },
    },
};
</script>
