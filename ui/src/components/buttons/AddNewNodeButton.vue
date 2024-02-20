<template>
    <Button :label="$t('addNewNode')" icon="pi pi-plus" severity="success" />
</template>

<script lang="ts">
import Button from "primevue/button";

export default {
    components: {
        Button,
    },
};
</script>
