<template>
    <Fieldset :legend="$t('targetNodes')" :class="fieldSetHeight">
        <SearchInput
            class="flex flex-wrap justify-content-end mb-2"
            @update:input="filterPickListNodes"
        />
        <!-- TODO Optional: red/green color for items in source/target -->
        <!-- TODO order by id when moving-->
        <!-- TODO fix height of Picklist-->
        <PickList
            v-model="pickListNodes"
            @update:modelValue="handleListChange"
            :showSourceControls="false"
            :showTargetControls="false"
            :targetListProps="{ class: 'h-30rem' }"
            :sourceListProps="{ class: 'h-30rem' }"
            :moveToTargetProps="{ class: 'bg-red-600' }"
            :moveAllToTargetProps="{ class: 'bg-red-600' }"
            :moveToSourceProps="{ class: 'bg-green-500' }"
            :moveAllToSourceProps="{ class: 'bg-green-500' }"
        >
            <template #sourceheader>{{ $t("selectedNodes") }}</template>
            <template #targetheader>{{ $t("availableNodes") }}</template>
            <template #item="slotProps">
                <div class="flex flex-wrap flex-column">
                    <span class="font-bold">
                        [{{ slotProps.item.id }}]
                        {{ slotProps.item.clientDN.CN }}
                    </span>
                    <span class="flex flex-wrap">
                        <EditableTagListView :tags="slotProps.item.tags" />
                    </span>
                </div>
            </template>
        </PickList>
    </Fieldset>
</template>

<script lang="ts">
import Fieldset from "primevue/fieldset";
import PickList from "primevue/picklist";
import EditableTagListView from "@/components/tags/EditableTagListView.vue";
import { ManagerNode } from "@/utils/Types";
import SearchInput from "./SearchInput.vue";
import TargetNodesCommon from "./TargetNodesCommon.vue";

export default {
    components: {
        Fieldset,
        PickList,
        EditableTagListView,
        SearchInput,
    },
    mixins: [TargetNodesCommon],
    data() {
        return {
            pickListNodes: [[] as ManagerNode[], [] as ManagerNode[]],
        };
    },
    async mounted() {
        await this.fetchManagerNodes();
        this.filterPickListNodes("");
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
        handleListChange() {
            let updatedNodeIds = [...this.targetNodeIds];
            this.pickListNodes[0].forEach((node) => {
                if (!updatedNodeIds.includes(node.id)) {
                    updatedNodeIds.push(node.id);
                }
            });
            updatedNodeIds = updatedNodeIds.filter(
                (id) => !this.pickListNodes[1].some((node) => node.id === id)
            );
            this.$emit("update:targetNodeIds", updatedNodeIds);
        },
    },
};
</script>
