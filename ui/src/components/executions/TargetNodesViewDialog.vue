<template>
    <Button outlined @click="showDialog = true">
        {{
            $t("XofY", {
                x: countCompletedNodes(),
                y: execution.nodeStatusInfos.length,
            })
        }}
    </Button>
    <Dialog
        v-model:visible="showDialog"
        modal
        :dismissableMask="true"
        :closable="false"
        class="w-8"
    >
        <template #header>
            <span class="w-12" />
            <Button
                icon="pi pi-times"
                severity="primary"
                outlined
                @click="showDialog = false"
            />
        </template>
        <TargetNodesView
            :targetNodeIds="getNodeIdsFromStatusInfo()"
            :fieldSetHeight="'h-full'"
            :execution="execution"
            :showProcessingStateInfo="true"
        />
    </Dialog>
</template>

<script lang="ts">
import Dialog from "primevue/dialog";
import Button from "primevue/button";
import TargetNodesView from "@/components/targetNodes/TargetNodesView.vue";
import { RequestExecution } from "@/utils/Types";

export default {
    components: {
        Dialog,
        Button,
        TargetNodesView,
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
    },
    data() {
        return {
            showDialog: false,
        };
    },
    methods: {
        countCompletedNodes(): number {
            return this.execution.nodeStatusInfos.filter(
                (node) => node.completed !== null
            ).length;
        },
        getNodeIdsFromStatusInfo(): Number[] {
            return this.execution.nodeStatusInfos.map(
                (nodeStatusInfo) => nodeStatusInfo.nodeId
            );
        },
    },
};
</script>
