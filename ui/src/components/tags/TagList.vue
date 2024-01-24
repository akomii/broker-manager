<template>
    <Chip
        v-for="tag in tags"
        :key="tag"
        class="py-1 px-2 m-1"
        :removable="removable"
        @remove="removeTag(tag)"
    >
        <span class="pi pi-tag"></span>
        <span class="ml-1 text-sm">
            {{ tag }}
        </span>
    </Chip>
</template>

<script lang="ts">
import Chip from "primevue/chip";

export default {
    components: {
        Chip,
    },
    props: {
        tags: {
            type: Array<string>,
            required: true,
        },
        removable: {
            type: Boolean,
            default: false,
        },
    },
    methods: {
        removeTag(tagToRemove: string): void {
            const updatedTags = this.tags.filter((tag) => tag !== tagToRemove);
            this.$emit("update:tags", updatedTags);
        },
    },
};
</script>
