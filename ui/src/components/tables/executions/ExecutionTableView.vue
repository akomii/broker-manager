<template>
    <Fieldset :legend="$t('executions')">
        <DataTable
            :value="filteredDataTableExecutions"
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
                    @search="filterDataTableExecutions"
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




            <!-- TODO -->
            <Column
                field="acceptance"
                :header="$t('acceptance')"
                bodyStyle="text-align: center"
            >
                <template #body="slotProps">
                    <TargetNodesViewDialog
                        v-if="
                            isNodeStatusInfoNotEmpty(
                                slotProps.data.nodeStatusInfos
                            )
                        "
                        :execution="slotProps.data"
                        :showProcessingStateInfo="true"
                    />
                    <i v-else class="pi pi-minus" />
                </template>
            </Column>

            
            <Column field="actions" bodyStyle="text-align: center">
                <template #body="slotProps">
                    <MenuButton
                        :icon="'pi pi-ellipsis-v'"
                        :outlined="true"
                        :menu="
                            getMenuForExecutionState(
                                slotProps.data.executionState
                            )
                        "
                    />
                </template>
            </Column>



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
import { RequestExecution, NodeStatusInfo } from "@/utils/Types";
import { ExecutionState } from "@/utils/Enums";
import ExecutionStateLabel from "@/components/labels/ExecutionStateLabel.vue";
import DateView from "@/components/datePickers/DateView.vue";
import ScheduledDateView from "@/components/datePickers/ScheduledDateView.vue";
import MenuButton from "@/components/common/MenuButton.vue";
import ExportTableButton from "@/components/common/ExportTableButton.vue";
import SearchInput from "@/components/common/SearchInput.vue";
import AnchoredRequestIcon from "./AnchoredRequestIcon.vue";
import ResultsNotDownloadedIcon from "@/components/icons/ResultsNotDownloadedIcon.vue";
import Checkbox from "primevue/checkbox";
import TargetNodesViewDialog from "./TargetNodesViewDialog.vue";
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
import TablePaginator from "@/components/tables/TablePaginator.vue";

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
        TargetNodesViewDialog,
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
        TablePaginator,
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
            showArchived: false,
            currentSearchTerm: "",
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
    methods: {
        filterDataTableExecutions(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },

        //-------------------TODO---------------------

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
    },
};
</script>
