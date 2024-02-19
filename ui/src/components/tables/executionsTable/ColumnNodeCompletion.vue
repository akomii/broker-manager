<template>
    <Column :header="$t('acceptance')">
        <template #body="slotProps">
            <TargetNodesTableDialog
                v-if="isNodeStatusInfoNotEmpty(slotProps.data.nodeStatusInfos)"
                :targetNodes="slotProps.data.nodes"
                :targetNodeStatusInfos="slotProps.data.nodeStatusInfos"
                :sequenceId="slotProps.data.sequenceId"
            />
            <NotAvailableIcon v-else class="flex justify-content-center" />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import TargetNodesTableDialog from "@/components/dialogs/TargetNodesTableDialog.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";
import { NodeStatusInfo } from "@/utils/Types";

export default {
    components: {
        Column,
        TargetNodesTableDialog,
        NotAvailableIcon,
    },
    methods: {
        isNodeStatusInfoNotEmpty(nodeStatusInfo: NodeStatusInfo[]): boolean {
            return nodeStatusInfo.length > 0;
        },
    },
};
</script>
