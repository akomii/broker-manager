<template>
    <DataTable
        :value="filteredNodeExecutions"
        sortField="id"
        :sortOrder="1"
        ref="nodeExecutionsTable"
        paginator
        :rows="10"
        :rowsPerPageOptions="[10, 25, 50]"
    >
        <template #header>
            <NodeExecutionsTableHeader
                @update:showArchived="showArchived = $event"
                @search="filterNodeExecutions"
            />
        </template>

        <ColumnRequestId key="requestId" />


        <template #empty>
            <p class="flex justify-content-center">
                {{ $t("noExecutionsFound") }}
            </p>
        </template>
        <template #paginatorstart>
            <span>{{ nodeExecutionsCountMessage }}</span>
        </template>
        <template #paginatorend>
            <ExportTableButton class="mt-3" :dt="$refs.nodeExecutionsTable" />
        </template>
    </DataTable>
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import NodeExecutionsTableHeader from "./NodeExecutionsTableHeader.vue";
import ColumnRequestId from "@/components/tableColumns/managerRequestColumns/ColumnRequestId.vue";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";
import { NodeExecutionsTableElement } from "@/utils/TableElements.ts";

export default {
    components: {
        DataTable,
        Column,
        NodeExecutionsTableHeader,
        ColumnRequestId,
        ExportTableButton,
    },
    props: {
        nodeExecutions: {
            type: Array as () => NodeExecutionsTableElement[],
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
        filteredNodeExecutions(): NodeExecutionsTableElement[] {
            return this.nodeExecutions;
        },
        nodeExecutionsCountMessage(): string {
            const count = this.nodeExecutions.length;
            return count === 1
                ? this.$t("oneExecution")
                : this.$t("xExecutions", { numExecutions: count });
        },
    },

    methods: {
        filterNodeExecutions(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },
    },
};
</script>
