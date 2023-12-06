<template>
        <template v-if="editable">
            <EditTargetNodes :modelValue="modelValue" @update:modelValue="handleModelValueChange"
                :allManagerNodes="allManagerNodes" />
        </template>
        <template v-else>
            <div class="grid nested-grid flex flex-wrap w-full">
                <div class="col-6">
                    <div class="grid">
                        <div class="col-12">
                            <p class="font-semibold text-primary text-lg underline m-0">
                                Aktuelle Ausführung: {{ execution.sequenceId }}
                            </p>
                        </div>
                        <div class="col-12">
                            <p class="font-semibold text-primary text-2xl m-0">
                                Zustimmung: {{ executionConsent }} / {{ execution.nodeStatusInfos.length }}
                            </p>
                        </div>
                    </div>
                </div>
                <div class="col-6 flex flex-wrap justify-content-end align-items-end">
                    <SearchInput @inputChange="updateGlobalFilter" />
                </div>
            </div>

            <DataTable ref="dt" v-model:filters="filters" :value="selectedNodes" tableStyle="min-width: 50rem"
                sortField="id" :sortOrder="1" :globalFilterFields="['id', 'clientDN.L', 'tags', 'lastContact']" scrollable
                scrollHeight="500px">
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
                        {{ formatToGermanDate(slotProps.data.lastContact) }}
                    </template>
                </Column>
                <Column field="state" header="Bearbeitungsstatus">
                    <template #body="slotProps">
                        TODO
                        OVERLAYPANEL + TIMELINE
                    </template>
                </Column>
            </DataTable>
            <ExportCsvButton :datatableRef="$refs.dt" />
        </template>
</template>

<script lang="ts">
import Fieldset from 'primevue/fieldset';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Row from 'primevue/row';
import { FilterMatchMode } from 'primevue/api';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';

import { ManagerNode, RequestExecution } from '@/utils/Types';
import { TestDataService } from '@/service/TestDataService';
import TagChip from '@/components/small/TagChip.vue';
import ExportCsvButton from '@/components/small/ExportCsvButton.vue';
import EditTargetNodes from '@/components/requests/targetNodes/EditTargetNodes.vue';
import { formatToGermanDate } from '@/utils/Helper.ts';
import SearchInput from '@/components/small/SearchInput.vue';

export default {
    components: {
        Fieldset,
        DataTable,
        Column,
        Row,
        InputText,
        TagChip,
        Button,
        EditTargetNodes,
        ExportCsvButton,
        SearchInput
    },
    props: {
        modelValue: {
            type: Array as () => number[],
            required: true
        },
        execution: {
            type: Object as () => RequestExecution,
            required: true
        },
        editable: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            allManagerNodes: [] as ManagerNode[],
            filters: {
                global: { value: '', matchMode: FilterMatchMode.CONTAINS },
            }
        };
    },
    computed: {
        selectedNodes() {
            return this.allManagerNodes.filter(node => this.modelValue.includes(node.id));
        },
        executionConsent() {
            return this.execution.nodeStatusInfos.filter(nodeInfo => nodeInfo.completed !== null).length;
        }
    },
    mounted() {
        TestDataService.getNodes().then((data: ManagerNode[]) => {
            this.allManagerNodes = data;
        });
    },
    methods: {
        formatToGermanDate,
        updateGlobalFilter(searchTerm: string) {
            this.filters.global.value = searchTerm;
        },
        handleModelValueChange(newModelValue: ManagerNode[]) {
            this.$emit('update:modelValue', newModelValue);
        },
    }
};
</script>