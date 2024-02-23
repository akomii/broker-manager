<template>
    <DataTable
        :class="dataTableHeight"
        :value="filteredEnrichedTableNodes"
        sortField="id"
        :sortOrder="1"
        scrollable
        scrollHeight="flex"
        ref="targetNodesTable"
    >
        <template #header>
            <TargetNodesTableHeader
                :targetNodesLength="targetNodes.length"
                :completedNodeStatusCount="completedNodeStatusCount"
                :sequenceId="sequenceId"
                :showProcessingStateInfo="showProcessingStateInfo"
                @search="filterEnrichedTableNodes"
            />
        </template>
        <ColumnId key="nodeId" />
        <ColumnCommonName key="commonName" />
        <ColumnTags key="tags" />
        <ColumnLastContact key="lastContact" />
        <ColumnsNodeProcessingState
            v-if="showProcessingStateInfo"
            key="nodeProcessingState"
        />
        <template #empty>
            <p class="flex justify-content-center">{{ $t("noNodesFound") }}</p>
        </template>
        <template #footer>
            <TableFooter :refDataTable="$refs.targetNodesTable">
                <template #count-message>{{ nodesCountMessage }}</template>
            </TableFooter>
        </template>
    </DataTable>
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import TargetNodesTableHeader from "@/components/tables/targetNodesTable/TargetNodesTableHeader.vue";
import SearchInput from "@/components/tables/SearchInput.vue";
import TableFooter from "@/components/tables/TableFooter.vue";
import ColumnNodeId from "@/components/tableColumns/ColumnId.vue";
import ColumnCommonName from "@/components/tableColumns/managerNodeColumns/ColumnCommonName.vue";
import ColumnTags from "@/components/tableColumns/ColumnTags.vue";
import ColumnLastContact from "@/components/tableColumns/managerNodeColumns/ColumnLastContact.vue";
import ColumnsNodeProcessingState from "@/components/tables/targetNodesTable/ColumnsNodeProcessingState.vue";
import MomentWrapper from "@/utils/MomentWrapper";
import { ManagerNode, NodeStatusInfo } from "@/utils/Types";

export default {
    components: {
        DataTable,
        TargetNodesTableHeader,
        SearchInput,
        TableFooter,
        ColumnNodeId,
        ColumnCommonName,
        ColumnTags,
        ColumnLastContact,
        ColumnsNodeProcessingState,
    },
    props: {
        // TODO CREATE NEW DATATYPE AS INPUT PROP
        // TODO refactor and add docs
        targetNodes: {
            type: Array as () => ManagerNode[],
            required: true,
        },
        targetNodeStatusInfos: {
            type: Object as () => NodeStatusInfo[],
            required: true,
        },
        sequenceId: {
            type: Number,
            required: true,
        },
        showProcessingStateInfo: {
            type: Boolean,
            default: false,
        },
        dataTableHeight: {
            type: String,
            required: true,
        },
    },
    data() {
        return {
            currentSearchTerm: "",
        };
    },
    computed: {
        enrichedTargetNodes(): ManagerNode[] {
            return this.targetNodes.map((node) => {
                const nodeStatusInfo = this.targetNodeStatusInfos.find(
                    (info) => info.nodeId === node.id
                );
                return {
                    ...node,
                    nodeStatusInfo: nodeStatusInfo,
                };
            });
        },
        filteredEnrichedTableNodes(): ManagerNode[] {
            return this.enrichedTargetNodes.filter((node) => {
                const searchFields = [
                    node.id.toString(),
                    node.clientDN?.CN,
                    node.tags.join(" "),
                    MomentWrapper.formatDateToGermanLocale(node.lastContact),
                ];
                return searchFields.some((field) =>
                    field
                        .toLowerCase()
                        .includes(this.currentSearchTerm.toLowerCase())
                );
            });
        },
        completedNodeStatusCount(): number {
            return this.targetNodeStatusInfos.filter(
                (nodeInfo) => nodeInfo.completed !== null
            ).length;
        },
        nodesCountMessage(): string {
            const count = this.targetNodes.length;
            return count === 1
                ? this.$t("oneNode")
                : this.$t("xNodes", { numNodes: count });
        },
    },
    methods: {
        filterEnrichedTableNodes(searchTerm: string = "") {
            this.currentSearchTerm = searchTerm;
        },
    },
};
</script>
