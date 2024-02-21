<template>
    <Column :header="$t('sequenceId')" sortable>
        <template #body="slotProps">
            <div class="flex gap-1">
                <span>
                    {{ slotProps.data.sequenceId }}
                </span>
                <AnchoredRequestIcon
                    v-if="isSequenceIdAnchored(slotProps.data.sequenceId)"
                />
                <ResultsNotDownloadedIcon
                    v-if="
                        slotProps.data.resultsDownloadLog &&
                        isResultsDownloadLogEmpty(
                            slotProps.data.resultsDownloadLog
                        ) &&
                        isExecutionNotPending(slotProps.data.executionState)
                    "
                />
                TODO
                <!-- TODO new Results Available-->
                <NewResultsAvailableIcon />
            </div>
        </template>
    </Column>
</template>

<script lang="ts">
import Column from "primevue/column";
import ResultsNotDownloadedIcon from "@/components/icons/ResultsNotDownloadedIcon.vue";
import NewResultsAvailableIcon from "@/components/icons/NewResultsAvailableIcon.vue";
import AnchoredRequestIcon from "@/components/icons/AnchoredRequestIcon.vue";
import { ResultsDownloadLog } from "@/utils/Types.ts";
import { ExecutionState } from "@/utils/Enums.ts";

export default {
    components: {
        Column,
        ResultsNotDownloadedIcon,
        NewResultsAvailableIcon,
        AnchoredRequestIcon,
    },
    props: {
        anchoredSequenceIdRef: {
            type: Number,
            default: 0,
        },
    },
    methods: {
        isResultsDownloadLogEmpty(downloadLog: ResultsDownloadLog[]): boolean {
            return downloadLog.length === 0;
        },
        isExecutionNotPending(state: ExecutionState): boolean {
            return state !== ExecutionState.PENDING;
        },
        isSequenceIdAnchored(sequenceId: Number): boolean {
            return sequenceId === this.anchoredSequenceIdRef;
        },
    },
};
</script>
