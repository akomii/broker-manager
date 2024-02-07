<template>
    <Button
        icon="pi pi-download"
        v-tooltip.bottom="$t('exportCurrentTable')"
        outlined
        @click="exportCSV()"
    />
</template>

<script lang="ts">
import Button from "primevue/button";

// TODO nested table components are not exported correctly
export default {
    components: {
        Button,
    },
    props: {
        dt: {
            type: Object, //must be a primvevue datatable
            required: true,
        },
    },
    methods: {
        exportCSV() {
            this.dt.exportCSV();
        },
    },
};
</script>
