<template>
    <TableHeaderCommon @search="$emit('search', $event)">
        <template #title>
            <h2 class="text-700">{{ $t("nodesOverview") }}</h2>
        </template>

        <template #filter-controls>
            <span>
                <Checkbox
                    v-model="showHidden"
                    :binary="true"
                    inputId="toggleHidden"
                    @update:modelValue="emitToggleHidden"
                />
                <label for="toggleHidden" class="ml-2">
                    {{ $t("showUnsubscribedNodes") }}
                </label>
            </span>
        </template>

        <template #new-item-actions>
            <AddNewNodeButton />
        </template>
    </TableHeaderCommon>
</template>

<script lang="ts">
import Checkbox from "primevue/checkbox";
import TableHeaderCommon from "@/components/tables/TableHeaderCommon.vue";
import AddNewNodeButton from "@/components/buttons/AddNewNodeButton.vue";

// TODO refactor and add docs
export default {
    components: {
        Checkbox,
        TableHeaderCommon,
        AddNewNodeButton,
    },
    data() {
        return {
            showHidden: false,
        };
    },
    methods: {
        emitToggleHidden(val: boolean) {
            this.$emit("update:showHidden", val);
        },
    },
};
</script>
