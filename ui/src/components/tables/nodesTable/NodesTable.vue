<template>
    <DataTable
        :value="filteredNodes"
        sortField="id"
        :sortOrder="1"
        ref="nodesTable"
        paginator
        :rows="10"
        :rowsPerPageOptions="[10, 25, 50]"
    >
        <template #header>
            <NodesTableHeader
                @update:showHidden="showHidden = $event"
                @search="filterNodes"
            />
        </template>

        <!-- COLUMNS HERE-->

        <template #empty>
            <p class="flex justify-content-center">
                {{ $t("noNodesFound") }}
            </p>
        </template>
        <template #paginatorstart>
            <span>{{ nodesCountMessage }}</span>
        </template>
        <template #paginatorend>
            <ExportTableButton class="mt-3" :dt="$refs.nodesTable" />
        </template>
    </DataTable>
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import NodesTableHeader from "@/components/tables/nodesTable/NodesTableHeader.vue";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";

export default {
    components: {
        DataTable,
        Column,
        NodesTableHeader,
        ExportTableButton,
    },
    props: {
        nodes: {
            type: Array as () => ManagerNode[],
            required: true,
        },
    },
    data() {
        return {
            showHidden: false,
            currentSearchTerm: "",
        };
    },
    computed: {
        filteredNodes(): ManagerNode[] {
            //TODO
            return this.nodes;
        },
        nodesCountMessage(): string {
            const count = this.nodes.length;
            return count === 1
                ? this.$t("oneNode")
                : this.$t("xNodes", { numNodes: count });
        },
    },
    methods: {
        filterNodes(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },
    },
};
</script>
