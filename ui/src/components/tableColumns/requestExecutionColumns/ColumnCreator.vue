<template>
    <Column field="creator" :header="$t('creator')" sortable />
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
