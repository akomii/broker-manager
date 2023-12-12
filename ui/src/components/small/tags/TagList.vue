<template>
    <template v-for="(tag, index) in tags" :key="tag + index">
        <Chip class="py-1 px-2 m-1" :removable="removable" @remove="removeTag(tag)">
            <span class="pi pi-tag"></span>
            <span class="ml-1 text-sm">
                {{ tag }}
            </span>
        </Chip>
    </template>
</template>

<script lang="ts">
import Chip from 'primevue/chip';

export default {
    components: {
        Chip
    },
    props: {
        tags: {
            type: Array as () => String[],
            required: true
        },
        removable: {
            type: Boolean,
            default: false
        }
    },
    methods: {
        removeTag(tagToRemove: String) {
            const updatedTags = this.tags.filter(tag => tag !== tagToRemove);
            this.$emit('update:tags', updatedTags);
        }
    },
}
</script>
