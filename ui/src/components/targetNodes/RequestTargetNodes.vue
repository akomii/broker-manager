<template>
    <Fieldset :legend="$t('targetNodes')" :toggleable="true">
        <template v-if="editable">
            <SearchInput
                class="flex flex-wrap justify-content-end mb-2"
                @update:input="filterPickListNodes"
            />
            <!-- TODO Optional: red/green color for items in source/target -->
            <!-- TOOD order by id when moving-->
            <!-- TODO increase height -->
            <PickList
                v-model="pickListNodes"
                @update:modelValue="handleListChange"
                dataKey="id"
                :showSourceControls="false"
                :showTargetControls="false"
                :targetListProps="{ class: 'h-30rem' }"
                :sourceListProps="{ class: 'h-30rem' }"
                :moveToTargetProps="{ class: 'bg-red-600' }"
                :moveAllToTargetProps="{ class: 'bg-red-600' }"
                :moveToSourceProps="{ class: 'bg-green-500' }"
                :moveAllToSourceProps="{ class: 'bg-green-500' }"
            >
                <template #sourceheader>{{ $t("selectedNodes") }}</template>
                <template #targetheader>{{ $t("availableNodes") }}</template>
                <template #item="slotProps">
                    <div class="flex flex-wrap flex-column">
                        <span class="font-bold">
                            [{{ slotProps.item.id }}]
                            {{ slotProps.item.clientDN.CN }}
                        </span>
                        <span class="flex flex-wrap">
                            <TagList :tags="slotProps.item.tags" />
                        </span>
                    </div>
                </template>
            </PickList>
        </template>
        <template v-else>
            <div class="flex flex-wrap justify-content-between mb-2">
                <div class="flex align-items-center">
                    <p
                        v-if="isNotDraft()"
                        class="font-semibold text-primary text-2xl m-0"
                    >
                        {{ $t("executionAcceptanceOfNode") }} [{{
                            execution.sequenceId
                        }}] : {{ requestExecutionOnNodes }} /
                        {{ execution.nodeStatusInfos.length }}
                    </p>
                </div>
                <SearchInput @update:input="filterDataTableNodes" />
            </div>

            <DataTable
                ref="dt"
                class="h-34rem"
                :value="dataTableNodes"
                sortField="id"
                :sortOrder="1"
                scrollable
                scrollHeight="500px"
            >
                <Column field="id" :header="$t('id')" sortable />
                <Column field="clientDN.CN" :header="$t('node')" sortable />
                <Column field="tags" :header="$t('tags')" sortable>
                    <template #body="slotProps">
                        <TagList :tags="slotProps.data.tags" />
                    </template>
                </Column>
                <Column
                    field="lastContact"
                    :header="$t('lastContact')"
                    sortable
                >
                    <template #body="slotProps">
                        {{
                            formatDateToGermanLocale(slotProps.data.lastContact)
                        }}
                    </template>
                </Column>
                <template v-if="isNotDraft()">
                    <Column field="state" :header="$t('processingState')">
                        <template #body="slotProps">
                            <NodeStatusInfoTimeline
                                :nodeStatusInfo="
                                    getNodeStatusInfoForNode(slotProps.data.id)
                                "
                            />
                        </template>
                    </Column>
                    <Column field="msg" header="">
                        <template #body="slotProps">
                            <Button
                                v-if="
                                    getNodeStatusInfoForNode(slotProps.data.id)
                                        .statusMessage
                                "
                                @click="showStatusMessage(slotProps.data.id)"
                                icon="pi pi-exclamation-circle text-xl text-blue-600"
                                text
                                rounded
                            />
                        </template>
                    </Column>
                </template>
                <!-- TODO edge case if only 1 node -->
                <template #footer>
                    {{
                        $t("xNodes", {
                            numNodes: selectedNodes ? selectedNodes.length : 0,
                        })
                    }}
                </template>
            </DataTable>
            <!-- ToDo export table does not work???-->
            <ExportTableButton class="mt-3" :datatableRef="$refs.dt" />
        </template>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import Button from "primevue/button";
import PickList from "primevue/picklist";

import { ManagerNode, RequestExecution, NodeStatusInfo } from "@/utils/Types";
import { RequestState } from "@/utils/Enums";
import MomentWrapper from "@/utils/MomentWrapper";
import { TestDataService } from "@/services/TestDataService";
import TagList from "@/components/tags/TagList.vue";
import ExportTableButton from "./ExportTableButton.vue";
import SearchInput from "./SearchInput.vue";
import NodeStatusInfoTimeline from "./NodeStatusInfoTimeline.vue";

export default {
    components: {
        Fieldset,
        DataTable,
        Column,
        Button,
        PickList,
        TagList,
        ExportTableButton,
        SearchInput,
        NodeStatusInfoTimeline,
    },
    props: {
        targetNodeIds: {
            type: Set<number>,
            required: true,
        },
        execution: {
            type: Object as () => RequestExecution,
            required: true,
        },
        requestState: {
            type: String as () => keyof typeof RequestState,
            required: true,
        },
        editable: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            allManagerNodes: [] as ManagerNode[],
            pickListNodes: [[] as ManagerNode[], [] as ManagerNode[]],
            dataTableNodes: [] as ManagerNode[],
        };
    },
    computed: {
        selectedNodes(): ManagerNode[] {
            return this.allManagerNodes
                .filter((node) => this.targetNodeIds.has(node.id))
                .sort((a, b) => a.id - b.id);
        },
        availableNodes(): ManagerNode[] {
            return this.allManagerNodes
                .filter((node) => !this.targetNodeIds.has(node.id))
                .sort((a, b) => a.id - b.id);
        },
        requestExecutionOnNodes() {
            return this.execution.nodeStatusInfos.filter(
                (nodeInfo) => nodeInfo.completed !== null
            ).length;
        },
    },
    async mounted() {
        await this.fetchManagerNodes();
        this.filterPickListNodes("");
        this.filterDataTableNodes("");
    },
    methods: {
        async fetchManagerNodes(): Promise<void> {
            this.allManagerNodes = await TestDataService.getNodes();
        },
        formatDateToGermanLocale(date: Date): string {
            return MomentWrapper.formatDateToGermanLocale(date);
        },
        filterDataTableNodes(searchTerm: string) {
            this.dataTableNodes = this.filterNodesBySearchTerm(
                this.selectedNodes,
                searchTerm
            );
        },
        filterPickListNodes(searchTerm: string) {
            const filteredSelectedNodes = this.filterNodesBySearchTerm(
                this.selectedNodes,
                searchTerm
            );
            const filteredAvailableNodes = this.filterNodesBySearchTerm(
                this.availableNodes,
                searchTerm
            );
            this.pickListNodes = [
                filteredSelectedNodes,
                filteredAvailableNodes,
            ];
        },
        // TODO add filter by german date
        filterNodesBySearchTerm(
            nodes: ManagerNode[],
            searchTerm: string
        ): ManagerNode[] {
            if (!searchTerm) return nodes;
            return nodes.filter(
                (node) =>
                    node.id
                        .toString()
                        .toLowerCase()
                        .includes(searchTerm.toLowerCase()) ||
                    [...node.tags].some((tag) =>
                        tag.toLowerCase().includes(searchTerm.toLowerCase())
                    ) ||
                    node.clientDN.CN.toLowerCase().includes(
                        searchTerm.toLowerCase()
                    )
            );
        },
        handleListChange() {
            let updatedNodeIds = [...this.targetNodeIds];
            this.pickListNodes[0].forEach((node) => {
                if (!updatedNodeIds.includes(node.id)) {
                    updatedNodeIds.push(node.id);
                }
            });
            updatedNodeIds = updatedNodeIds.filter(
                (id) => !this.pickListNodes[1].some((node) => node.id === id)
            );
            this.$emit("update:targetNodeIds", updatedNodeIds);
        },
        isNotDraft(): boolean {
            return !(this.requestState === RequestState.DRAFT);
        },
        getNodeStatusInfoForNode(nodeId: number): NodeStatusInfo {
            return (
                this.execution.nodeStatusInfos.find(
                    (info) => info.nodeId === nodeId
                ) || ({} as NodeStatusInfo)
            );
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
    },
};
</script>
