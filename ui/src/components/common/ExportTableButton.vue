<template>
    <Button
        icon="pi pi-download"
        :label="$t('exportCurrentTable')"
        outlined
    />
</template>

<script lang="ts">
import Button from "primevue/button";

export default {
    components: {
        Button,
    },
};
</script>
