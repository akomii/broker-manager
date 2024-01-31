<template>
    <div v-if="mostActualState">
        <Button @click="togglePanel" plain text>
            {{ mostActualState }}
        </Button>
        <OverlayPanel ref="panel" showCloseIcon>
            <Timeline :value="statusArray">
                <template #opposite="timelineSlotProps">
                    <small>{{ timelineSlotProps.item.date }}</small>
                </template>
                <template #content="timelineSlotProps">
                    <small>{{ timelineSlotProps.item.status }}</small>
                </template>
            </Timeline>
        </OverlayPanel>
    </div>
    <i v-else class="pi pi-minus text-gray-700 flex justify-content-center" />
</template>

<script lang="ts">
import Button from "primevue/button";
import OverlayPanel from "primevue/overlaypanel";
import Timeline from "primevue/timeline";
import { NodeStatusInfo } from "@/utils/Types";
import { NodeState } from "@/utils/Enums";
import MomentWrapper from "@/utils/MomentWrapper";

const orderedStates: NodeState[] = [
    NodeState.RETRIEVED,
    NodeState.QUEUED,
    NodeState.PROCESSING,
    NodeState.REJECTED,
    NodeState.COMPLETED,
    NodeState.FAILED,
    NodeState.EXPIRED,
];

export default {
    components: {
        Button,
        OverlayPanel,
        Timeline,
    },
    props: {
        nodeStatusInfo: {
            type: Object as () => NodeStatusInfo,
            required: true,
        },
    },
    computed: {
        mostActualState(): string | null {
            let currentState: NodeState = NodeState.RETRIEVED;
            let latestTimestamp: Date | null = null;
            let allStatesNull = true;
            for (const state of orderedStates) {
                const timestamp = (this.nodeStatusInfo as any)[
                    state.toLowerCase()
                ] as Date | null;
                if (timestamp) {
                    allStatesNull = false;
                    if (!latestTimestamp || timestamp > latestTimestamp) {
                        latestTimestamp = timestamp;
                        currentState = state;
                    }
                }
            }
            if (allStatesNull) {
                return null;
            }
            return this.$t(`enums.nodeState.${currentState}`);
        },
        statusArray(): { status: string; date: string }[] {
            return orderedStates
                .filter(
                    (state) => (this.nodeStatusInfo as any)[state.toLowerCase()]
                )
                .map((state) => {
                    const dateValue = (this.nodeStatusInfo as any)[
                        state.toLowerCase()
                    ];
                    let formattedDate = "";
                    if (dateValue instanceof Date) {
                        formattedDate =
                            MomentWrapper.formatDateToGermanLocale(dateValue);
                    } else if (typeof dateValue === "string") {
                        formattedDate = MomentWrapper.formatDateToGermanLocale(
                            new Date(dateValue)
                        );
                    }
                    return {
                        status: this.$t(`enums.nodeState.${state}`),
                        date: formattedDate,
                    };
                });
        },
    },
    methods: {
        togglePanel(event: Event) {
            const overlayPanel = this.$refs.panel as OverlayPanel;
            overlayPanel?.toggle(event);
        },
    },
};
</script>
