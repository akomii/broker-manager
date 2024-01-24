<template>
    TODO
    <Button
        icon="pi pi-external-link"
        :label="$t('exportCurrentTable')"
        outlined
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
        // datatableRef should be a reference to a DataTable component
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
