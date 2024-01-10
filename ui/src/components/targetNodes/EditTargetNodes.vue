<template>
    <div class="grid flex flex-wrap w-full">
        <div class="col-12 flex flex-wrap justify-content-end align-items-end">
            <SearchInput @inputChange="filterPickListNodes" />
        </div>
        <PickList
            v-model="pickListNodes"
            @update:modelValue="handleListChange"
            dataKey="id"
            :showSourceControls="false"
            :showTargetControls="false"
            :targetListProps="{ class: 'h-25rem w-auto' }"
            class="col-12"
            :sourceListProps="{ class: 'h-25rem w-auto' }"
            :moveToTargetProps="{ class: 'bg-red-600' }"
            :moveAllToTargetProps="{ class: 'bg-red-600' }"
            :moveToSourceProps="{ class: 'bg-green-500' }"
            :moveAllToSourceProps="{ class: 'bg-green-500' }"
            :pt="{
                item: ({ context }) => ({
                    class: [
                        { 'bg-gray-100': !context.active && context.focused },
                    ],
                }),
            }"
        >
            <template #sourceheader>Ausgewählte Standorte</template>
            <template #targetheader>Verfügbare Standorte</template>
            <template #item="slotProps">
                <div class="flex flex-wrap align-items-center m-auto">
                    <div class="flex flex-wrap flex-column w-20rem">
                        <span class="font-bold"
                            >[{{ slotProps.item.id }}]
                            {{ slotProps.item.clientDN.O }}</span
                        >
                        <div class="flex flex-wrap">
                            <TagList :tags="slotProps.item.tags" />
                        </div>
                    </div>
                </div>
            </template>
        </PickList>
    </div>
    <!-- TODO color for items in source/target -->
    <!-- TODO Filter wird resetted wenn mit Filter die PickList geupdated wird -->
</template>

<script lang="ts">
import PickList from "primevue/picklist";
import TagList from "@/components/tags/TagList.vue";
import SearchInput from "@/components/inputs/SearchInput.vue";
import { ManagerNode } from "@/utils/Types";

export default {
    components: {
        PickList,
        TagList,
        SearchInput,
    },
    props: {
        modelValue: {
            type: Array as () => number[],
            required: true,
        },
        allManagerNodes: {
            type: Array as () => ManagerNode[],
            required: true,
        },
    },
    data() {
        return {
            pickListNodes: [[] as ManagerNode[], [] as ManagerNode[]],
        };
    },
    computed: {
        selectedNodes() {
            return this.allManagerNodes
                .filter((node) => this.modelValue.includes(node.id))
                .sort((a, b) => a.id - b.id);
        },
        availableNodes() {
            return this.allManagerNodes
                .filter((node) => !this.modelValue.includes(node.id))
                .sort((a, b) => a.id - b.id);
        },
    },
    watch: {
        modelValue: {
            immediate: true,
            handler() {
                this.filterPickListNodes("");
            },
        },
    },
    methods: {
        filterPickListNodes(searchTerm: string) {
            const filteredSelectedNodes = this.filterNodesBySearchTerm(
                this.selectedNodes,
                searchTerm
            );
            const filteredAvailableNodes = this.filterNodesBySearchTerm(
                this.availableNodes,
                searchTerm
            );
            this.pickListNodes = [
                filteredSelectedNodes,
                filteredAvailableNodes,
            ];
        },
        filterNodesBySearchTerm(
            nodes: ManagerNode[],
            searchTerm: string
        ): ManagerNode[] {
            if (!searchTerm) return nodes;
            return nodes.filter(
                (node) =>
                    node.id
                        .toString()
                        .toLowerCase()
                        .includes(searchTerm.toLowerCase()) ||
                    node.tags.some((tag) =>
                        tag.toLowerCase().includes(searchTerm.toLowerCase())
                    ) ||
                    node.clientDN.O.toLowerCase().includes(
                        searchTerm.toLowerCase()
                    )
            );
        },
        handleListChange() {
            let updatedModelValue = [...this.modelValue];
            this.pickListNodes[0].forEach((node) => {
                if (!updatedModelValue.includes(node.id)) {
                    updatedModelValue.push(node.id);
                }
            });
            updatedModelValue = updatedModelValue.filter(
                (id) => !this.pickListNodes[1].some((node) => node.id === id)
            );
            this.$emit("update:modelValue", updatedModelValue);
        },
    },
};
</script>
