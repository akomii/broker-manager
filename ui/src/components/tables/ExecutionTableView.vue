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
                            {{ $t("showArchivedRequests") }}
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
            <Column field="sequenceId" :header="$t('sequenceId')" sortable>
                <template #body="slotProps">
                    <div class="flex gap-1">
                        <span>
                            {{ slotProps.data.sequenceId }}
                        </span>
                        <AnchoredRequestIcon
                            v-if="
                                isSequenceIdAnchoroed(slotProps.data.sequenceId)
                            "
                        />
                        <NoDownloadedResultsIcon
                            v-if="isResultsDownloadLogEmpty(slotProps.data)"
                        />
                    </div>
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

            <Column
                field="acceptance"
                :header="$t('acceptance')"
                bodyStyle="text-align: center"
            >
                <template #body="slotProps">
                    <TargetNodesViewDialog
                        :execution="slotProps.data"
                        :showProcessingStateInfo="true"
                    />
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
import { RequestExecution } from "@/utils/Types";
import { ExecutionState } from "@/utils/Enums";
import ExecutionStateLabel from "@/components/labels/ExecutionStateLabel.vue";
import DateView from "@/components/datePickers/DateView.vue";
import ScheduledDateView from "@/components/datePickers/ScheduledDateView.vue";
import MenuButton from "@/components/common/MenuButton.vue";
import ExportTableButton from "@/components/common/ExportTableButton.vue";
import SearchInput from "@/components/common/SearchInput.vue";
import AnchoredRequestIcon from "./AnchoredRequestIcon.vue";
import NoDownloadedResultsIcon from "./NoDownloadedResultsIcon.vue";
import Checkbox from "primevue/checkbox";
import TargetNodesViewDialog from "./TargetNodesViewDialog.vue";
import MomentWrapper from "@/utils/MomentWrapper.ts";

// TODO Table Export

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
            dataTableExecutions: this.executions,
            showArchived: false,
            currentSearchTerm: "",
        };
    },
    mounted() {
        this.filterDataTableExecutions();
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
        isSequenceIdAnchoroed(sequenceId: Number): boolean {
            return sequenceId === this.anchoredSequenceIdRef;
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
    },
};
</script>
