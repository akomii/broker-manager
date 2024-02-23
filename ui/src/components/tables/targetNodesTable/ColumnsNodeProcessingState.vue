<template>
    <Column field="state" :header="$t('processingState')">
        <template #body="slotProps">
            <NodeStatusInfoTimeline
                v-if="
                    isNodeStatusInfoNotEmptyOrUndefined(
                        slotProps.data.nodeStatusInfo
                    )
                "
                :nodeStatusInfo="slotProps.data.nodeStatusInfo"
            />
            <NotAvailableIcon v-else class="flex justify-content-center" />
        </template>
    </Column>
    <Column field="msg" header="">
        <template #body="slotProps">
            <NodeStatusMessageButton
                :statusMessage="slotProps.data.nodeStatusInfo?.statusMessage"
            />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import NodeStatusInfoTimeline from "@/components/dialogs/NodeStatusInfoTimeline.vue";
import NodeStatusMessageButton from "@/components/buttons/NodeStatusMessageButton.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";
import { NodeStatusInfo } from "@/utils/Types";

// TODO refactor and add docs
export default {
    components: {
        Column,
        NodeStatusInfoTimeline,
        NodeStatusMessageButton,
        NotAvailableIcon,
    },
    methods: {
        isNodeStatusInfoNotEmptyOrUndefined(
            nodeStatusInfo: NodeStatusInfo | undefined
        ): boolean {
            if (!nodeStatusInfo) {
                return false;
            }
            const properties = Object.keys(nodeStatusInfo) as Array<
                keyof NodeStatusInfo
            >;
            return properties.some((prop) => {
                if (prop === "nodeId") {
                    return false;
                }
                return nodeStatusInfo[prop] !== null;
            });
        },
    },
};
</script>
