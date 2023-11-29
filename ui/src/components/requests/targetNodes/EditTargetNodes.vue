<template>
    <div class="flex flex-wrap m-2">
        <PickList v-model="products" dataKey="id" :pt="{
            sourceList: { style: { height: '350px', width: '360px' } },
            targetList: { style: { height: '350px', width: '360px' } },
            sourcecontrols: { style: { display: 'none' } },
            targetcontrols: { style: { display: 'none' } },
            moveToTargetButton: { root: { class: 'bg-red-600' } },
            moveAllToTargetButton: { root: { class: 'bg-red-600' } },
            moveToSourceButton: { root: { class: 'bg-green-500' } },
            moveAllToSourceButton: { root: { class: 'bg-green-500' } },
            item: ({ context }) => ({ class: [{ 'bg-gray-100': !context.active && context.focused }] }),
        }">
            <template #sourceheader>Ausgewählte Standorte</template>
            <template #targetheader>Verfügbare Standorte</template>
            <template #item="slotProps">
                <div class="flex flex-wrap align-items-center m-auto">
                    <div class="flex flex flex-column w-20rem">
                        <span class="font-bold">[{{ slotProps.item.id }}] {{ slotProps.item.clientDN.O }}</span>
                        <div class="p-flex p-flex-wrap">
                            <TagChip v-for="tag in slotProps.item.tags">
                                {{ tag }}
                            </TagChip>
                        </div>
                    </div>
                </div>
            </template>
        </PickList>

        <!-- TODO color for items in source/target -->
        <!-- TODO sort by ID -->
        <!-- TODO search -->
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
import PickList from 'primevue/picklist';

export default {
    components: {
        Fieldset,
        DataTable,
        Column,
        Row,
        InputText,
        TagChip,
        Button,
        PickList
    },
    props: {
        modelValue: { type: Array as () => ManagerNode[], required: true },
    },
    data() {
        return {
            allManagerNodes: [] as ManagerNode[],
            localModelValue: [] as ManagerNode[],
            filters: {
                global: { value: null, matchMode: FilterMatchMode.CONTAINS },
            },
            products: [[], []],
        };
    },
    mounted() {
        TestDataService.getNodes().then((data: ManagerNode[]) => {
            this.allManagerNodes = data;
            this.updateFilteredNodes();
        });
    },

    watch: {
        modelValue: {
            immediate: true,
            handler() {
                this.updateFilteredNodes();
            },
        },
    },
    methods: {
        updateFilteredNodes() {
            if (this.modelValue && this.modelValue.length > 0) {
                const modelValueIds = new Set(this.modelValue.map(node => node.id));
                this.allManagerNodes = this.allManagerNodes.filter(node =>
                    !modelValueIds.has(node.id)
                );
            }
            this.localModelValue = this.modelValue || [];
            this.products = [this.localModelValue, this.allManagerNodes];
        },
        updateModelValue() {
            this.$emit('update:modelValue', this.localModelValue);
        },
    }
};
</script>
