<template>
    <Fieldset :legend="$t('targetNodes')" :toggleable="true">
        <template v-if="editable">
            <!--
        <EditTargetNodes
            :modelValue="targetNodeIds"
            @update:modelValue="onTargetNodeIdsChange"
            :allManagerNodes="allManagerNodes"
        />
        -->
        </template>
        <template v-else>
            <div class="flex justify-content-between flex-wrap">
                <div class="flex align-items-center">
                    <p class="font-semibold text-primary text-2xl m-0">
                        [{{ execution.sequenceId }}] {{ $t("acceptance") }}:
                        {{ requestExecutionOnNodes }} /
                        {{ execution.nodeStatusInfos.length }}
                    </p>
                </div>
                <div class="flex align-items-center">
                    <SearchInput @update:input="updateGlobalFilter" />
                </div>
            </div>

            <DataTable
                ref="dt"
                v-model:filters="filters"
                :value="selectedNodes"
                sortField="id"
                :sortOrder="1"
                :globalFilterFields="[
                    'id',
                    'clientDN.L',
                    'tags',
                    'lastContact',
                    'state',
                ]"
                scrollable
            >
                <Column field="id" :header="$t('id')" sortable />
                <Column field="clientDN.L" :header="$t('node')" sortable />
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
                    <Column field="msg" header="" class="border-solid">
                        <template #body="slotProps">
                            <Button
                                v-if="
                                    getNodeStatusInfoForNode(slotProps.data.id)
                                        .statusMessage
                                "
                                @click="showStatusMessage(slotProps.data.id)"
                                icon="pi pi-exclamation-circle text-xl text-blue-600 m-0"
                                text
                                rounded
                            />
                        </template>
                    </Column>
                </template>
            </DataTable>
            <!-- TODO make globalFilterFields conditionally -->
            <!-- TODO Search does not work for state -->
            <!-- TODO Search does not work for date -->

            <!-- Filter by Tag does not work -->
            <!-- ToDo Somehow does not work???-->
            <ExportTableButton class="mt-3" :datatableRef="$refs.dt" />
        </template>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import Button from "primevue/button";
import { FilterMatchMode } from "primevue/api";

import { ManagerNode, RequestExecution, NodeStatusInfo } from "@/utils/Types";
import { RequestState } from "@/utils/Enums";
import MomentWrapper from "@/utils/MomentWrapper";
import { TestDataService } from "@/services/TestDataService";
import TagList from "@/components/tags/TagList.vue";
import ExportTableButton from "./ExportTableButton.vue";
import EditTargetNodes from "./EditTargetNodes.vue";
import SearchInput from "./SearchInput.vue";
import NodeStatusInfoTimeline from "./NodeStatusInfoTimeline.vue";

export default {
    components: {
        Fieldset,
        DataTable,
        Column,
        Button,
        TagList,
        EditTargetNodes,
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
            filters: {
                global: { value: "", matchMode: FilterMatchMode.CONTAINS },
            },
        };
    },
    computed: {
        selectedNodes(): ManagerNode[] {
            return this.allManagerNodes.filter((node) =>
                this.targetNodeIds.has(node.id)
            );
        },
        requestExecutionOnNodes() {
            return this.execution.nodeStatusInfos.filter(
                (nodeInfo) => nodeInfo.completed !== null
            ).length;
        },
    },
    async mounted() {
        await this.fetchManagerNodes();
    },
    methods: {
        async fetchManagerNodes(): Promise<void> {
            this.allManagerNodes = await TestDataService.getNodes();
        },
        formatDateToGermanLocale(date: Date): string {
            return MomentWrapper.formatDateToGermanLocale(date);
        },
        updateGlobalFilter(searchTerm: string) {
            this.filters.global.value = searchTerm;
        },
        onTargetNodeIdsChange(newModelValue: ManagerNode[]) {
            this.$emit("update:targetNodeIds", newModelValue);
        },
        isNotDraft(): boolean {
            return !(this.requestState === RequestState.DRAFT);
        },
        getNodeStatusInfoForNode(nodeId: number): NodeStatusInfo {
            return this.execution.nodeStatusInfos.find(
                (info) => info.nodeId === nodeId
            );
        },
        showStatusMessage(nodeId: number) {
            const nodeInfo = this.getNodeStatusInfoForNode(nodeId);
            if (nodeInfo && nodeInfo.statusMessage) {
                const newWindow = window.open("", "_blank");
                newWindow.document.write(
                    `<pre>${nodeInfo.statusMessage}</pre>`
                );
                newWindow.document.title = `Status Message for Node ${nodeId}`;
                newWindow.document.close();
            }
        },
    },
};
</script>
