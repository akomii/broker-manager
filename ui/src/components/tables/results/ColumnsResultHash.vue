<template>
    <Column :header="$t('hash')" field="hashValue" />
    <Column :header="$t('algorithm')" field="hashAlgorithm" />
</template>

<script lang="ts">
import Column from "primevue/column";

export default {
    components: {
        Column,
    },
};
</script>
