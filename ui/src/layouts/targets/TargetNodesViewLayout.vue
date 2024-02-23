<template>
    <Fieldset :legend="$t('targetNodes')" :class="fieldSetHeight">
        <TargetNodesTable
            :targetNodes="targetNodes"
            :targetNodeStatusInfos="targetNodeStatusInfos"
            :sequenceId="execution.sequenceId"
            :showProcessingStateInfo="showProcessingStateInfo"
            :dataTableHeight="dataTableHeight"
        />
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import TargetNodesTable from "@/components/tables/targetNodesTable/TargetNodesTable.vue";
import { ManagerNode, RequestExecution, NodeStatusInfo } from "@/utils/Types";
import { TestDataService } from "@/services/TestDataService";

// TODO refactor and add docs
export default {
    components: {
        Fieldset,
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
            allManagerNodes: [] as ManagerNode[],
        };
    },
    computed: {
        dataTableHeight(): string {
            const heightNumber = parseInt(this.fieldSetHeight.split("-")[1]);
            return `h-${heightNumber - 10}-4rem`;
            // TODO return `calc(${this.fieldSetHeight} - 10rem)`;
        },
        targetNodes(): ManagerNode[] {
            return this.allManagerNodes.filter((node) =>
                this.targetNodeIds.includes(node.id)
            );
        },
        targetNodeStatusInfos(): NodeStatusInfo[] {
            return this.execution.nodeStatusInfos.filter((info) =>
                this.targetNodeIds.includes(info.nodeId)
            );
        },
    },
    async mounted() {
        await this.fetchManagerNodes();
    },
    methods: {
        async fetchManagerNodes(): Promise<void> {
            this.allManagerNodes = await TestDataService.getNodes();
        },
    },
};
</script>
