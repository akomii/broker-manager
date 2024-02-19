<template>
    <Column field="query.principal.name" :header="$t('principal')" sortable/>
</template>

<script lang="ts">
import Column from "primevue/column";

export default {
    components: {
        Column,
    },
};
</script>
