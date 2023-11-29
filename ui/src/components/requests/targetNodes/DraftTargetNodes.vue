<template>
    <div class="flex m-2 max-w-20rem grid">
        <template v-if="editable">
            <EditTargetNodes v-model="localModelValue"></EditTargetNodes>
        </template>
        <template v-else>
            <DataTable v-model:filters="filters" :value="localModelValue" tableStyle="min-width: 50rem" sortField="id"
                :sortOrder="1" :globalFilterFields="['id', 'clientDN.L', 'tags', 'lastContact']" scrollable
                scrollHeight="500px">
                <template #header>
                    <div class="flex justify-content-between flex-wrap">
                        <h2>Angezielte Standorte</h2>
                        <span class="p-input-icon-left">
                            <i class="pi pi-search" />
                            <InputText v-model="filters['global'].value" placeholder="Suche..." />
                        </span>
                    </div>
                </template>
                <Column field="id" header="ID" sortable></Column>
                <Column field="clientDN.L" header="Standort" sortable></Column>
                <Column field="tags" header="Tags" sortable>
                    <template #body="slotProps">
                        <div class="p-flex p-flex-wrap">
                            <TagChip v-for="tag in slotProps.data.tags">
                                {{ tag }}
                            </TagChip>
                        </div>
                    </template>
                </Column>
                <Column field="lastContact" header="Letzter Kontakt" sortable>
                    <template #body="slotProps">
                        {{ formatGermanDate(slotProps.data.lastContact) }}
                    </template>
                </Column>
            </DataTable>
            <Button icon="pi pi-external-link" label="Export" class="mt-2" @click="exportCSV($event)" />
        </template>
    </div>
</template>

<script lang="ts">
import Fieldset from 'primevue/fieldset';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Row from 'primevue/row';
import { FilterMatchMode } from 'primevue/api';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';

import { ManagerNode } from '@/utils/Types';
import { TestDataService } from '@/service/TestDataService';
import TagChip from '@/components/common/TagChip.vue';
import EditTargetNodes from '@/components/requests/targetNodes/EditTargetNodes.vue';

export default {
    components: {
        Fieldset,
        DataTable,
        Column,
        Row,
        InputText,
        TagChip,
        Button,
        EditTargetNodes
    },
    props: {
        modelValue: { type: Array as () => number[], required: true },
        editable: { type: Boolean, default: false },
    },
    data() {
        return {
            allManagerNodes: [] as ManagerNode[],
            localModelValue: [] as ManagerNode[],
            filters: {
                global: { value: null, matchMode: FilterMatchMode.CONTAINS },
            }
        };
    },
    mounted() {
        TestDataService.getNodes().then((data: ManagerNode[]) => {
            this.allManagerNodes = data;
            this.localModelValue = this.allManagerNodes.filter(node => this.modelValue.includes(node.id));
        });
    },
    watch: {
        modelValue: {
            immediate: true,
            handler(newValue) {
                if (newValue && newValue.length > 0) {
                    if (this.allManagerNodes.length > 0) {
                        this.localModelValue = this.allManagerNodes.filter(node =>
                            this.modelValue.includes(node.id)
                        );
                    }
                }
            },
        },
    },
    methods: {
        updateModelValue() {
            this.$emit('update:modelValue', this.localModelValue.map(node => node.id));
        },
        formatGermanDate(dateString) {
            const date = new Date(dateString);
            const options = {
                year: 'numeric', month: '2-digit', day: '2-digit',
                hour: '2-digit', minute: '2-digit', hour12: false
            };
            return new Intl.DateTimeFormat('de-DE', options).format(date);
        },
        exportCSV() {
            this.$refs.dt.exportCSV();
        }
    }
};
</script>
