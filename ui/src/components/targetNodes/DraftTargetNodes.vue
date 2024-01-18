<template>
    <template v-if="editable">
        <EditTargetNodes
            :modelValue="modelValue"
            @update:modelValue="handleModelValueChange"
            :allManagerNodes="allManagerNodes"
        />
    </template>
    <template v-else>
        <div class="flex flex-wrap w-full justify-content-end align-items-end">
            <SearchInput @inputChange="updateGlobalFilter" />
        </div>
        <DataTable
            ref="dt"
            v-model:filters="filters"
            :value="selectedNodes"
            tableStyle="min-width: 50rem"
            sortField="id"
            :sortOrder="1"
            :globalFilterFields="['id', 'clientDN.L', 'tags', 'lastContact']"
            scrollable
            scrollHeight="500px"
        >
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
        </DataTable>
        <ExportTableButton :datatableRef="$refs.dt" />
    </template>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import DataTable from "primevue/datatable";
import Column from "primevue/column";
import Row from "primevue/row";
import { FilterMatchMode } from "primevue/api";
import InputText from "primevue/inputtext";
import Button from "primevue/button";

import { ManagerNode } from "@/utils/Types";
import { TestDataService } from "@/services/TestDataService";
import TagList from "@/components/tags/TagList.vue";
import ExportTableButton from "@/components/buttons/ExportTableButton.vue";
import EditTargetNodes from "@/components/targetNodes/EditTargetNodes.vue";
import MomentWrapper from "@/utils/MomentWrapper";
import SearchInput from "./SearchInput.vue";

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
    },
    props: {
        modelValue: {
            type: Array as () => number[],
            required: true,
        },
        editable: {
            type: Boolean,
            default: false,
        },
    },
    data() {
        return {
            allManagerNodes: [] as ManagerNode[],
            filters: {
                global: { value: "", matchMode: FilterMatchMode.CONTAINS },
            },
        };
    },
    computed: {
        selectedNodes() {
            return this.allManagerNodes.filter((node) =>
                this.modelValue.includes(node.id)
            );
        },
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
            this.$emit("update:modelValue", newModelValue);
        },
    },
};
</script>
