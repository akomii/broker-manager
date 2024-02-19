<template>
    <div class="flex flex-wrap justify-content-between align-items-center">
        <div>
            <span class="field-checkbox">
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
            <span class="field-checkbox">
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
        <SearchInput @update:input="emitSearch" />
    </div>
</template>

<script lang="ts">
import Checkbox from "primevue/checkbox";
import SearchInput from "@/components/tables/SearchInput.vue";

export default {
    components: {
        Checkbox,
        SearchInput,
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
        emitSearch(val: String) {
            this.$emit("search", val);
        },
    },
};
</script>
