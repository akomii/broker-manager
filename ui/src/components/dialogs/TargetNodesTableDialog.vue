<template>
    <DialogCommon
        :buttonLabel="completedNodeStatusCount"
        :dialogTitle="$t('targetNodes')"
    >
        <template #dialog-body>
            <TargetNodesTable
                class="mx-3"
                :targetNodes="targetNodes"
                :targetNodeStatusInfos="targetNodeStatusInfos"
                :sequenceId="sequenceId"
                :showProcessingStateInfo="true"
                :dataTableHeight="'h-full'"
            />
        </template>
    </DialogCommon>
</template>

<script lang="ts">
import DialogCommon from "@/components/dialogs/DialogCommon.vue";
import TargetNodesTable from "@/components/tables/targetNodes/TargetNodesTable.vue";
import { ManagerNode, NodeStatusInfo } from "@/utils/Types.ts";

export default {
    components: {
        DialogCommon,
        TargetNodesTable,
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
    },
    data() {
        return {
            showDialog: false,
        };
    },
    computed: {
        completedNodeStatusCount(): number {
            return this.targetNodeStatusInfos.filter(
                (nodeInfo) => nodeInfo.completed !== null
            ).length;
        },
    },
};
</script>
