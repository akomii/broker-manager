<template>
    <Fieldset :legend="$t('targetNodes')" :class="fieldSetHeight">
        <DataTable
            :class="dataTableHeight"
            :value="dataTableNodes"
            sortField="id"
            :sortOrder="1"
            scrollable
            scrollHeight="flex"
            ref="dt"
        >
            <template #header>
                <div class="flex flex-wrap justify-content-between">
                    <div class="flex align-items-center">
                        <span
                            v-if="showProcessingStateInfo"
                            class="font-semibold text-primary text-xl m-0"
                        >
                            {{ $t("executionAcceptanceOfNode") }} [{{
                                execution.sequenceId
                            }}] : {{ requestExecutionOnNodes }} /
                            {{ execution.nodeStatusInfos.length }}
                        </span>
                    </div>
                    <SearchInput @update:input="filterDataTableNodes" />
                </div>
            </template>
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
            <template #empty>
                <p class="flex justify-content-center">
                    {{ $t("noNodesFound") }}
                </p>
            </template>
            <template #footer>
                <div class="flex flex-wrap justify-content-between">
                    <span>
                        {{
                            dataTableNodes.length === 1
                                ? $t("oneNode")
                                : $t("xNodes", {
                                      numNodes: dataTableNodes
                                          ? dataTableNodes.length
                                          : 0,
                                  })
                        }}
                    </span>
                    <ExportTableButton class="mt-3" :dt="$refs.dt" />
                </div>
            </template>
        </DataTable>
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
            return `h-${heightNumber - 10}-4rem`;
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
