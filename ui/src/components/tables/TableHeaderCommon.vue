<template>
    <div class="flex flex-wrap justify-content-between">
        <div class="flex flex-wrap align-items-center gap-3">
            <slot name="title"></slot>
            <slot name="filter-controls"></slot>
        </div>

        <div class="flex flex-wrap align-items-center gap-3">
            <SearchInput @update:search="$emit('search', $event)" />
            <slot name="new-item-actions"></slot>
        </div>
    </div>
</template>

<script lang="ts">
import SearchInput from "@/components/tables/SearchInput.vue";

export default {
    components: {
        SearchInput,
    },
};
</script>
