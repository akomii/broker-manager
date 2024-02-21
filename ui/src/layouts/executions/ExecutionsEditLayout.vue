<template>
    <Fieldset :legend="$t('executions')">
        <DataTable
            :value="dataTableExecutions"
            paginator
            :rows="10"
            :rowsPerPageOptions="[10, 25, 50]"
            sortField="sequenceId"
            :sortOrder="-1"
            ref="dt"
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
                    <Button
                        :label="$t('addNewExecution')"
                        icon="pi pi-plus"
                        severity="success"
                        @click="addNewRequestExecution"
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
                                isSequenceIdAnchored(slotProps.data.sequenceId)
                            "
                        />
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
                    <DatePick
                        class="w-12rem"
                        :date="slotProps.data.referenceDate"
                        :disabled="isExecutionAfterPending(slotProps.data)"
                        @update:date="slotProps.data.referenceDate = $event"
                    />
                </template>
            </Column>
            <Column
                field="publishDate"
                :header="$t('dates.publishDate')"
                sortable
            >
                <template #body="slotProps">
                    <DatePick
                        class="w-12rem"
                        :date="slotProps.data.scheduledPublishDate"
                        :disabled="isExecutionAfterPending(slotProps.data)"
                        @update:date="
                            slotProps.data.scheduledPublishDate = $event
                        "
                    />
                </template>
            </Column>
            <Column
                field="executionDate"
                :header="$t('dates.executionDate')"
                sortable
            >
                <template #body="slotProps">
                    <DatePick
                        class="w-12rem"
                        :date="slotProps.data.executionDate"
                        :disabled="isExecutionAfterPending(slotProps.data)"
                        @update:date="slotProps.data.executionDate = $event"
                    />
                </template>
            </Column>
            <Column
                field="closingDate"
                :header="$t('dates.closingDate')"
                sortable
            >
                <template #body="slotProps">
                    <DatePick
                        class="w-12rem"
                        :date="slotProps.data.scheduledClosingDate"
                        :disabled="isExecutionAfterPending(slotProps.data)"
                        @update:date="
                            slotProps.data.scheduledClosingDate = $event
                        "
                    />
                </template>
            </Column>
            <Column
                field="archiveDate"
                :header="$t('dates.archiveDate')"
                sortable
            >
                <template #body="slotProps">
                    <DatePick
                        class="w-12rem"
                        :date="slotProps.data.scheduledArchiveDate"
                        :disabled="isExecutionAfterPending(slotProps.data)"
                        @update:date="
                            slotProps.data.scheduledArchiveDate = $event
                        "
                    />
                </template>
            </Column>

            <ConfirmPopup></ConfirmPopup>
            <Column field="actions" bodyStyle="text-align: center">
                <Toast />
                <template #body="slotProps">
                    <div class="flex justify-content-center gap-2">
                        <Button
                            v-if="
                                !isSequenceIdAnchored(slotProps.data.sequenceId)
                            "
                            v-tooltip.bottom="$t('anchorThisExecution')"
                            @click="setSequenceIdRef(slotProps.data.sequenceId)"
                            icon="pi pi-star"
                            outlined
                        />
                        <Button
                            v-if="slotProps.data.executionState === 'PENDING'"
                            v-tooltip.bottom="$t('deleteThisExecution')"
                            @click="
                                deleteRequestExecution(
                                    $event,
                                    slotProps.data.sequenceId
                                )
                            "
                            :disabled="
                                isSequenceIdAnchored(slotProps.data.sequenceId)
                            "
                            severity="danger"
                            icon="pi pi-trash"
                            outlined
                        />
                    </div>
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
                        executions.length === 1
                            ? $t("oneExecution")
                            : $t("xExecutions", {
                                  numExecutions: executions
                                      ? executions.length
                                      : 0,
                              })
                    }}
                </span>
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
import DateView from "@/components/timeWidgets/DateView.vue";
import ScheduledDateView from "@/components/timeWidgets/ScheduledDateView.vue";
import MenuButton from "@/components/buttons/MenuButton.vue";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";
import SearchInput from "@/components/tables/SearchInput.vue";
import AnchoredRequestIcon from "@/components/icons/AnchoredRequestIcon.vue";
import ResultsNotDownloadedIcon from "@/components/icons/ResultsNotDownloadedIcon.vue";
import Checkbox from "primevue/checkbox";
import MomentWrapper from "@/utils/MomentWrapper.ts";
import Button from "primevue/button";
import Toast from "primevue/toast";
import ConfirmPopup from "primevue/confirmpopup";
import DatePick from "@/components/timeWidgets/DatePick.vue";


// TODO REFACTOOR
//TODO sorting with scheduled and actual dates does not work

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
        Button,
        Toast,
        ConfirmPopup,
        DatePick,
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
        isSequenceIdAnchored(sequenceId: Number): boolean {
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
        // TODO wrong dates are shown in table
        addNewRequestExecution() {
            const newRequestExecution: RequestExecution = {
                sequenceId: this.executions.length + 1,
                externalId: null,
                referenceDate: new Date(),
                executionDate: new Date(),
                scheduledPublishDate: new Date(),
                publishedDate: null,
                scheduledClosingDate: new Date(),
                closedDate: null,
                scheduledArchiveDate: new Date(),
                archivedDate: null,
                creator: "NewCreator", // TODO current user
                createdDate: new Date(),
                executionState: ExecutionState.PENDING,
                nodeStatusInfos: [],
                resultsDownloadLog: [],
            };
            const updatedExecutions = [...this.executions, newRequestExecution];
            this.$emit("update:executions", updatedExecutions);
            this.$toast.add({
                severity: "success",
                detail: this.$t("addedNewExecution"),
                life: 3000,
            });
            this.$nextTick(() => {
                this.filterDataTableExecutions();
            });
        },
        setSequenceIdRef(sequenceId: Number) {
            this.$emit("update:anchoredSequenceIdRef", sequenceId);
            this.$toast.add({
                severity: "info",
                detail: this.$t("setAnchorToSequenceId", {
                    sequenceId: sequenceId,
                }),
                life: 3000,
            });
        },
        deleteRequestExecution(event, sequenceId: number) {
            this.$confirm.require({
                target: event.currentTarget,
                message: this.$t("doYouWantToDeleteThisExecution"),
                icon: "pi pi-info-circle",
                rejectClass: "p-button-outlined p-button-sm",
                acceptClass: "p-button-danger p-button-sm",
                rejectLabel: this.$t("cancel"),
                acceptLabel: this.$t("delete"),
                accept: () => {
                    const updatedExecutions = this.executions.filter(
                        (execution) => execution.sequenceId !== sequenceId
                    );
                    this.$emit("update:executions", updatedExecutions);
                    this.$toast.add({
                        severity: "success",
                        detail: this.$t("deletedExecutionWithSequenceId", {
                            sequenceId: sequenceId,
                        }),
                        life: 3000,
                    });
                    this.$nextTick(() => {
                        this.filterDataTableExecutions();
                    });
                },
            });
        },
        isExecutionAfterPending(execution: RequestExecution): boolean {
            return execution.executionState !== ExecutionState.PENDING;
        },
    },
};
</script>
