<template>
    <Button
        icon="pi pi-external-link"
        label="Aktuelle Tabelle exportieren"
        outlined
        class="mt-2"
        @click="exportTable"
    />
</template>

<script lang="ts">
import Button from "primevue/button";

export default {
    components: {
        Button,
    },
    props: {
        datatableRef: {
            type: Object,
            required: true,
        },
    },
    methods: {
        exportTable() {
            if (this.datatableRef) {
                this.datatableRef.exportTable();
            }
        },
    },
};
</script>
