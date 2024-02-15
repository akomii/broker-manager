<template>
    <Fieldset :legend="$t('executions')">
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
                <RequestTableHeader
                    @update:showArchived="showArchived = $event"
                    @search="filterEnrichedExecutions"
                />
            </template>
            <ColumnSequenceId key="sequenceId" />

            <!-- TODO ADD context menu to show/hide columns -->
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
            <ColumnNodeAcceptanceVue key="acceptance" />
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
                <span>
                    {{ executionsCountMessage }}
                </span>
            </template>
            <template #paginatorend>
                <ExportTableButton class="mt-3" :dt="$refs.executionsTable" />
            </template>
        </DataTable>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import { RequestExecution, ManagerNode } from "@/utils/Types";
import { ExecutionState } from "@/utils/Enums";
import ExecutionStateLabel from "@/components/labels/ExecutionStateLabel.vue";
import DateView from "@/components/datePickers/DateView.vue";
import ScheduledDateView from "@/components/datePickers/ScheduledDateView.vue";
import MenuButton from "@/components/buttons/MenuButton.vue";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";
import SearchInput from "@/components/tables/SearchInput.vue";
import AnchoredRequestIcon from "@/components/icons/AnchoredRequestIcon.vue";
import ResultsNotDownloadedIcon from "@/components/icons/ResultsNotDownloadedIcon.vue";
import Checkbox from "primevue/checkbox";
import MomentWrapper from "@/utils/MomentWrapper.ts";
import RequestTableHeader from "@/components/tables/RequestTableHeader.vue";
import ColumnSequenceId from "@/components/tables/executionColumns/ColumnSequenceId.vue";
import ColumnExternalId from "@/components/tables/executionColumns/ColumnExternalId.vue";
import ColumnCreator from "@/components/tables/executionColumns/ColumnCreator.vue";
import ColumnExecutionState from "@/components/tables/executionColumns/ColumnExecutionState.vue";
import ColumnCreatedDate from "@/components/tables/executionColumns/ColumnCreatedDate.vue";
import ColumnReferenceDate from "@/components/tables/executionColumns/ColumnReferenceDate.vue";
import ColumnPublishDate from "@/components/tables/executionColumns/ColumnPublishDate.vue";
import ColumnExecutionDate from "@/components/tables/executionColumns/ColumnExecutionDate.vue";
import ColumnClosingDate from "@/components/tables/executionColumns/ColumnClosingDate.vue";
import ColumnArchiveDate from "@/components/tables/executionColumns/ColumnArchiveDate.vue";
import ColumnMenuAction from "@/components/tables/executionColumns/ColumnMenuAction.vue";
import ColumnNodeAcceptanceVue from "@/components/tables/executions/ColumnNodeAcceptance.vue";
import { TestDataService } from "@/services/TestDataService";

export default {
    components: {
        Fieldset,
        DataTable,
        Column,
        ExecutionStateLabel,
        DateView,
        ScheduledDateView,
        MenuButton,
        ExportTableButton,
        SearchInput,
        AnchoredRequestIcon,
        ResultsNotDownloadedIcon,
        Checkbox,
        RequestTableHeader,
        ColumnSequenceId,
        ColumnExternalId,
        ColumnCreator,
        ColumnExecutionState,
        ColumnCreatedDate,
        ColumnPublishDate,
        ColumnExecutionDate,
        ColumnClosingDate,
        ColumnArchiveDate,
        ColumnMenuAction,
        ColumnNodeAcceptanceVue,
    },
    props: {
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
