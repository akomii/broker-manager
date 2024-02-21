<template>
    <DataTable
        :value="filteredNodeExecutions"
        sortField="id"
        :sortOrder="1"
        ref="nodeExecutionsTable"
        paginator
        :rows="10"
        :rowsPerPageOptions="[10, 25, 50]"
        rowGroupMode="rowspan"
        :groupRowsBy="['id', 'query.title']"
    >
        <template #header>
            <NodeExecutionsTableHeader
                @update:showArchived="showArchived = $event"
                @search="filterNodeExecutions"
            />
        </template>

        <ColumnRequestId key="requestId" />
        <ColumnTitle key="title" />
        <ColumnSequenceId key="sequenceId" />
        <ColumnExternalId key="externalId" />
        <ColumnExecutionState key="executionState" />
        <ColumnReferenceDate key="referenceDate" />
        <ColumnsNodeProcessingState key="nodeProcessingState" />

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
import ColumnTitle from "@/components/tableColumns/managerRequestColumns/ColumnTitle.vue";
import ColumnSequenceId from "@/components/tableColumns/requestExecutionColumns/ColumnSequenceId.vue";
import ColumnExternalId from "@/components/tableColumns/requestExecutionColumns/ColumnExternalId.vue";
import ColumnExecutionState from "@/components/tableColumns/requestExecutionColumns/ColumnExecutionState.vue";
import ColumnReferenceDate from "@/components/tableColumns/requestExecutionColumns/ColumnReferenceDate.vue";
// Watch out! This import happens from tables/targetNodesTable
import ColumnsNodeProcessingState from "@/components/tables/targetNodesTable/ColumnsNodeProcessingState.vue";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";
import { NodeExecutionsTableElement } from "@/utils/TableElements.ts";

export default {
    components: {
        DataTable,
        Column,
        NodeExecutionsTableHeader,
        ColumnRequestId,
        ColumnTitle,
        ColumnSequenceId,
        ColumnExternalId,
        ColumnExecutionState,
        ColumnReferenceDate,
        ColumnsNodeProcessingState,
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
