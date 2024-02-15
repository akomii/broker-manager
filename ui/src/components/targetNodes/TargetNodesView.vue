<template>
    <Fieldset :legend="$t('targetNodes')" :class="fieldSetHeight">
        <TargetNodesTable
            :targetNodes="[]"
            :targetNodeStatusInfos="[]"
            :sequenceId="execution.sequenceId"
            :showProcessingStateInfo="showProcessingStateInfo"
            :dataTableHeight="dataTableHeight"
        />
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import Button from "primevue/button";
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import { ManagerNode, RequestExecution, NodeStatusInfo } from "@/utils/Types";
import MomentWrapper from "@/utils/MomentWrapper";
import ExportTableButton from "@/components/common/ExportTableButton.vue";
import SearchInput from "@/components/common/SearchInput.vue";
import TargetNodesTable from "@/components/tables/targetNodes/TargetNodesTable.vue";
import { TestDataService } from "@/services/TestDataService";

export default {
    components: {
        Fieldset,
        DataTable,
        Column,
        Button,
        EditableTagListView,
        ExportTableButton,
        SearchInput,
        TargetNodesTable,
    },
    props: {
        execution: {
            type: Object as () => RequestExecution,
            required: true,
        },
        showProcessingStateInfo: {
            type: Boolean,
            default: false,
        },
        targetNodeIds: {
            type: Array as () => number[],
            required: true,
        },
        fieldSetHeight: {
            type: String as () => string,
            default: "h-48-4rem",
        },
    },
    data() {
        return {
            dataTableNodes: [] as ManagerNode[],
            allManagerNodes: [] as ManagerNode[],
        };
    },
    computed: {
        requestExecutionOnNodes(): number {
            return this.execution.nodeStatusInfos.filter(
                (nodeInfo) => nodeInfo.completed !== null
            ).length;
        },
        dataTableHeight(): string {
            const heightNumber = parseInt(this.fieldSetHeight.split("-")[1]);
            return `h-${heightNumber - 10}-4rem`;
        },
        selectedNodes(): ManagerNode[] {
            return this.sortNodesById(
                this.allManagerNodes.filter((node) =>
                    this.targetNodeIds.includes(node.id)
                )
            );
        },
        availableNodes(): ManagerNode[] {
            return this.sortNodesById(
                this.allManagerNodes.filter(
                    (node) => !this.targetNodeIds.includes(node.id)
                )
            );
        },
    },
    async mounted() {
        await this.fetchManagerNodes();
        this.filterDataTableNodes("");
    },
    methods: {
        async fetchManagerNodes(): Promise<void> {
            this.allManagerNodes = await TestDataService.getNodes();
        },
        sortNodesById(nodes: ManagerNode[]): ManagerNode[] {
            return nodes.sort((a, b) => a.id - b.id);
        },
        // TODO add filter by german date
        filterNodesBySearchTerm(
            nodes: ManagerNode[],
            searchTerm: string
        ): ManagerNode[] {
            return nodes.filter(
                (node) =>
                    node.id.toString().includes(searchTerm) ||
                    node.tags.some((tag) =>
                        tag.toLowerCase().includes(searchTerm)
                    ) ||
                    node.clientDN.CN.toLowerCase().includes(searchTerm)
            );
        },
        filterDataTableNodes(searchTerm: string) {
            this.dataTableNodes = this.filterNodesBySearchTerm(
                this.selectedNodes,
                searchTerm.toLowerCase()
            );
        },
        formatDateToGermanLocale(date: Date): string {
            return MomentWrapper.formatDateToGermanLocale(date);
        },
        showStatusMessage(nodeId: number) {
            const nodeInfo = this.getNodeStatusInfoForNode(nodeId);
            if (nodeInfo?.statusMessage) {
                const newWindow = window.open("", "_blank");
                if (newWindow) {
                    newWindow.document.write(
                        `<pre>${nodeInfo.statusMessage}</pre>`
                    );
                    newWindow.document.title = this.$t("nodeStatusMessage", {
                        nodeId: nodeId,
                    });
                    newWindow.document.close();
                }
            }
        },
        getNodeStatusInfoForNode(nodeId: number): NodeStatusInfo {
            return (
                this.execution.nodeStatusInfos.find(
                    (info) => info.nodeId === nodeId
                ) || ({} as NodeStatusInfo)
            );
        },
    },
};
</script>
