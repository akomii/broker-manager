<template>
    <DataTable
        :value="tableElements"
        :sortField="sortField"
        :sortOrder="sortOrder"
        :ref="tableId"
        paginator
        :rows="rowsPerPage"
        :rowsPerPageOptions="rowsPerPageOptions"
        :rowGroupMode="rowGroupMode"
        :groupRowsBy="['id', 'query.title']"
    >
        <template #header>
            <slot name="table-header"></slot>
        </template>
        <slot name="table-columns"></slot>
        <template #empty>
            <p class="flex justify-content-center">
                {{ emptyTableMessage }}
            </p>
        </template>
        <template #paginatorstart>
            <span>{{ tableElementsCountMessage }}</span>
        </template>
        <template #paginatorend>
            <ExportTableButton class="mt-3" :dt="$refs.tableId" />
        </template>
    </DataTable>
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";

// TODO refactor and add docs
export default {
    components: {
        DataTable,
        ExportTableButton,
    },
    props: {
        tableElements: {
            type: Array,
            required: true,
        },
        tableId: {
            type: String,
            required: true,
        },
        sortField: {
            type: String,
            default: "id",
        },
        sortOrder: {
            type: Number,
            default: 1,
        },
        rowsPerPage: {
            type: Number,
            default: 10,
        },
        rowsPerPageOptions: {
            type: Array<number>,
            default: [10, 25, 50],
        },
        rowGroupMode: {
            type: String,
            default: undefined,
            validator: function (value) {
                return (
                    value === undefined ||
                    value === "rowspan" ||
                    value === "subheader"
                );
            },
        },
        groupByRows: {
            type: Array<string>,
            default: undefined,
        },
        emptyTableMessage: String,
        tableElementsCountMessage: String,
    },
    data() {},
};
</script>
