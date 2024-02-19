<template>
    <DataTable
        :value="filteredEnrichedRequests"
        sortField="id"
        :sortOrder="1"
        ref="requestsTable"
        paginator
        :rows="10"
        :rowsPerPageOptions="[10, 25, 50]"
    >
        <template #header>
            <RequestsTableHeader
                @update:showArchived="showArchived = $event"
                @update:showDraft="showDraft = $event"
                @search="filterEnrichedRequests"
            />
        </template>
        <ColumnRequestId key="requestId" />
        <ColumnPrincipal key="principal" />
        <ColumnTitle key="title" />
        <ColumnRequestType key="requestType" />
        <ColumnRequestState key="requestState" />
        <ColumnOrganizationNames key="organizationNames" />
        <ColumnTags key="tags" />
        <ColumnCurrentPublishDate key="currentPublishDate" />
        <ColumnCurrentScheduledClosingDateVue
            key="currentScheduledClosingDate"
        />
        <ColumnCurrentNodeCompletion key="currentNodeCompletion" />
        <ColumnRequestDetailsAction key="requestDetailsAction" />
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
import RequestsTableHeader from "@/components/tables/requestsTable/RequestsTableHeader.vue";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";
import { ManagerRequest, RequestExecution, Organization } from "@/utils/Types";
import { RequestState, ExecutionState } from "@/utils/Enums";
import ColumnId from "@/components/tableColumns/ColumnId.vue";
import ColumnPrincipal from "@/components/tableColumns/managerRequestColumns/ColumnPrincipal.vue";
import ColumnTitle from "@/components/tableColumns/managerRequestColumns/ColumnTitle.vue";
import ColumnRequestType from "@/components/tableColumns/managerRequestColumns/ColumnRequestType.vue";
import ColumnRequestState from "@/components/tableColumns/managerRequestColumns/ColumnRequestState.vue";
import ColumnOrganizationNames from "@/components/tables/requestsTable/ColumnOrganizationNames.vue";
import ColumnCurrentPublishDate from "@/components/tables/requestsTable/ColumnCurrentPublishDate.vue";
import ColumnCurrentScheduledClosingDateVue from "@/components/tables/requestsTable/ColumnCurrentScheduledClosingDate.vue";
import ColumnTags from "@/components/tableColumns/ColumnTags.vue";
import ColumnCurrentNodeCompletion from "@/components/tables/requestsTable/ColumnCurrentNodeCompletion.vue";
import ColumnRequestDetailsAction from "@/components/tableColumns/managerRequestColumns/ColumnRequestDetailsAction.vue";
import { TestDataService } from "@/services/TestDataService";
import MomentWrapper from "@/utils/MomentWrapper";

export default {
    components: {
        DataTable,
        Column,
        RequestsTableHeader,
        ColumnRequestId: ColumnId,
        ColumnPrincipal,
        ColumnTitle,
        ColumnRequestType,
        ColumnRequestState,
        ColumnOrganizationNames,
        ColumnCurrentPublishDate,
        ColumnCurrentScheduledClosingDateVue,
        ColumnTags,
        ColumnCurrentNodeCompletion,
        ColumnRequestDetailsAction,
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
            showDraft: false,
            currentSearchTerm: "",
            allOrganizations: [] as Organization[],
        };
    },
    async mounted() {
        await this.fetchOrganizations();
    },
    computed: {
        enrichedRequests(): ManagerRequest[] {
            return this.requests.map((request) => {
                const enrichedRequest = { ...request };
                enrichedRequest.authorizedOrgsNames = this.getOrgNamesByIds(
                    enrichedRequest.authorizedOrgs
                );
                enrichedRequest.currentExecution = this.findCurrentExecution(
                    enrichedRequest.executions
                );
                return enrichedRequest;
            });
        },
        filteredEnrichedRequests(): ManagerRequest[] {
            return this.enrichedRequests
                .filter((request) => {
                    return (
                        (this.showArchived ||
                            request.requestState !== RequestState.ARCHIVED) &&
                        (this.showDraft ||
                            request.requestState !== RequestState.DRAFT)
                    );
                })
                .filter((request) => {
                    const searchFields = [
                        request.id.toString(),
                        request.query.principal.name,
                        request.query.title,
                        this.$t(`enums.requestType.${request.requestType}`),
                        this.$t(`enums.requestState.${request.requestState}`),
                        request.authorizedOrgsNames.join(" "),
                        request.tags.join(" "),
                        MomentWrapper.formatDateToGermanLocale(
                            request.currentExecution?.publishedDate
                        ),
                        MomentWrapper.formatDateToGermanLocale(
                            request.currentExecution?.scheduledClosingDate
                        ),
                    ];
                    return searchFields.some((field) =>
                        field
                            ?.toLowerCase()
                            .includes(this.currentSearchTerm.toLowerCase())
                    );
                });
        },
        requestsCountMessage(): string {
            const count = this.requests.length;
            return count === 1
                ? this.$t("oneRequest")
                : this.$t("xRequests", { numRequests: count });
        },
    },

    methods: {
        async fetchOrganizations(): Promise<void> {
            this.allOrganizations = await TestDataService.getOrganizations();
        },
        getOrgNamesByIds(orgIds: number[]): string[] {
            return orgIds.map(
                (orgId) =>
                    this.allOrganizations.find((org) => org.id === orgId)
                        ?.name || "Unknown Organization"
            );
        },
        findCurrentExecution(
            executions: RequestExecution[]
        ): RequestExecution | null {
            return executions.reduce(
                (acc: RequestExecution | null, cur: RequestExecution) => {
                    if (
                        cur.executionState !== ExecutionState.PUBLISHED ||
                        cur.publishedDate === null
                    ) {
                        return acc;
                    }
                    const curDate = new Date(cur.publishedDate);
                    const accDate = acc
                        ? new Date(acc.publishedDate as Date)
                        : null;
                    return !acc || curDate > accDate ? cur : acc;
                },
                null
            );
        },
        filterEnrichedRequests(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },
    },
};
</script>
