<template>
    <Fieldset :legend="$t('executions')">
        <DataTable
            :value="dataTableExecutions"
            paginator
            :rows="10"
            :rowsPerPageOptions="[10, 25, 50]"
            sortField="sequenceId"
            :sortOrder="-1"
        >
            <template #header>
                <div class="flex flex-wrap justify-content-end">
                    <SearchInput @update:input="filterDataTableExecutions" />
                </div>

                <!-- TODO REMOVE -->
                <div class="flex flex-wrap justify-content-between">
                    <button @click="toggleArchivedVisibility">
                        {{
                            showArchived
                                ? $t("hideArchived")
                                : $t("showArchived")
                        }}
                    </button>
                </div>
            </template>
            <Column field="sequenceId" :header="$t('sequenceId')" sortable>
                <template #body="slotProps">
                    <span class="mr-2">
                        {{ slotProps.data.sequenceId }}
                    </span>
                    <AnchoredRequestIcon
                        v-if="isSequenceIdAnchoroed(slotProps.data.sequenceId)"
                    />
                    <NoDownloadedResultsIcon
                        v-if="isResultsDownloadLogEmpty(slotProps.data)"
                    />
                </template>
            </Column>
            <Column field="externalId" :header="$t('externalId')" sortable />
            <Column field="creator" :header="$t('creator')" sortable />
            <Column
                field="executionState"
                :header="$t('enums.executionState.label')"
                sortable
            >
                <template #body="slotProps">
                    <ExecutionStateLabel
                        :state="slotProps.data.executionState"
                    />
                </template>
            </Column>
            <Column
                field="createdDate"
                :header="$t('dates.createdDate')"
                sortable
            >
                <template #body="slotProps">
                    <DateView :date="slotProps.data.createdDate" />
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
            <Column
                field="publishDate"
                :header="$t('dates.publishDate')"
                sortable
            >
                <template #body="slotProps">
                    <ScheduledDateView
                        :tooltipLabel="$t('dates.publishDate')"
                        :scheduledDate="slotProps.data.scheduledPublishDate"
                        :actualDate="slotProps.data.publishedDate"
                    />
                </template>
            </Column>
            <Column
                field="executionDate"
                :header="$t('dates.executionDate')"
                sortable
            >
                <template #body="slotProps">
                    <DateView :date="slotProps.data.executionDate" />
                </template>
            </Column>
            <Column
                field="closingDate"
                :header="$t('dates.closingDate')"
                sortable
            >
                <template #body="slotProps">
                    <ScheduledDateView
                        :tooltipLabel="$t('dates.closingDate')"
                        :scheduledDate="slotProps.data.scheduledClosingDate"
                        :actualDate="slotProps.data.closedDate"
                    />
                </template>
            </Column>
            <Column
                field="archiveDate"
                :header="$t('dates.archiveDate')"
                sortable
            >
                <template #body="slotProps">
                    <ScheduledDateView
                        :tooltipLabel="$t('dates.archiveDate')"
                        :scheduledDate="slotProps.data.scheduledArchiveDate"
                        :actualDate="slotProps.data.archivedDate"
                    />
                </template>
            </Column>
            <!--TODO Show TargetNodeView in Popup-->
            <Column
                field="acceptance"
                :header="$t('acceptance')"
                bodyStyle="text-align: center"
            >
                <template #body="slotProps">
                    {{
                        $t("XofY", {
                            x: countCompletedNodes(
                                slotProps.data.nodeStatusInfos
                            ),
                            y: slotProps.data.nodeStatusInfos.length,
                        })
                    }}
                </template>
            </Column>
            <Column field="actions" bodyStyle="text-align: center">
                <template #body="slotProps">
                    <MenuButton
                        :icon="'pi pi-ellipsis-v'"
                        :outlined="true"
                        :menu="exeuctionMenu"
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
                    {{
                        dataTableExecutions.length === 1
                            ? $t("oneExecution")
                            : $t("xExecutions", {
                                  numExecutions: dataTableExecutions
                                      ? dataTableExecutions.length
                                      : 0,
                              })
                    }}
                </span>
            </template>
            <template #paginatorend>
                <ExportTableButton class="mt-3" />
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
import NoDownloadedResultsIcon from "./NoDownloadedResultsIcon.vue";

// TODO Column Toggle
// TODO Context Menu
// TODO Table Export
// TODO Sort funktioniert nicht für Datum mit Scheduled

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
            exeuctionMenu: [
                { label: this.$t("closeExecution") },
                { label: this.$t("archiveExecution") },
                { label: this.$t("goToResults") },
            ],
            dataTableExecutions: this.executions,
            showArchived: false,
        };
    },
    mounted() {
        this.filterDataTableExecutions("");
    },
    methods: {
        countCompletedNodes(nodeInfos: NodeStatusInfo[]): number {
            return nodeInfos.filter((node) => node.completed !== null).length;
        },
        filterDataTableExecutions(searchTerm: string) {
            this.dataTableExecutions = this.filterExecutionsBySearchTerm(
                this.executions.filter(
                    (execution) =>
                        this.showArchived ||
                        execution.executionState !== ExecutionState.ARCHIVED
                ),
                searchTerm.toLowerCase()
            );
        },

        //TODO status search only works in english
        //TODO Datesearch does not work
        filterExecutionsBySearchTerm(
            executions: RequestExecution[],
            searchTerm: string
        ): RequestExecution[] {
            return executions.filter((execution) => {
                // Convert all searchable fields to string and lowercase for case-insensitive search
                const searchInFields = [
                    execution.sequenceId?.toString(),
                    execution.externalId?.toString(),
                    execution.creator?.toLowerCase(),
                    execution.executionState?.toLowerCase(),
                    execution.createdDate?.toString(),
                    execution.referenceDate?.toString(),
                    execution.scheduledPublishDate?.toString(),
                    execution.publishedDate?.toString(),
                    execution.executionDate?.toString(),
                    execution.scheduledClosingDate?.toString(),
                    execution.closedDate?.toString(),
                    execution.scheduledArchiveDate?.toString(),
                    execution.archivedDate?.toString(),
                ];
                // Check if any field includes the searchTerm
                return searchInFields.some(
                    (field) => field && field.toLowerCase().includes(searchTerm)
                );
            });
        },
        isResultsDownloadLogEmpty(execution: RequestExecution): boolean {
            return execution.resultsDownloadLog.length === 0;
        },
        isSequenceIdAnchoroed(sequenceId: Number): boolean {
            return sequenceId === this.anchoredSequenceIdRef;
        },
        toggleArchivedVisibility() {
            this.showArchived = !this.showArchived;
            this.filterDataTableExecutions(""); // Re-filter the executions to apply the new toggle state
        },
    },
};
</script>
