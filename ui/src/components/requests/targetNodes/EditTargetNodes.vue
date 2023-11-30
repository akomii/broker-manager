<template>
    <div class="flex flex-wrap m-2">
        <PickList v-model="pickListNodes" dataKey="id" :pt="{
            sourceList: { style: { height: '350px', width: '360px' } },
            targetList: { style: { height: '350px', width: '360px' } },
            sourcecontrols: { style: { display: 'none' } },
            targetcontrols: { style: { display: 'none' } },
            moveToTargetButton: { root: { class: 'bg-red-600' } },
            moveAllToTargetButton: { root: { class: 'bg-red-600' } },
            moveToSourceButton: { root: { class: 'bg-green-500' } },
            moveAllToSourceButton: { root: { class: 'bg-green-500' } },
            item: ({ context }) => ({ class: [{ 'bg-gray-100': !context.active && context.focused }] }),
        }" @update:modelValue="(pickList) => $emit('update:modelValue', pickList[0])">
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
        <!-- TODO search -->
    </div>
    {{ modelValue }}
</template>

<script lang="ts">
import PickList from 'primevue/picklist';
import TagChip from '@/components/common/TagChip.vue';
import { TestDataService } from '@/service/TestDataService';
import { ManagerNode } from '@/utils/Types';

export default {
    components: {
        PickList,
        TagChip,
    },
    props: {
        modelValue: { type: Array as () => ManagerNode[], required: true },
    },
    data() {
        return {
            allManagerNodes: [] as ManagerNode[],
            pickListNodes: [[] as ManagerNode[], [] as ManagerNode[]],
        };
    },
    computed: {
        availableNodes() {
            const selectedIds = new Set(this.modelValue.map(node => node.id));
            return this.allManagerNodes.filter(node => !selectedIds.has(node.id)).sort((a, b) => a.id - b.id);
        },
    },
    watch: {
        modelValue: {
            immediate: true,
            handler() {
                this.updatePickListNodes();
            },
        },
    },
    mounted() {
        TestDataService.getNodes().then((data: ManagerNode[]) => {
            this.allManagerNodes = data;
            this.updatePickListNodes();
        });
    },
    methods: {
        updatePickListNodes() {
            const sortedModelValue = this.modelValue.slice().sort((a, b) => a.id - b.id);
            this.pickListNodes = [sortedModelValue, this.availableNodes];
        },
    }
};
</script>
