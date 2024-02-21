<template>
    <DataTable
        :value="filteredNodeRequests"
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
                @search="filterNodeRequests"
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
import { NodeRequestsTableElement } from "@/utils/TableElements.ts";

export default {
    components: {
        DataTable,
        Column,
        NodeRequestsTableHeader,
        ExportTableButton,
    },
    props: {
        nodeRequests: {
            type: Array as () => NodeRequestsTableElement[],
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
        filteredNodeRequests(): ManagerRequest[] {
            return this.nodeRequests;
        },
        nodeRequestsCountMessage(): string {
            const count = this.nodeRequests.length;
            return count === 1
                ? this.$t("oneRequest")
                : this.$t("xRequests", { numRequests: count });
        },
    },

    methods: {
        filterNodeRequests(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },
    },
};
</script>
