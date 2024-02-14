<template>
    <div class="flex flex-wrap justify-content-between align-items-center">
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
        <SearchInput @update:input="emitSearch" />
    </div>
</template>

<script lang="ts">
import Checkbox from "primevue/checkbox";
import SearchInput from "@/components/common/SearchInput.vue";

export default {
    components: {
        Checkbox,
        SearchInput,
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
        emitSearch(val: String) {
            this.$emit("search", val);
        },
    },
};
</script>
