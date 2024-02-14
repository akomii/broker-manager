<template>
    <DataTable :value="mergedLogAndState" class="flex justify-content-center">
        <Column :header="$t('date')" field="date" sortable>
            <template #body="slotProps">
                {{ formatDate(slotProps.data.date) }}
            </template>
        </Column>
        <Column :header="$t('user')" field="user" sortable />
        <Column :header="$t('organizations')" field="userOrgs" sortable>
            <template #body="slotProps">
                <SimpleChipList :chips="slotProps.data.userOrgs" />
            </template>
        </Column>
        <Column :header="$t('hash')" field="hashValue"></Column>
        <Column :header="$t('algorithm')" field="hashAlgorithm"></Column>
        <Column field="actions" bodyStyle="text-align: center">
            <template #body="slotProps">
                <DownloadButton
                    :tooltipLabel="$t('downloadThisResultsAgain')"
                    :disabled="
                        isExecutionArchived(slotProps.data.executionState)
                    "
                    :action="dummyAction"
                />
            </template>
        </Column>
    </DataTable>
</template>

<script lang="ts">
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import SimpleChipList from "@/components/tags/SimpleChipList.vue";
import DownloadButton from "@/components/common/DownloadButton.vue";
import MomentWrapper from "@/utils/MomentWrapper.ts";
import { ExecutionState } from "@/utils/Enums";
import { ResultsDownloadLog } from "@/utils/Types";

export default {
    components: {
        DataTable,
        Column,
        DownloadButton,
        SimpleChipList,
    },
    props: {
        downloadLog: {
            type: Array as () => ResultsDownloadLog[],
            required: true,
        },
        executionState: {
            type: String as () => ExecutionState,
            required: true,
        },
    },
    computed: {
        mergedLogAndState() {
            return this.downloadLog.map((log) => ({
                ...log,
                executionState: this.executionState,
            }));
        },
    },
    methods: {
        formatDate(date: Date): string {
            return MomentWrapper.formatDateToGermanLocale(date);
        },
        isExecutionArchived(state: ExecutionState): boolean {
            return state === ExecutionState.ARCHIVED;
        },
        dummyAction() {
            console.log("dummy action");
        },
    },
};
</script>
