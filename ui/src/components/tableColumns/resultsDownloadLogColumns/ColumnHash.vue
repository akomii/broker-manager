<template>
    <Column field="hashValue" :header="$t('hash')" />
    <Column field="hashAlgorithm" :header="$t('algorithm')" />
</template>

<script lang="ts">
import Column from "primevue/column";

// TODO refactor and add docs
export default {
    components: {
        Column,
    },
};
</script>
