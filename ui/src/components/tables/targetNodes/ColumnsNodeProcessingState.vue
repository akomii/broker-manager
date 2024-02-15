<template>
    <Column field="state" :header="$t('processingState')">
        <template #body="slotProps">
            <NodeStatusInfoTimelineButton
                :nodeStatusInfo="slotProps.data.nodeStatusInfo"
            />
        </template>
    </Column>
    <Column field="msg" header="">
        <template #body="slotProps">
            <NodeStatusMessageButton
                :statusMessage="slotProps.data.nodeStatusInfo.statusMessage"
            />
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import NodeStatusInfoTimelineButton from "@/components/buttons/NodeStatusInfoTimelineButton.vue";
import NodeStatusMessageButton from "@/components/buttons/NodeStatusMessageButton.vue";

export default {
    components: {
        Column,
        NodeStatusInfoTimelineButton,
        NodeStatusMessageButton,
    },
};
</script>
