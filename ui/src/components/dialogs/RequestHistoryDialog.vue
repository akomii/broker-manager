<template>
    <DialogCommon
        :buttonIcon="'pi pi-external-link'"
        :buttonLabel="$t('requestHistory')"
        :dialogTitle="$t('requestHistory')"
    >
        <template #dialog-body>
            <DataTable
                v-model:expandedRows="expandedRows"
                :value="history"
                sortField="date"
                :sortOrder="-1"
            >
                <Column expander />
                <Column :header="$t('changeDate')" field="date" sortable>
                    <template #body="slotProps">
                        {{ formatDate(slotProps.data.date) }}
                    </template>
                </Column>
                <Column :header="$t('user')" field="user" sortable />
                <template #expansion="slotProps">
                    {{ slotProps.data.clob }}
                </template>
            </DataTable>
        </template>
    </DialogCommon>
</template>

<script lang="ts">
import DialogCommon from "@/components/dialogs/DialogCommon.vue";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import { ModificationHistoryItem } from "@/utils/Types";
import MomentWrapper from "@/utils/MomentWrapper";

export default {
    components: {
        DialogCommon,
        DataTable,
        Column,
    },
    props: {
        history: {
            type: Array as () => ModificationHistoryItem[],
            required: true,
        },
    },
    data() {
        return {
            expandedRows: [],
        };
    },
    methods: {
        formatDate(date: Date): string {
            return MomentWrapper.formatDateToGermanLocale(date);
        },
    },
};
</script>
