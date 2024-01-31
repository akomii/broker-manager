<template>
    <Fieldset :legend="$t('targetNodes')" :class="fieldSetHeight">
        <div class="flex flex-wrap justify-content-between mb-2">
            <div class="flex align-items-center">
                <p
                    v-if="showProcessingStateInfo"
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
            :class="dataTableHeight"
            :value="dataTableNodes"
            sortField="id"
            :sortOrder="1"
            scrollable
            scrollHeight="flex"
        >
            <Column field="id" :header="$t('id')" sortable />
            <Column field="clientDN.CN" :header="$t('node')" sortable />
            <Column field="tags" :header="$t('tags')" sortable>
                <template #body="slotProps">
                    <EditableTagListView :tags="slotProps.data.tags" />
                </template>
            </Column>
            <Column field="lastContact" :header="$t('lastContact')" sortable>
                <template #body="slotProps">
                    {{ formatDateToGermanLocale(slotProps.data.lastContact) }}
                </template>
            </Column>
            <template v-if="showProcessingStateInfo">
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
            <template #footer>
                {{
                    selectedNodes.length === 1
                        ? $t("oneNode")
                        : $t("xNodes", {
                              numNodes: selectedNodes
                                  ? selectedNodes.length
                                  : 0,
                          })
                }}
            </template>
        </DataTable>
        <ExportTableButton class="mt-3" />
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import Button from "primevue/button";
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import { ManagerNode, RequestExecution, NodeStatusInfo } from "@/utils/Types";
import { RequestState } from "@/utils/Enums";
import MomentWrapper from "@/utils/MomentWrapper";
import ExportTableButton from "@/components/common/ExportTableButton.vue";
import SearchInput from "@/components/common/SearchInput.vue";
import NodeStatusInfoTimeline from "./NodeStatusInfoTimeline.vue";
import TargetNodesCommon from "./TargetNodesCommon.vue";

export default {
    components: {
        Fieldset,
        DataTable,
        Column,
        Button,
        EditableTagListView,
        ExportTableButton,
        SearchInput,
        NodeStatusInfoTimeline,
    },
    mixins: [TargetNodesCommon],
    props: {
        execution: {
            type: Object as () => RequestExecution,
            required: true,
        },
        requestState: {
            type: String as () => keyof typeof RequestState,
            required: true,
        },
        showProcessingStateInfo: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            dataTableNodes: [] as ManagerNode[],
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
            return `h-${heightNumber - 12}-4rem`;
        },
    },
    async mounted() {
        await this.fetchManagerNodes();
        this.filterDataTableNodes("");
    },
    methods: {
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
