<template>
    <DataTable
        :value="dataTableExecutions"
        sortField="sequenceId"
        :sortOrder="-1"
        ref="dt"
        v-model:expandedRows="expandedRows"
    >
        <template #header>
            <div
                class="flex flex-wrap justify-content-between align-items-center"
            >
                <span>
                    <Checkbox
                        v-model="showArchived"
                        :binary="true"
                        inputId="toggleArchived"
                        @update:modelValue="filterDataTableExecutions()"
                    />
                    <label for="toggleArchived" class="ml-2">
                        {{ $t("showArchivedExecutions") }}
                    </label>
                </span>
                <SearchInput
                    @update:input="
                        (val) => {
                            currentSearchTerm = val;
                            filterDataTableExecutions();
                        }
                    "
                />
            </div>
        </template>

        <!-- TODO ADD context menu to show/hide columns -->
        <Column expander v-if="showExpanderColumn" />
        <Column field="sequenceId" :header="$t('sequenceId')" sortable>
            <template #body="slotProps">
                <div class="flex gap-1">
                    <span>
                        {{ slotProps.data.sequenceId }}
                    </span>
                    <NoDownloadedResultsIcon
                        v-if="
                            isResultsDownloadLogEmpty(slotProps.data) &&
                            slotProps.data.executionState !== 'PENDING'
                        "
                    />
                    TODO
                    <NewResultsAvailableIcon />
                </div>
            </template>
        </Column>
        <Column field="externalId" :header="$t('externalId')" sortable>
            <template #body="slotProps">
                <span v-if="slotProps.data.externalId">
                    {{ slotProps.data.externalId }}
                </span>
                <i v-else class="pi pi-minus" />
            </template>
        </Column>
        <Column
            field="executionState"
            :header="$t('enums.executionState.label')"
            sortable
        >
            <template #body="slotProps">
                <ExecutionStateLabel :state="slotProps.data.executionState" />
            </template>
        </Column>
        <Column
            field="referenceDate"
            :header="$t('dates.referenceDate')"
            sortable
        >
            <template #body="slotProps">
                <DateView :date="slotProps.data.referenceDate" />
            </template>
        </Column>
        <Column field="actions" bodyStyle="text-align: center">
            <template #body="slotProps">
                <DownloadButton
                    :tooltipLabel="$t('downloadCurrentResults')"
                    :disabled="
                        isExecutionArchived(slotProps.data.executionState) ||
                        isExecutionUnpublished(slotProps.data.executionState)
                    "
                    :action="dummyAction"
                />
            </template>
        </Column>

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
            <div class="flex flex-wrap justify-content-between">
                <span>
                    {{
                        executions.length === 1
                            ? $t("oneExecution")
                            : $t("xExecutions", {
                                  numExecutions: executions
                                      ? executions.length
                                      : 0,
                              })
                    }}
                </span>
                <ExportTableButton class="mt-3" :dt="$refs.dt" />
            </div>
        </template>
    </DataTable>
    <!-- TODO empty cannot be extended -->
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import { RequestExecution, NodeStatusInfo } from "@/utils/Types";
import { ExecutionState } from "@/utils/Enums";
import ExecutionStateLabel from "@/components/labels/ExecutionStateLabel.vue";
import DateView from "@/components/datePickers/DateView.vue";
import ScheduledDateView from "@/components/datePickers/ScheduledDateView.vue";
import MenuButton from "@/components/common/MenuButton.vue";
import ExportTableButton from "@/components/common/ExportTableButton.vue";
import SearchInput from "@/components/common/SearchInput.vue";
import AnchoredRequestIcon from "../executions/AnchoredRequestIcon.vue";
import NoDownloadedResultsIcon from "../executions/NoDownloadedResultsIcon.vue";
import Checkbox from "primevue/checkbox";
import TargetNodesViewDialog from "../executions/TargetNodesViewDialog.vue";
import MomentWrapper from "@/utils/MomentWrapper.ts";
import DownloadButton from "../common/DownloadButton.vue";
import SimpleChipList from "../tags/SimpleChipList.vue";
import ExpandedResultsLogTable from "./ExpandedResultsLogTable.vue";
import NewResultsAvailableIcon from "./NewResultsAvailableIcon.vue";

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
        NoDownloadedResultsIcon,
        Checkbox,
        TargetNodesViewDialog,
        DownloadButton,
        SimpleChipList,
        ExpandedResultsLogTable,
        NewResultsAvailableIcon,
    },
    props: {
        executions: {
            type: Array as () => RequestExecution[],
            required: true,
        },
    },
    data() {
        return {
            //TODO add functionality
            pendingExecutionMenu: [{ label: this.$t("publishExecution") }],
            publishedExecutionMenu: [
                { label: this.$t("closeExecution") },
                { label: this.$t("archiveExecution") },
                { label: this.$t("goToResults") },
            ],
            closedExecutionMenu: [
                { label: this.$t("archiveExecution") },
                { label: this.$t("goToResults") },
            ],
            archivedExecutionMenu: [{ label: this.$t("goToResults") }],
            dataTableExecutions: this.executions,
            showArchived: false,
            currentSearchTerm: "",
            expandedRows: [],
        };
    },
    mounted() {
        this.filterDataTableExecutions();
    },
    computed: {
        // Checks if there's any execution with a non-empty resultsDownloadLog
        showExpanderColumn() {
            return this.dataTableExecutions.some(
                (execution) => execution.resultsDownloadLog.length > 0
            );
        },
    },
    methods: {
        filterDataTableExecutions() {
            this.dataTableExecutions = this.filterExecutionsBySearchTerm(
                this.executions.filter(
                    (execution) =>
                        this.showArchived ||
                        execution.executionState !== ExecutionState.ARCHIVED
                ),
                this.currentSearchTerm.toLowerCase()
            );
        },
        filterExecutionsBySearchTerm(
            executions: RequestExecution[],
            searchTerm: string
        ): RequestExecution[] {
            return executions.filter((execution) => {
                const translatedExecutionState = this.$t(
                    `enums.executionState.${execution.executionState}`
                ).toLowerCase();
                const formattedCreateDate = this.formatDate(
                    execution.createdDate
                );
                const formattedReferenceDate = this.formatDate(
                    execution.referenceDate
                );
                const formattedScheduledPublishDate = this.formatDate(
                    execution.scheduledPublishDate
                );
                const formattedPublishedDate = this.formatDate(
                    execution.publishedDate
                );
                const formattedExecutionDate = this.formatDate(
                    execution.executionDate
                );
                const formattedScheduledClosingDate = this.formatDate(
                    execution.scheduledClosingDate
                );
                const formattedClosingDate = this.formatDate(
                    execution.closedDate
                );
                const formattedScheduledArchiveDate = this.formatDate(
                    execution.scheduledArchiveDate
                );
                const formattedArchiveDate = this.formatDate(
                    execution.archivedDate
                );
                const searchInFields = [
                    execution.sequenceId?.toString(),
                    execution.externalId?.toString(),
                    execution.creator?.toLowerCase(),
                    translatedExecutionState,
                    formattedCreateDate,
                    formattedReferenceDate,
                    formattedScheduledPublishDate,
                    formattedPublishedDate,
                    formattedExecutionDate,
                    formattedScheduledClosingDate,
                    formattedClosingDate,
                    formattedScheduledArchiveDate,
                    formattedArchiveDate,
                ];
                return searchInFields.some(
                    (field) => field && field.toLowerCase().includes(searchTerm)
                );
            });
        },
        formatDate(date: Date | undefined | null): string {
            return MomentWrapper.formatDateToGermanLocale(date);
        },
        isResultsDownloadLogEmpty(execution: RequestExecution): boolean {
            return execution.resultsDownloadLog.length === 0;
        },
        getMenuForExecutionState(state: ExecutionState) {
            switch (state) {
                case ExecutionState.PENDING:
                    return this.pendingExecutionMenu;
                case ExecutionState.PUBLISHED:
                    return this.publishedExecutionMenu;
                case ExecutionState.CLOSED:
                    return this.closedExecutionMenu;
                case ExecutionState.ARCHIVED:
                    return this.archivedExecutionMenu;
                default:
                    return [];
            }
        },
        isNodeStatusInfoNotEmpty(nodeStatusInfo: NodeStatusInfo[]): boolean {
            return nodeStatusInfo.length > 0;
        },
        isExecutionArchived(state: ExecutionState): boolean {
            return state === ExecutionState.ARCHIVED;
        },
        isExecutionUnpublished(state: ExecutionState): boolean {
            return state === ExecutionState.PENDING;
        },
        dummyAction() {
            console.log("dummy action");
        },
        getStateAndDownloadLogFromExecution(execution: RequestExecution) {
            return execution.resultsDownloadLog.map((log) => ({
                ...log,
                executionState: execution.executionState,
            }));
        },
    },
};
</script>
