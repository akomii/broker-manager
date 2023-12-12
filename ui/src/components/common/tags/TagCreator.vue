<template>
    <Toast />
    <span class="p-input-icon-left">
        <i class="pi pi-tags" />
        <InputText class="w-9rem h-2rem" v-model="newTag" placeholder="Neuer Tag..." @keyup.enter="addTag" />
    </span>
</template>

<script lang="ts">
import InputText from 'primevue/inputtext';
import Toast from 'primevue/toast';

export default {
    components: {
        InputText,
        Toast
    },
    props: {
        tags: {
            type: Set<string>,
            required: true
        }
    },
    data() {
        return {
            newTag: ''
        };
    },
    methods: {
        addTag() {
            const tag = this.newTag.trim();
            if (tag) {
                const updatedTags = new Set(this.tags);
                if (updatedTags.has(tag)) {
                    this.$toast.add({ severity: 'info', summary: 'Info', detail: `Tag \"${tag}\" existiert bereits im Objekt`, life: 3000 });
                } else {
                    updatedTags.add(tag);
                    this.$emit('update:tags', updatedTags);
                }
                this.newTag = '';
            }
        }
    }
};
</script>
