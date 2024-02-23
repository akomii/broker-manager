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
        <ColumnId key="nodeId" />
        <ColumnCommonName key="commonName" />
        <ColumnLocation key="location" />
        <ColumnLastContact key="lastContact" />
        <ColumnTags key="nodeTags" />
        <ColumnNodeDetailsAction key="nodeDetailsAction" />
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
import ColumnId from "@/components/tableColumns/ColumnId.vue";
import ColumnCommonName from "@/components/tableColumns/managerNodeColumns/ColumnCommonName.vue";
import ColumnLocation from "@/components/tableColumns/managerNodeColumns/ColumnLocation.vue";
import ColumnLastContact from "@/components/tableColumns/managerNodeColumns/ColumnLastContact.vue";
import ColumnTags from "@/components/tableColumns/ColumnTags.vue";
import ColumnNodeDetailsAction from "@/components/tableColumns/managerNodeColumns/ColumnNodeDetailsAction.vue";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";
import MomentWrapper from "@/utils/MomentWrapper";

export default {
    components: {
        DataTable,
        Column,
        NodesTableHeader,
        ColumnId,
        ColumnCommonName,
        ColumnLocation,
        ColumnLastContact,
        ColumnTags,
        ColumnNodeDetailsAction,
        ExportTableButton,
    },
     // TODO CREATE NEW DATATYPE AS INPUT PROP
     // TODO refactor and add docs
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
            return this.nodes
                .filter((node) => {
                    return this.showHidden || (node.apiKey !== null && node.apiKey !== "");
                })
                .filter((node) => {
                    const searchFields = [
                        node.id.toString(),
                        node.clientDN.CN,
                        node.clientDN.L,
                        MomentWrapper.formatDateToGermanLocale(
                            node?.lastContact
                        ),
                        node.tags.join(""),
                    ];
                    return searchFields.some((field) =>
                        field
                            ?.toLowerCase()
                            .includes(this.currentSearchTerm.toLowerCase())
                    );
                });
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
