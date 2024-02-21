<template>
    <DataTable
        :value="filteredEnrichedNodeRequests"
        sortField="id"
        :sortOrder="1"
        ref="nodeRequestsTable"
        paginator
        :rows="10"
        :rowsPerPageOptions="[10, 25, 50]"
    >
        <template #header>
            <NodeRequestsTableHeader
                @update:showArchived="showArchived = $event"
                @search="filterEnrichedNodeRequests"
            />
        </template>

        <template #empty>
            <p class="flex justify-content-center">
                {{ $t("noRequestsFound") }}
            </p>
        </template>
        <template #paginatorstart>
            <span>{{ requestsCountMessage }}</span>
        </template>
        <template #paginatorend>
            <ExportTableButton class="mt-3" :dt="$refs.nodeRequestsTable" />
        </template>
    </DataTable>
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import NodeRequestsTableHeader from "./NodeRequestsTableHeader.vue";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";
import { ManagerRequest } from "@/utils/Types";

export default {
    components: {
        DataTable,
        Column,
        NodeRequestsTableHeader,
        ExportTableButton,
    },
    // TODO CREATE NEW DATATYPE AS INPUT PROP
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
        enrichedNodeRequests(): ManagerRequest[] {
            return this.requests;
        },
        filteredEnrichedNodeRequests(): ManagerRequest[] {
            return this.enrichedNodeRequests;
        },
        requestsCountMessage(): string {
            const count = this.requests.length;
            return count === 1
                ? this.$t("oneRequest")
                : this.$t("xRequests", { numRequests: count });
        },
    },

    methods: {
        filterEnrichedNodeRequests(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },
    },
};
</script>
