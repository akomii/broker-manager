<template>
    <Column field="query.title" :header="$t('title')" sortable />
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
