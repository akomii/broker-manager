<template>
    <div class="flex flex-wrap align-items-center">
        <template v-if="!nodeStatusInfo">
            <div class="flex-wrap flex justify-content-center w-full">
                <i class="pi pi-minus text-gray-700" />
            </div>
        </template>
        <template v-else>
            <Button @click="togglePanel" plain text>
                {{ getCurrentState() }}
            </Button>
            <OverlayPanel ref="op" showCloseIcon>
                <Timeline :value="convertToStatusArray()">
                    <template #opposite="timelineSlotProps">
                        <small>{{ timelineSlotProps.item.date }}</small>
                    </template>
                    <template #content="timelineSlotProps">
                        <small>{{ timelineSlotProps.item.status }}</small>
                    </template>
                </Timeline>
            </OverlayPanel>
        </template>
    </div>
</template>

<script lang="ts">
import Button from 'primevue/button';
import OverlayPanel from 'primevue/overlaypanel';
import Timeline from 'primevue/timeline';

import { NodeStatusInfo } from "@/utils/Types";
import { NodeState } from "@/utils/Enums";
import { formatToGermanDate } from "@/utils/Helper.ts";

const orderedStates: NodeState[] = [
    NodeState.RETRIEVED,
    NodeState.QUEUED,
    NodeState.PROCESSING,
    NodeState.REJECTED,
    NodeState.COMPLETED,
    NodeState.FAILED,
    NodeState.EXPIRED,
];

const germanTranslations: { [key in NodeState]: string } = {
    [NodeState.RETRIEVED]: "Abgeholt",
    [NodeState.QUEUED]: "Warteschlange",
    [NodeState.PROCESSING]: "In Bearbeitung",
    [NodeState.REJECTED]: "Abgelehnt",
    [NodeState.COMPLETED]: "Abgeschlossen",
    [NodeState.FAILED]: "Fehlgeschlagen",
    [NodeState.EXPIRED]: "Abgelaufen",
};

export default {
    components: {
        Button,
        OverlayPanel,
        Timeline,
    },
    props: {
        nodeStatusInfo: {
            type: Object as () => NodeStatusInfo,
            required: true
        }
    },
    methods: {
        getCurrentState(): string {
            let currentState: NodeState = NodeState.RETRIEVED;
            let latestTimestamp = null;
            for (const state of orderedStates) {
                const timestamp = this.nodeStatusInfo[state as keyof NodeStatusInfo];
                if (timestamp && (!latestTimestamp || timestamp > latestTimestamp)) {
                    latestTimestamp = timestamp;
                    currentState = state;
                }
            }
            return germanTranslations[currentState];
        },
        convertToStatusArray(): { status: string; date: string }[] {
            const statusArray: { status: string; date: string }[] = [];
            for (const state of orderedStates) {
                const key = state as keyof NodeStatusInfo;
                const timestamp = this.nodeStatusInfo[key];
                if (timestamp) {
                    const formattedDate = formatToGermanDate(timestamp.toString());
                    statusArray.push({ status: germanTranslations[state], date: formattedDate });
                }
            }
            return statusArray;
        },
        togglePanel(event: Event) {
            const overlayPanel = this.$refs.op as OverlayPanel;
            if (overlayPanel && overlayPanel.toggle) {
                overlayPanel.toggle(event);
            }
        }
    }
};
</script>