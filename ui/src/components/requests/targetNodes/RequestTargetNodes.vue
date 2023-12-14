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

        <DataTable ref="dt" v-model:filters="filters" :value="selectedNodes" tableStyle="min-width: 50rem" sortField="id"
            :sortOrder="1" :globalFilterFields="['id', 'clientDN.L', 'tags', 'lastContact', 'state']" scrollable
            scrollHeight="500px">
            <Column field="id" header="ID" sortable></Column>
            <Column field="clientDN.L" header="Standort" sortable></Column>
            <Column field="tags" header="Tags" sortable>
                <template #body="slotProps">
                    <div class="p-flex p-flex-wrap">
                        <TagList :tags="slotProps.data.tags" />
                    </div>
                </template>
            </Column>
            <Column field="lastContact" header="Letzter Kontakt" sortable>
                <template #body="slotProps">
                    {{ formatDateToGermanLocale(slotProps.data.lastContact) }}
                </template>
            </Column>
            <Column field="state" header="Bearbeitungsstatus">
                <template #body="slotProps">
                    <NodeStatusInfoTimeline :nodeStatusInfo="getNodeStatusInfoForNode(slotProps.data.id)" />
                </template>
            </Column>
            <Column field="msg" header="" class="border-solid">
                <template #body="slotProps">
                    <Button v-if="getNodeStatusInfoForNode(slotProps.data.id).statusMessage"
                        @click="showStatusMessage(slotProps.data.id)"
                        icon="pi pi-exclamation-circle text-xl text-blue-600 m-0" text rounded />
                </template>
            </Column>
        </DataTable>
        <ExportTableButton :datatableRef="$refs.dt" />
    </template>
    <!-- TODO Search does not work for state -->
    <!-- TODO Search does not work for date -->
</template>

<script lang="ts">
import Fieldset from 'primevue/fieldset';
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Row from 'primevue/row';
import { FilterMatchMode } from 'primevue/api';
import InputText from 'primevue/inputtext';
import Button from 'primevue/button';

import { ManagerNode, RequestExecution, NodeStatusInfo } from '@/utils/Types';
import { TestDataService } from '@/service/TestDataService';
import TagList from '@/components/common/tags/TagList.vue';
import ExportTableButton from '@/components/common/buttons/ExportTableButton.vue';
import EditTargetNodes from '@/components/requests/targetNodes/EditTargetNodes.vue';
import MomentWrapper from '@/utils/MomentWrapper';
import SearchInput from '@/components/common/SearchInput.vue';
import NodeStatusInfoTimeline from '@/components/requests/common/NodeStatusInfoTimeline.vue'

export default {
    components: {
        Fieldset,
        DataTable,
        Column,
        Row,
        InputText,
        TagList,
        Button,
        EditTargetNodes,
        ExportTableButton,
        SearchInput,
        NodeStatusInfoTimeline,
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
            },
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
        formatDateToGermanLocale(date: Date): String {
            return MomentWrapper.formatDateToGermanLocale(date);
        },
        updateGlobalFilter(searchTerm: string) {
            this.filters.global.value = searchTerm;
        },
        handleModelValueChange(newModelValue: ManagerNode[]) {
            this.$emit('update:modelValue', newModelValue);
        },
        getNodeStatusInfoForNode(nodeId: number): NodeStatusInfo {
            return this.execution.nodeStatusInfos.find(info => info.nodeId === nodeId);
        },
        showStatusMessage(nodeId: number) {
            const nodeInfo = this.getNodeStatusInfoForNode(nodeId);
            if (nodeInfo && nodeInfo.statusMessage) {
                const newWindow = window.open('', '_blank');
                newWindow.document.write(`<pre>${nodeInfo.statusMessage}</pre>`);
                newWindow.document.title = `Status Message for Node ${nodeId}`;
                newWindow.document.close();
            }
        },
    }
};
</script>