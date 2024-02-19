<template>
    <DataTable
        :value="requests"
        sortField="id"
        :sortOrder="1"
        ref="requestsTable"
        paginator
        :rows="10"
        :rowsPerPageOptions="[10, 25, 50]"
    >
        <template #header>
            <RequestTableHeader
                @update:showArchived="showArchived = $event"
                @search="filterRequests"
            />
        </template>
        <ColumnRequestId key="requestId" />
        <ColumnPrincipal key="principal" />
        <ColumnTitle key="title" />
        <ColumnRequestType key="requestType" />
        <ColumnRequestState key="requestState" />
        <ColumnOrganization key="organization" />
        <ColumnCurrentPublishDate key="currentPublishDate" />
        <ColumnCurrentScheduledClosingDateVue
            key="currentScheduledClosingDate"
        />

        <!-- TODO Auswertestelle als TEXT NICHT ALS NUMBER -->

        <!-- Tags -->
        <!-- Aktuellste Zustimmung -->

        <!-- Aktionen Neuer Button??? -->

        <!-- TODO ReuqestTableHeader umbennen ExecutionTableHeader -->
        <!-- TODO Node TableHeader classe -->
        <!-- TODO RequestTableHeader, aber mit option Entwürfe zu hiden-->

        <template #empty>
            <p class="flex justify-content-center">
                {{ $t("noRequestsFound") }}
            </p>
        </template>
        <template #paginatorstart>
            <span>{{ requestsCountMessage }}</span>
        </template>
        <template #paginatorend>
            <ExportTableButton class="mt-3" :dt="$refs.requestsTable" />
        </template>
    </DataTable>
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import RequestTableHeader from "@/components/tables/RequestTableHeader.vue";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";
import { ManagerRequest } from "@/utils/Types";
import ColumnRequestId from "@/components/tableColumns/managerRequestColumns/ColumnRequestId.vue";
import ColumnPrincipal from "@/components/tableColumns/managerRequestColumns/ColumnPrincipal.vue";
import ColumnTitle from "@/components/tableColumns/managerRequestColumns/ColumnTitle.vue";
import ColumnRequestType from "@/components/tableColumns/managerRequestColumns/ColumnRequestType.vue";
import ColumnRequestState from "@/components/tableColumns/managerRequestColumns/ColumnRequestState.vue";
import ColumnOrganization from "@/components/tableColumns/managerRequestColumns/ColumnOrganization.vue";
import ColumnCurrentPublishDate from "@/components/tableColumns/managerRequestColumns/ColumnCurrentPublishDate.vue";
import ColumnCurrentScheduledClosingDateVue from "@/components/tableColumns/managerRequestColumns/ColumnCurrentScheduledClosingDate.vue";

export default {
    components: {
        DataTable,
        Column,
        RequestTableHeader,
        ColumnRequestId,
        ColumnPrincipal,
        ColumnTitle,
        ColumnRequestType,
        ColumnRequestState,
        ColumnOrganization,
        ColumnCurrentPublishDate,
        ColumnCurrentScheduledClosingDateVue,
        ExportTableButton,
    },
    props: {
        requests: {
            type: Array as () => ManagerRequest[],
            required: true,
        },
    },
    data() {
        return {
            showArchived: false,
            currentSearchTerm: "",
        };
    },
    computed: {
        filteredRequests(): ManagerRequest[] {
            /*
            return this.requests
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
                */
        },
        requestsCountMessage(): string {
            const count = this.requests.length;
            return count === 1
                ? this.$t("oneRequest")
                : this.$t("xRequests", { numRequests: count });
        },
    },
    methods: {
        filterRequests(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },
    },
};
</script>
