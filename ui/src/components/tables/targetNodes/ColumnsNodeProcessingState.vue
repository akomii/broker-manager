<template>
    <Column
        field="state"
        :header="$t('processingState')"
        bodyStyle="text-align: center"
    >
        <template #body="slotProps">
            <NodeStatusInfoTimelineButton
                v-if="
                    isNodeStatusInfoNotEmptyOrUndefined(
                        slotProps.data.nodeStatusInfo
                    )
                "
                :nodeStatusInfo="slotProps.data.nodeStatusInfo"
            />
            <NotAvailableIcon v-else />
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
import NodeStatusInfoTimelineButton from "@/components/buttons/NodeStatusInfoTimelineButton.vue";
import NodeStatusMessageButton from "@/components/buttons/NodeStatusMessageButton.vue";
import NotAvailableIcon from "@/components/icons/NotAvailableIcon.vue";
import { NodeStatusInfo } from "@/utils/Types";

export default {
    components: {
        Column,
        NodeStatusInfoTimelineButton,
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
