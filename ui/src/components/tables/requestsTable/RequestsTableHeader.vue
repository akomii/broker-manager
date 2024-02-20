<template>
    <TableHeaderCommon @search="$emit('search', $event)">
        <template #title>
            <h2 class="text-700">{{ $t("requestsOverview") }}</h2>
        </template>

        <template #filter-controls>
            <div class="grid">
                <span class="col-12">
                    <Checkbox
                        v-model="showArchived"
                        :binary="true"
                        inputId="toggleArchived"
                        @update:modelValue="emitToggleArchived"
                    />
                    <label for="toggleArchived" class="ml-2">
                        {{ $t("showArchivedRequests") }}
                    </label>
                </span>
                <span class="col-12">
                    <Checkbox
                        v-model="showDraft"
                        :binary="true"
                        inputId="toggleDraft"
                        @update:modelValue="emitToggleDraft"
                    />
                    <label for="toggleDraft" class="ml-2">
                        {{ $t("showDrafts") }}
                    </label>
                </span>
            </div>
        </template>

        <template #new-item-actions>
            <NewDraftButton />
        </template>
    </TableHeaderCommon>
</template>

<script lang="ts">
import Checkbox from "primevue/checkbox";
import TableHeaderCommon from "@/components/tables/TableHeaderCommon.vue";
import NewDraftButton from "@/components/buttons/NewDraftButton.vue";

export default {
    components: {
        Checkbox,
        TableHeaderCommon,
        NewDraftButton,
    },
    data() {
        return {
            showArchived: false,
            showDraft: false,
        };
    },
    methods: {
        emitToggleArchived(val: boolean) {
            this.$emit("update:showArchived", val);
        },
        emitToggleDraft(val: boolean) {
            this.$emit("update:showDraft", val);
        },
    },
};
</script>
