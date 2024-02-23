<template>
    <Column field="id" :header="$t('id')" sortable />
</template>

<script lang="ts">
import Column from "primevue/column";

// TODO remove
export default {
    components: {
        Column,
    },
};
</script>
