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
        <!-- TODO outsource Node header when necessary -->
        <template #header>
            <div
                class="flex flex-wrap justify-content-between align-items-center"
            >
                <span
                    v-if="showProcessingStateInfo"
                    class="font-semibold text-primary text-xl m-0"
                >
                    {{
                        $t("nodeAcceptanceOfExecution", {
                            executionId: sequenceId,
                        })
                    }}
                    :
                    {{
                        $t("XofY", {
                            x: completedNodeStatusCount,
                            y: targetNodeStatusInfos.length,
                        })
                    }}
                </span>
                <SearchInput @update:input="filterEnrichedTableNodes" />
            </div>
        </template>
        <ColumnNodeId key="nodeId" />
        <ColumnCommonName key="commonName" />
        <ColumnNodeTags key="nodeTags" />
        <ColumnLastContact key="lastContact" />
        <ColumnsNodeProcessingState
            v-if="showProcessingStateInfo"
            key="nodeProcessingState"
        />
        <template #empty>
            <p class="flex justify-content-center">
                {{ $t("noNodesFound") }}
            </p>
        </template>
        <template #footer>
            <TableFooter :refDataTable="$refs.targetNodesTable">
                <template #count-message>
                    {{ nodesCountMessage }}
                </template>
            </TableFooter>
        </template>
    </DataTable>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import Button from "primevue/button";
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import { ManagerNode, NodeStatusInfo } from "@/utils/Types";
import MomentWrapper from "@/utils/MomentWrapper";
import ExportTableButton from "@/components/common/ExportTableButton.vue";
import SearchInput from "@/components/common/SearchInput.vue";
import NodeStatusInfoTimelineButton from "@/components/buttons/NodeStatusInfoTimelineButton.vue";
import TableFooter from "@/components/tables/TableFooter.vue";
import ColumnNodeId from "@/components/tables/nodeColumns/ColumnNodeId.vue";
import ColumnCommonName from "@/components/tables/nodeColumns/ColumnCommonName.vue";
import ColumnNodeTags from "@/components/tables/nodeColumns/ColumnNodeTags.vue";
import ColumnLastContact from "@/components/tables/nodeColumns/ColumnLastContact.vue";
import ColumnsNodeProcessingState from "@/components/tables/targetNodes/ColumnsNodeProcessingState.vue";

export default {
    components: {
        Fieldset,
        DataTable,
        Column,
        Button,
        EditableTagListView,
        ExportTableButton,
        SearchInput,
        NodeStatusInfoTimelineButton,
        TableFooter,
        ColumnNodeId,
        ColumnCommonName,
        ColumnNodeTags,
        ColumnLastContact,
        ColumnsNodeProcessingState,
    },
    props: {
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
            type: String as () => string,
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
                    node.clientDN.CN,
                    node.tags.map((tag) => tag).join(" "), //TODO CHECK THIS
                    MomentWrapper.formatDateToGermanLocale(node.lastContact),
                ];
                return searchFields.some((field) =>
                    field
                        ?.toLowerCase()
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
