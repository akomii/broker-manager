<template>
    <Column
        field="acceptance"
        :header="$t('acceptance')"
        bodyStyle="text-align: center"
    >
        <template #body="slotProps">
            <TargetNodesTableDialog
                v-if="isNodeStatusInfoNotEmpty(slotProps.data.nodeStatusInfos)"
                :targetNodes="slotProps.data.nodes"
                :targetNodeStatusInfos="slotProps.data.nodeStatusInfos"
                :sequenceId="slotProps.data.sequenceId"
            />
            <NotAvailableIcon v-else />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import TargetNodesTableDialog from "@/components/dialogs/TargetNodesTableDialog.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";

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
