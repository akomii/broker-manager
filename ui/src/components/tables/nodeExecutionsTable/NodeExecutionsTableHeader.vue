<template>
    <TableHeaderCommon @search="$emit('search', $event)">
        <template #filter-controls>
            <span>
                <Checkbox
                    v-model="showArchived"
                    :binary="true"
                    inputId="toggleArchived"
                    @update:modelValue="emitToggleArchived"
                />
                <label for="toggleArchived" class="ml-2">
                    {{ $t("showArchivedExecutions") }}
                </label>
            </span>
        </template>
    </TableHeaderCommon>
</template>

<script lang="ts">
import Checkbox from "primevue/checkbox";
import TableHeaderCommon from "@/components/tables/TableHeaderCommon.vue";

// TODO refactor and add docs
export default {
    components: {
        Checkbox,
        TableHeaderCommon,
    },
    data() {
        return {
            showArchived: false,
        };
    },
    methods: {
        emitToggleArchived(val: boolean) {
            this.$emit("update:showArchived", val);
        },
    },
};
</script>
