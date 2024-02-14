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
import RequestTableHeader from "@/components/tables/results/RequestTableHeader.vue";
import ColumnSequenceId from "@/components/tables/executionColumns/ColumnSequenceId.vue";
import ColumnExternalId from "@/components/tables/executionColumns/ColumnExternalId.vue";
import ColumnExecutionState from "@/components/tables/executionColumns/ColumnExecutionState.vue";
import ColumnReferenceDate from "@/components/tables/executionColumns/ColumnReferenceDate.vue";
import ColumnResultDownloadAction from "@/components/tables/downloadLogColumns/ColumnResultDownloadAction.vue";
import TableFooter from "@/components/tables/TableFooter.vue";
import ExpandedResultsLogTable from "@/components/tables/results/ExpandedResultsLogTable.vue";

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
        TableFooter,
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
        executionsCountMessage(): string {
            const count = this.executions.length;
            return count === 1
                ? this.$t("oneExecution")
                : this.$t("xExecutions", { numExecutions: count });
        },
    },
    methods: {
        filterDataTableExecutions(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },
        dummyAction() {
            console.log("dummy action");
        },
    },
};
</script>
