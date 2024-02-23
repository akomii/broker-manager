<template>
    <Column field="id" :header="$t('requestId')" sortable />
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
