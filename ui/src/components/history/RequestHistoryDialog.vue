<template>
    <Button
        icon="pi pi-external-link"
        :label="$t('requestHistory')"
        outlined
        @click="toggleDialog()"
    />
    <Dialog
        v-model:visible="showDialog"
        modal
        :dismissableMask="true"
        :closable="false"
        class="w-8"
    >
        <template #header>
            <h2 class="text-xl font-bold">{{ $t("requestHistory") }}</h2>
            <span class="col" />
            <Button
                icon="pi pi-times"
                severity="primary"
                outlined
                @click="toggleDialog()"
            />
        </template>

        <DataTable
            v-model:expandedRows="expandedRows"
            :value="history"
            sortField="date"
            :sortOrder="-1"
        >
            <Column expander />
            <Column :header="$t('date')" field="date" sortable>
                <template #body="slotProps">
                    {{ formatDate(slotProps.data.date) }}
                </template>
            </Column>
            <Column :header="$t('user')" field="user" sortable />
            <template #expansion="slotProps">
                {{ slotProps.data.clob }}
            </template>
        </DataTable>
    </Dialog>
</template>

<script lang="ts">
import Button from "primevue/button";
import Dialog from "primevue/dialog";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import MomentWrapper from "@/utils/MomentWrapper";
import { ModificationHistoryItem } from "@/utils/Types";

export default {
    components: {
        Button,
        Dialog,
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
            showDialog: false,
            expandedRows: [],
        };
    },
    methods: {
        formatDate(date: Date): string {
            return MomentWrapper.formatDateToGermanLocale(date);
        },
        toggleDialog() {
            this.showDialog = !this.showDialog;
        },
    },
};
</script>
