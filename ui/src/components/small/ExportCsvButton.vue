<template>
    <Button icon="pi pi-external-link" label="Export" class="mt-2" @click="exportCSV" />
</template>
  
<script lang="ts">
import Button from 'primevue/button';

export default {
    components: {
        Button
    },
    emits: ['exportCSV'],
    methods: {
        exportCSV() {
            this.$emit('exportCSV');
        }
    }
};
</script>
