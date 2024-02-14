<template>
    <Column field="sequenceId" :header="$t('sequenceId')" sortable>
        <template #body="slotProps">
            <div class="flex gap-1">
                <span>
                    {{ slotProps.data.sequenceId }}
                </span>
                <ResultsNotDownloadedIcon
                    v-if="
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
import { ResultsDownloadLog } from "@/utils/Types.ts";
import { ExecutionState } from "@/utils/Enums.ts";

export default {
    components: {
        Column,
        ResultsNotDownloadedIcon,
        NewResultsAvailableIcon,
    },
    methods: {
        isResultsDownloadLogEmpty(downloadLog: ResultsDownloadLog[]): boolean {
            return downloadLog.length === 0;
        },
        isExecutionNotPending(state: ExecutionState): boolean {
            return state !== ExecutionState.PENDING;
        },
    },
};
</script>
