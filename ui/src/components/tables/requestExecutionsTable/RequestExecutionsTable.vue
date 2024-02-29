<template>
    <DataTable
        :value="filteredEnrichedExecutions"
        sortField="sequenceId"
        :sortOrder="-1"
        ref="executionsTable"
        paginator
        :rows="10"
        :rowsPerPageOptions="[10, 25, 50]"
    >
        <template #header>
            <TableHeaderCommon
                :showCheckBox1="true"
                :checkBox1Title="$t('showArchivedExecutions')"
                @toggle:checkBox1="showArchived = $event"
                @search="filterEnrichedExecutions"
            />
        </template>
        <ColumnSequenceId
            key="sequenceId"
            :anchoredSequenceIdRef="anchoredSequenceIdRef"
        />
        <ColumnExternalId key="externalId" />
        <ColumnCreator key="creator" />
        <ColumnExecutionState key="executionState" />
        <ColumnCreatedDate key="createdDate" />
        <ColumnReferenceDate key="referenceDate" />
        <ColumnPublishDate key="publishDate" />
        <ColumnExecutionDate key="executionDate" />
        <ColumnClosingDate key="closingDate" />
        <ColumnArchiveDate key="archiveDate" />
        <ColumnNodeCompletion key="completion" />
        <ColumnMenuAction
            key="menuAction"
            :menuData="getMenuForExecutionState"
        />
        <template #empty>
            <p class="flex justify-content-center">
                {{ $t("noExecutionsFound") }}
            </p>
        </template>
        <template #paginatorstart>
            <span>{{ executionsCountMessage }}</span>
        </template>
        <template #paginatorend>
            <ExportTableButton class="mt-3" :dt="$refs.executionsTable" />
        </template>
    </DataTable>
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import ColumnSequenceId from "@/components/tableColumns/requestExecutionColumns/ColumnSequenceId.vue";
import ColumnExternalId from "@/components/tableColumns/requestExecutionColumns/ColumnExternalId.vue";
import ColumnCreator from "@/components/tableColumns/requestExecutionColumns/ColumnCreator.vue";
import ColumnExecutionState from "@/components/tableColumns/requestExecutionColumns/ColumnExecutionState.vue";
import ColumnCreatedDate from "@/components/tableColumns/requestExecutionColumns/ColumnCreatedDate.vue";
import ColumnReferenceDate from "@/components/tableColumns/requestExecutionColumns/ColumnReferenceDate.vue";
import ColumnPublishDate from "@/components/tableColumns/requestExecutionColumns/ColumnPublishDate.vue";
import ColumnExecutionDate from "@/components/tableColumns/requestExecutionColumns/ColumnExecutionDate.vue";
import ColumnClosingDate from "@/components/tableColumns/requestExecutionColumns/ColumnClosingDate.vue";
import ColumnArchiveDate from "@/components/tableColumns/requestExecutionColumns/ColumnArchiveDate.vue";
import ColumnNodeCompletion from "@/components/tables/requestExecutionsTable/ColumnNodeCompletion.vue";
import ColumnMenuAction from "@/components/tableColumns/requestExecutionColumns/ColumnMenuAction.vue";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";
import { RequestExecution, ManagerNode } from "@/utils/Types";
import MomentWrapper from "@/utils/MomentWrapper";
import { ExecutionState } from "@/utils/Enums";
import { TestDataService } from "@/services/TestDataService.js";
import TableHeaderCommon from "@/components/tables/TableHeaderCommon.vue";

export default {
    components: {
        DataTable,
        ColumnSequenceId,
        ColumnExternalId,
        ColumnCreator,
        ColumnExecutionState,
        ColumnCreatedDate,
        ColumnReferenceDate,
        ColumnPublishDate,
        ColumnExecutionDate,
        ColumnClosingDate,
        ColumnArchiveDate,
        ColumnNodeCompletion,
        ColumnMenuAction,
        ExportTableButton,
        TableHeaderCommon,
    },
    props: {
        // TODO CREATE NEW DATATYPE AS INPUT PROP
        // TODO refactor and add docs
        executions: {
            type: Array as () => RequestExecution[],
            required: true,
        },
        anchoredSequenceIdRef: {
            type: Number,
            required: true,
        },
    },
    data() {
        return {
            showArchived: false,
            currentSearchTerm: "",
            allManagerNodes: [] as ManagerNode[],
        };
    },
    computed: {
        enrichedExecutions(): RequestExecution[] {
            return this.executions.map((execution) => {
                const correspondingNodes = this.allManagerNodes.filter((node) =>
                    execution.nodeStatusInfos.some(
                        (statusInfo) => statusInfo.nodeId === node.id
                    )
                );
                return {
                    ...execution,
                    nodes: correspondingNodes,
                };
            });
        },
        filteredEnrichedExecutions(): RequestExecution[] {
            return this.enrichedExecutions
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
                        execution.creator,
                        this.$t(
                            `enums.executionState.${execution.executionState}`
                        ),
                        MomentWrapper.formatDateToGermanLocale(
                            execution.createdDate
                        ),
                        MomentWrapper.formatDateToGermanLocale(
                            execution.referenceDate
                        ),
                        MomentWrapper.formatDateToGermanLocale(
                            execution.scheduledPublishDate
                        ),
                        MomentWrapper.formatDateToGermanLocale(
                            execution.publishedDate
                        ),
                        MomentWrapper.formatDateToGermanLocale(
                            execution.executionDate
                        ),
                        MomentWrapper.formatDateToGermanLocale(
                            execution.scheduledClosingDate
                        ),
                        MomentWrapper.formatDateToGermanLocale(
                            execution.closedDate
                        ),
                        MomentWrapper.formatDateToGermanLocale(
                            execution.scheduledArchiveDate
                        ),
                        MomentWrapper.formatDateToGermanLocale(
                            execution.archivedDate
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
    async mounted() {
        await this.fetchManagerNodes();
    },
    methods: {
        async fetchManagerNodes(): Promise<void> {
            this.allManagerNodes = await TestDataService.getNodes();
        },
        filterEnrichedExecutions(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },
        // TODO add funcionality
        getMenuForExecutionState(state: ExecutionState) {
            switch (state) {
                case ExecutionState.PENDING:
                    return [{ label: this.$t("publishExecution") }];
                case ExecutionState.PUBLISHED:
                    return [
                        { label: this.$t("closeExecution") },
                        { label: this.$t("archiveExecution") },
                        { label: this.$t("goToResults") },
                    ];
                case ExecutionState.CLOSED:
                    return [
                        { label: this.$t("archiveExecution") },
                        { label: this.$t("goToResults") },
                    ];
                case ExecutionState.ARCHIVED:
                    return [{ label: this.$t("goToResults") }];
                default:
                    return [];
            }
        },
    },
};
</script>
